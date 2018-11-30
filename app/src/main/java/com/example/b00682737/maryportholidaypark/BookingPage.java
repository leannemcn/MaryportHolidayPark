package com.example.b00682737.maryportholidaypark;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class BookingPage extends AppCompatActivity {

    private static final String TAG = "BookingPage";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener, mDateSetListener2;
    private TextView mDisplayDate2;

    EditText editTextName;
    Button createBookingg;
    Spinner spinnerSpin;

    DatabaseReference databaseCaravan;

    //testing below
   // private FirebaseAuth firebaseAuth3;
  //  private DatabaseReference databaseReference;
   // private EditText manyBeds;
   // private Button createBooking;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);
        mDisplayDate = (TextView) findViewById(R.id.checkIn);
        mDisplayDate2 = (TextView) findViewById(R.id.checkOut);

        databaseCaravan = FirebaseDatabase.getInstance().getReference("bookings");

        editTextName = (EditText) findViewById(R.id.editTextName);
        createBookingg = (Button) findViewById(R.id.createBookingg);
        spinnerSpin = (Spinner) findViewById(R.id.spinnerSpin);

        createBookingg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBedroom();

            }
        });
      //  firebaseAuth3 = FirebaseAuth.getInstance();
      // databaseReference = FirebaseDatabase.getInstance().getReference();
      //  manyBeds = (EditText)findViewById(R.id.manyBeds);
      //  createBooking = (Button)findViewById(R.id.createBooking);

        mDisplayDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BookingPage.this,
                        android.R.style.Theme_Light, mDateSetListener, year, day, month);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDisplayDate2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal2 = Calendar.getInstance();
                int year = cal2.get(Calendar.YEAR);
                int day = cal2.get(Calendar.DAY_OF_MONTH);
                int month = cal2.get(Calendar.MONTH);

                DatePickerDialog dialog2 = new DatePickerDialog(
                        BookingPage.this,
                        android.R.style.Theme_Light, mDateSetListener2, year, day, month);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1; //this makes january appear as month 1 instead of 0
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year );
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }

        };

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1; //this makes january appear as month 1 instead of 0
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year );
                String date = month + "/" + day + "/" + year;
                mDisplayDate2.setText(date);
            }

        };
      //  private void saveUserInformation(){
          //  String manyBeds = manyBeds.getText().toString().trim();

          //  UserInformation userInformation = new UserInformation(manyBeds);
           // FirebaseUser user = firebaseAuth3.getCurrentUser();
           // databaseReference.child(user.getUid()).setValue(userInformation);
           // Toast.makeText(this, "information saved..", Toast.LENGTH_LONG).show();

    }
   // if(view == createBooking){
        //    saveUserInformation();
  //  }

    private void addBedroom(){
        String name = editTextName.getText().toString().trim();
        String caravan = spinnerSpin.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name)){
           String id = databaseCaravan.push().getKey(); //to create a unique string inside bookings

            Caravan caravan1 = new Caravan(id, name, caravan);
            databaseCaravan.child(id).setValue(caravan1);

            Toast.makeText(this, "Booking added", Toast.LENGTH_LONG).show();


        }else{
            Toast.makeText(this, "you should enter a name", Toast.LENGTH_LONG).show();
        }
    }

    }


//}
