package com.example.b00682737.maryportholidaypark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.b00682737.maryportholidaypark.FirebaseInfo;
import com.example.b00682737.maryportholidaypark.Models.FBbooking;
import com.example.b00682737.maryportholidaypark.Models.UserInfo;
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
import java.util.Locale;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    UserInfo userInfo;
    private EditText userName, userEmail, userPhone, userAddress;

    FBbooking currentBooking;
    ArrayList<FBbooking> pastBookings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        userName = (EditText) findViewById(R.id.etUsername);
        userEmail = (EditText) findViewById(R.id.etUserEmail);
        userPhone = (EditText) findViewById(R.id.etUserPhone);
        userAddress = (EditText) findViewById(R.id.etUserAddr);

        // Check User Information
        mAuth = FirebaseAuth.getInstance();

        userInfo = appSettings.getUser();
        if (TextUtils.isEmpty(userInfo.name) || TextUtils.isEmpty(userInfo.email) || mAuth.getCurrentUser() == null) {
            finish();
            return;
        }

        userName.setText(userInfo.name);
        userName.setKeyListener(null);

        userEmail.setText(userInfo.email);
        userEmail.setKeyListener(null);

        userPhone.setText(userInfo.phone);
        userAddress.setText(userInfo.address);

        // Profile Update Button
        findViewById(R.id.btnUpdate).setOnClickListener(this);

        // View Booking Button
        findViewById(R.id.btnViewCurrentBookings).setOnClickListener(this);
        findViewById(R.id.btnViewPastBookings).setOnClickListener(this);

        // Get Current Booking Information
        getUserBookingInformation();
    }

    private void getUserBookingInformation() {
        // Get Booking Information
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseInfo.BOOKINGS);

        showProgressDialog();
        mPostReference.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                hideProgressDialog();

                for (DataSnapshot infoSnip : dataSnapshot.getChildren()) {
                    if (infoSnip.exists()) {
                        FBbooking bookingData = infoSnip.getValue(FBbooking.class);
                        bookingData.setKey(infoSnip.getKey());

                        boolean isExpired = checkIfExpiredBooking(bookingData.getCaravanCheckIn(), bookingData.getCaravanCheckOut());
                        if(isExpired) {
                            pastBookings.add(bookingData);
                        } else {
                            currentBooking = bookingData;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
                if (databaseError != null) {
                    showToastMessage(databaseError.getMessage());
                    //this will display error message if booking doesn't exist
                }
            }
        });
    }

    public boolean checkIfExpiredBooking(String start, int expire) {
        boolean expired = false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar startCalendar = Calendar.getInstance();
        try {
            startCalendar.setTime(format.parse(start));
            startCalendar.add(Calendar.DATE, expire);
            if(System.currentTimeMillis() > startCalendar.getTimeInMillis()) {
                expired = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return expired;
    } //To allow user to make new booking after expired booking

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnUpdate) {
            updateUserInfo();
        } else if (viewId == R.id.btnViewCurrentBookings) {
            if (currentBooking == null) {
                showAlert("You have no available booking data!");
            } else {
                Intent intent = new Intent(mContext, BookingDetailsActivity.class);
                intent.putExtra("booking_info", currentBooking);
                startActivity(intent);
            }
        } else if (viewId == R.id.btnViewPastBookings) {
            if (pastBookings == null || pastBookings.isEmpty()) {
                showAlert("You have no available booking data!");
            } else {
                Intent intent = new Intent(mContext, BookingListActivity.class);
                intent.putExtra("booking_list", pastBookings);
                startActivity(intent);
            }
        }
    }

    private void updateUserInfo() //to edit and update details
     {
        final String phone = userPhone.getText().toString().trim();
        final String address = userAddress.getText().toString().trim();

        if (phone.isEmpty() || address.isEmpty()) {
            showToastMessage("Please enter all details");
            return;
        }

        showProgressDialog();

        userInfo.phone = phone;
        userInfo.address = address;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(FirebaseInfo.USERS).child(mAuth.getCurrentUser().getUid()).setValue(userInfo, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                hideProgressDialog();

                if (databaseError == null) {
                    appSettings.saveUser(userInfo);
                    showAlert(R.string.success_update_profile, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } else {
                    showToastMessage(databaseError.getMessage());
                }
            }
        });
    }
}
