package com.example.b00682737.maryportholidaypark.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.example.b00682737.maryportholidaypark.RentalCaravans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Booking extends BaseActivity{
    private static final int REQUEST_CODE = 1001;

    private DatabaseReference _mDB;
    private int _type;

    private String[] bedrooms;
    private String[] extras;

    private ImageView imgCarnaby;
    private ImageView imgDelta;
    private RadioGroup radioCaravan;
    private CalendarView calendarView;
    private Spinner bedroomSpinner;
    private Spinner extraSpinner;
    private ProgressBar pbCreateBooking;

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
        _type = intent.getIntExtra("type", 0);

        bedrooms = RentalCaravans.bedrooms;
        extras = RentalCaravans.extraOptions;
        disabledDates = new ArrayList<>();
        selectedDates = new ArrayList<>();

        pbCreateBooking = findViewById(R.id.pb_create_booking);

        imgCarnaby = findViewById(R.id.img_booking_carnaby);
        imgCarnaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent carnabyListIntent = new Intent(Booking.this, CarnabyList.class);
                startActivity(carnabyListIntent);
            }
        });
        imgDelta = findViewById(R.id.img_booking_delta);
        imgDelta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent DeltaListIntent = new Intent(Booking.this, DeltaListActivity.class);
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
        extraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selectedExtra = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        initBedroomSpinner();
        initExtraSpinner();

        Button btnBooking = findViewById(R.id.btn_createbooking);
        if(_type == 0) {
            btnBooking.setText(R.string.create_booking);
        } else {
            btnBooking.setText(R.string.edit_booking);
        }
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_type == 0) {
                    selectedDates = calendarView.getSelectedDates();
                    if(selectedDates.size() == 0) {
                        if(pbCreateBooking.getVisibility() == View.VISIBLE) {
                            pbCreateBooking.setVisibility(View.GONE);
                        }
                        Toast.makeText(Booking.this, "Please select booking dates!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int amount = getAmount();

                    AlertDialog.Builder altBuilder = new AlertDialog.Builder(Booking.this);
                    altBuilder.setTitle("Booking");
                    altBuilder.setMessage("Pay for booking(£" + Integer.toString(amount) + ")");
                    altBuilder.setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            payForBooking();
                        }
                    });
                    altBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    AlertDialog altDialog = altBuilder.create();
                    altDialog.show();
                } else {
                    createBooking("");
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        if(_type == 1) {
            calendarView.setVisibility(View.GONE);
        }
        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
        getBookingDateForSelectedBedroom();
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

    public void payForBooking() {
        AsyncHttpClient client = new AsyncHttpClient(true,80,443);;
        client.get(RentalCaravans.SERVERURL + "/client_token", new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, String clientToken) {
                DropInRequest dropInRequest = new DropInRequest()
                        .clientToken(clientToken);
                startActivityForResult(dropInRequest.getIntent(Booking.this), REQUEST_CODE);
            }

            @Override
            public void onFailure(int statusCode, PreferenceActivity.Header[] headers, String responseString, Throwable throwable) {
                Log.d("Debug", "123");
            }
        });
    }

    public void createBooking(String transactionId) {
        Calendar checkInCalendar = null;
        String checkInDate = "";
        HashMap<String, Object> bookingData = new HashMap<>();
        if(_type == 0) {
            checkInCalendar = selectedDates.get(0);
            checkInDate = checkInCalendar.get(Calendar.YEAR) + "-" + (checkInCalendar.get(Calendar.MONTH) + 1) + "-" + checkInCalendar.get(Calendar.DAY_OF_MONTH);
            bookingData.put("transactionId", transactionId);
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        _mDB = FirebaseDatabase.getInstance().getReference("bookings/" + uid);
        bookingData.put("caravanBedrooms", bedrooms[_selectedBedroom]);
        if(_type == 0) {
            bookingData.put("caravanCheckIn", checkInDate);
            bookingData.put("caravanCheckOut", selectedDates.size());
        }

        bookingData.put("caravanId", _selectedCaravan);
        if(_selectedCaravan == 1) {
            bookingData.put("caravanName", "Carnaby");
        } else {
            bookingData.put("caravanName", "Delta");
        }
        bookingData.put("extras", extras[_selectedExtra]);

        _mDB.updateChildren(bookingData)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            if(pbCreateBooking.getVisibility() == View.VISIBLE) {
                                pbCreateBooking.setVisibility(View.GONE);
                            }
                            Toast.makeText(getApplicationContext(), "Congratulations! Your booking has been successfully added", Toast.LENGTH_SHORT).show();
                        } else {
                            if(pbCreateBooking.getVisibility() == View.VISIBLE) {
                                pbCreateBooking.setVisibility(View.GONE);
                            }
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
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
                RequestParams params = new RequestParams();
                params.put("payment_method_nonce", result.getPaymentMethodNonce().getNonce());
                int amount = getAmount();
                params.put("amount", amount);
                client.post(RentalCaravans.SERVERURL + "/checkout", params, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.d("Debug", "345");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                if(pbCreateBooking.getVisibility() == View.GONE) {
                                    pbCreateBooking.setVisibility(View.VISIBLE);
                                }
                                createBooking(responseString);
                            }
                        }
                );
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
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
