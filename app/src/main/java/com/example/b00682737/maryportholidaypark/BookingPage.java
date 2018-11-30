package com.example.b00682737.maryportholidaypark;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingPage extends AppCompatActivity {

    private static final String TAG = "BookingPage";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener, mDateSetListener2;
    private TextView mDisplayDate2;

    EditText enterCheckIn, enterCheckOut, editTextName;
    Button createBooking2;
    Spinner spinnerSpin;

    DatabaseReference databaseCaravan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);
        //   mDisplayDate = (TextView) findViewById(R.id.checkIn);
        //mDisplayDate2 = (TextView) findViewById(R.id.checkOut);

        databaseCaravan = FirebaseDatabase.getInstance().getReference("bookings");

        enterCheckIn = (EditText) findViewById(R.id.enterCheckIn);
        enterCheckOut = (EditText) findViewById(R.id.enterCheckOut);
        createBooking2 = (Button) findViewById(R.id.createBooking2);
        spinnerSpin = (Spinner) findViewById(R.id.spinnerSpin);

        createBooking2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBooking();

            }
        });
    }

        //  mDisplayDate.setOnClickListener(new View.OnClickListener(){
        //    @Override
        //   public void onClick(View view){
        //    Calendar cal = Calendar.getInstance();
        //   int year = cal.get(Calendar.YEAR);
        //  int day = cal.get(Calendar.DAY_OF_MONTH);
        //  int month = cal.get(Calendar.MONTH);

        // DatePickerDialog dialog = new DatePickerDialog(
        //     BookingPage.this,
        //    //     android.R.style.Theme_Light, mDateSetListener, year, day, month);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.show();
        // }
        //  });

        //  mDisplayDate2.setOnClickListener(new View.OnClickListener(){
        //   @Override
        //  public void onClick(View view){
        //   Calendar cal2 = Calendar.getInstance();
        //  int year = cal2.get(Calendar.YEAR);
        //  int day = cal2.get(Calendar.DAY_OF_MONTH);
        // int month = cal2.get(Calendar.MONTH);

        //  DatePickerDialog dialog2 = new DatePickerDialog(
        //       BookingPage.this,
        //       android.R.style.Theme_Light, mDateSetListener2, year, day, month);
        // dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog2.show();
        //  }
        //  });

        //mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        // @Override
        // public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //   month = month + 1; //this makes january appear as month 1 instead of 0
        //  Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year );
        // String date = month + "/" + day + "/" + year;
        // mDisplayDate.setText(date);
        //    }

        //   };

        //  mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
        //  @Override
        // public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //    month = month + 1; //this makes january appear as month 1 instead of 0
        //    Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year );
        //   String date = month + "/" + day + "/" + year;
        //   mDisplayDate2.setText(date);
        // }

        //  };

        // }

        private void addBooking(){
            String checkingin = enterCheckIn.getText().toString().trim();
            String checkingout = enterCheckOut.getText().toString().trim();
            String caravan = spinnerSpin.getSelectedItem().toString();


            if (!TextUtils.isEmpty(checkingin)) {
                String id = databaseCaravan.push().getKey(); //to create a unique string inside bookings

                Caravan caravan1 = new Caravan(id, checkingin, caravan,checkingout);
                databaseCaravan.child(id).setValue(caravan1);

                Toast.makeText(this, "Your booking has been successfully added", Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(this, "Please fill out required details for making a booking", Toast.LENGTH_LONG).show();
            }
        }
    }


