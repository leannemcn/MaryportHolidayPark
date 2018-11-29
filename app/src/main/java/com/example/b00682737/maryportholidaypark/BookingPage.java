package com.example.b00682737.maryportholidaypark;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

public class BookingPage extends AppCompatActivity {

    private static final String TAG = "BookingPage";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener, mDateSetListener2;
    private TextView mDisplayDate2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);
        mDisplayDate = (TextView) findViewById(R.id.checkIn);
        mDisplayDate2 = (TextView) findViewById(R.id.checkOut);

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

    }


}
