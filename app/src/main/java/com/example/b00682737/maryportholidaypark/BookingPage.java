package com.example.b00682737.maryportholidaypark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingPage extends AppCompatActivity {

    private static final String TAG = "BookingPage";
    EditText enterCheckIn, enterCheckOut;
    Button createBooking2;
    Spinner spinnerSpin, spinnerSpin2;
    DatabaseReference databaseCaravan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

        databaseCaravan = FirebaseDatabase.getInstance().getReference("bookings");
        //details will fall under 'bookings' heading in database

        enterCheckIn = (EditText) findViewById(R.id.enterCheckIn);
        enterCheckOut = (EditText) findViewById(R.id.enterCheckOut);
        createBooking2 = (Button) findViewById(R.id.createBooking2);
        spinnerSpin = (Spinner) findViewById(R.id.spinnerSpin);
        spinnerSpin2 = (Spinner) findViewById(R.id.spinnerSpin2);

        createBooking2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBooking(); //to crate a new booking in database

            }
        });
    }


        private void addBooking(){
            String checkingin = enterCheckIn.getText().toString().trim();
            String checkingout = enterCheckOut.getText().toString().trim();
            String caravan = spinnerSpin.getSelectedItem().toString();
            String extras = spinnerSpin2.getSelectedItem().toString();
            //will store the following data to the database


            if (!TextUtils.isEmpty(checkingin)) {
                String id = databaseCaravan.push().getKey(); //to create a unique string inside bookings

               Caravan caravan1 = new Caravan(id, checkingin, extras, caravan, checkingout);
               databaseCaravan.child(id).setValue(caravan1);

               Toast.makeText(this, "Congratulations! Your booking has been successfully added", Toast.LENGTH_LONG).show();
               finish();
               startActivity(new Intent(BookingPage.this, ProfilePage.class));
               //will bring user back to their profile page if booking is successful


            } else {
               Toast.makeText(this, "Error! Please fill out required details for making a booking", Toast.LENGTH_LONG).show();
               //will keep user on booking page if booking has been unsuccessful

                }
            }
        }



