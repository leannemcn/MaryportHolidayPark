package com.example.b00682737.maryportholidaypark.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.example.b00682737.maryportholidaypark.Models.FBbooking;
import com.example.b00682737.maryportholidaypark.RentalCaravans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class EditBookingActivity extends BaseActivity{
    FBbooking currentBooking;

    private DatabaseReference _mDB;

    private String[] bedrooms;
    private String[] extras;

    private ImageView imgCarnaby;
    private ImageView imgDelta;
    private RadioGroup radioCaravan;
    private CalendarView calendarView;
    private Spinner bedroomSpinner;
    private Spinner extraSpinner;

    private int _selectedCaravan = 1;
    private int _selectedBedroom;
    private int _selectedExtra;
    private ArrayList<HashMap<String, Object>> disabledDates;
    private List<Calendar> selectedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        Intent intent = getIntent();
        currentBooking = intent.getParcelableExtra("booking");

        // Checking if booking exists within database
        if (currentBooking == null) {
            finish();
            return;
        }

        bedrooms = RentalCaravans.bedrooms;
        extras = RentalCaravans.extraOptions;
        disabledDates = new ArrayList<>();
        selectedDates = new ArrayList<>();

        imgCarnaby = findViewById(R.id.img_booking_carnaby);
        imgCarnaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent carnabyListIntent = new Intent(EditBookingActivity.this, CarnabyList.class);
                startActivity(carnabyListIntent);
            }
        });
        imgDelta = findViewById(R.id.img_booking_delta);
        imgDelta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent DeltaListIntent = new Intent(EditBookingActivity.this, DeltaList.class);
                startActivity(DeltaListIntent);
            }
        });

        radioCaravan = findViewById(R.id.radio_caravan);
        RadioButton radioCarnaby = findViewById(R.id.radio_carnaby);
        RadioButton radioDelta = findViewById(R.id.radio_delta);
        radioCaravan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.radio_delta) {
                    radioCarnaby.setChecked(false);
                    _selectedCaravan = 2;
                } else {
                    _selectedCaravan = 1;
                    radioDelta.setChecked(false);
                }
                getBookingDateForSelectedBedroom();
            }
        });

        bedroomSpinner = findViewById(R.id.spinner_bedroom);
        initBedroomSpinner();
        bedroomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selectedBedroom = position;
                getBookingDateForSelectedBedroom();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        extraSpinner = findViewById(R.id.spinner_extraoption);
        initExtraSpinner();
        extraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selectedExtra = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnBooking = findViewById(R.id.btn_createbooking);
        btnBooking.setText(R.string.edit_booking);

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedDates = calendarView.getSelectedDates();

                if(selectedDates.size() == 0) {
                    showToastMessage("Please select booking dates!");
                    return;
                }

                updateBooking("");
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        //calendarView.setVisibility(View.GONE);

        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        if (currentBooking.getCaravanId() == 1) {
            radioCarnaby.setChecked(true);
            _selectedCaravan = 1;
        } else {
            radioDelta.setChecked(true);
            _selectedCaravan = 2;
        }

        for (int i = 0; i < bedrooms.length; i++) {
            if (currentBooking.getCaravanBedrooms().equals(bedrooms[i])) {
                _selectedBedroom = i;
                bedroomSpinner.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < extras.length; i++) {
            if (currentBooking.getExtras().equals(extras[i])) {
                _selectedExtra = i;
                extraSpinner.setSelection(i);
                break;
            }
        }

        Date dateCheckIn = DateUtil.parseDataFromFormat12(currentBooking.getCaravanCheckIn());
        ArrayList<Calendar> selectedDateList = new ArrayList<>();
        for (int i = 0; i < currentBooking.getCaravanCheckOut(); i++) {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.setTime(dateCheckIn);
            selectedDate.add(Calendar.DATE, i);
            selectedDateList.add(selectedDate);
        }
        calendarView.setSelectedDates(selectedDateList);

        getBookingDateForSelectedBedroom();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this will inflate the menu and add items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button.
        int id = item.getItemId();


        if(id == R.id.cancelBooking) {
            confirmCancelBooking();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initBedroomSpinner() {
        List<String> bedroomList = new ArrayList<String>();
        bedroomList.clear();
        for(String bedroom: bedrooms) {
            bedroomList.add(bedroom);
        }
        ArrayAdapter<String> bedroomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bedroomList);
        bedroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bedroomSpinner.setAdapter(bedroomAdapter);
    }

    public void initExtraSpinner() {
        List<String> extraList = new ArrayList<String>();
        extraList.clear();
        for(String extra: extras) {
            extraList.add(extra);
        }
        ArrayAdapter<String> extraAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, extraList);
        extraAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        extraSpinner.setAdapter(extraAdapter);
    }

    private void confirmCancelBooking() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Confirm Cancel");
        alertDialogBuilder.setMessage("Would you like to cancel current Booking?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                cancelBooking();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void cancelBooking() {
        showProgressDialog();

        // User have current booking and cancel it
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        RequestParams params = new RequestParams();
        params.put("transactionId", currentBooking.getTransactionId());

        client.post(RentalCaravans.SERVERURL + "/refund", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Debug", "456");
                hideProgressDialog();
                showToastMessage("Cancel booking failed");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                DatabaseReference _mDB = FirebaseDatabase.getInstance().getReference("bookings/" + currentBooking.getKey());
                _mDB.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        hideProgressDialog();

                        if (databaseError == null) {
                            showAlert("Booking is canceled", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                        } else {
                            showAlert(databaseError.getMessage());
                        }
                    }
                });
            }
        });
    }

    public void updateBooking(String transactionId) {

        showProgressDialog();

        HashMap<String, Object> bookingData = new HashMap<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        _mDB = FirebaseDatabase.getInstance().getReference("bookings/" + currentBooking.getKey());
        bookingData.put("caravanBedrooms", bedrooms[_selectedBedroom]);

        bookingData.put("caravanId", _selectedCaravan);
        if(_selectedCaravan == 1) {
            bookingData.put("caravanName", "Carnaby");
        } else {
            bookingData.put("caravanName", "Delta");
        }
        bookingData.put("extras", extras[_selectedExtra]);

        // Set Date
        Calendar checkInCalendar = null;
        String checkInDate = "";
        checkInCalendar = selectedDates.get(0);
        checkInDate = checkInCalendar.get(Calendar.YEAR) + "-" + (checkInCalendar.get(Calendar.MONTH) + 1) + "-" + checkInCalendar.get(Calendar.DAY_OF_MONTH);
        bookingData.put("caravanCheckIn", checkInDate);
        bookingData.put("caravanCheckOut", selectedDates.size());

        _mDB.updateChildren(bookingData)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        hideProgressDialog();
                        if(task.isSuccessful()) {
                            showAlert("Congratulations! Your booking has been successfully updated.", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Error! Please fill out required details for making a booking", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getBookingDateForSelectedBedroom() {
        _mDB = FirebaseDatabase.getInstance().getReference();
        _mDB.child("bookings").orderByChild("caravanId").equalTo(_selectedCaravan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    disabledDates.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        FBbooking bookingData = snapshot.getValue(FBbooking.class);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        Calendar disabledDateCalendar = Calendar.getInstance();
                        int disabledRange = bookingData.getCaravanCheckOut();
                        try {
                            disabledDateCalendar.setTime(format.parse(bookingData.getCaravanCheckIn()));
                            HashMap<String, Object> tmpData = new HashMap<>();
                            tmpData.put("start", disabledDateCalendar);
                            tmpData.put("range", disabledRange);
                            disabledDates.add(tmpData);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    setDisabledDate(disabledDates);
                } else {
                    List<Calendar> disabledCalendars = new ArrayList<>();
                    calendarView.setDisabledDays(disabledCalendars);
                    calendarView.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Canceled", "123123");
            }
        });
    }

    public void setDisabledDate(ArrayList<HashMap<String, Object>> disabledDates) {
        List<Calendar> disabledCalendars = new ArrayList<>();
        for(HashMap<String, Object> disabledDate: disabledDates) {
            Calendar startCalendar = (Calendar) disabledDate.get("start");
            disabledCalendars.add(startCalendar);
            int range = (int) disabledDate.get("range");
            for(int i = 1; i < range; i++) {
                Calendar nextCalendar = Calendar.getInstance();
                nextCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR));
                nextCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH));
                nextCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.get(Calendar.DAY_OF_MONTH));
                nextCalendar.add(Calendar.DATE, i);
                disabledCalendars.add(nextCalendar);
            }
        }
        calendarView.setDisabledDays(disabledCalendars);
        calendarView.invalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public int getAmount() {
        int result = 70;
        int length = selectedDates.size();
        if(length > 0) {
            int extras = (length - 1) * 30;
            result += extras;
        }
        return result;
    }
}


