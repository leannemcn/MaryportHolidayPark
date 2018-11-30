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
    EditText enterCheckIn, enterCheckOut;
    Button createBooking2;
    Spinner spinnerSpin;

    DatabaseReference databaseCaravan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

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


