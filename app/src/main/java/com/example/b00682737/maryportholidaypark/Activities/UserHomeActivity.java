package com.example.b00682737.maryportholidaypark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.b00682737.maryportholidaypark.R;

import com.example.b00682737.maryportholidaypark.FirebaseInfo;
import com.example.b00682737.maryportholidaypark.Models.FBbooking;
import com.example.b00682737.maryportholidaypark.RentalCaravans;
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
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class UserHomeActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        // Navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        int menuItemSize = navigationView.getMenu().size();
        for (int i = 0; i < menuItemSize; i++) {
            navigationView.getMenu().getItem(i).setCheckable(false);
        }
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView tvUserName = headerView.findViewById(R.id.tvUserName);
        TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        tvUserName.setText(appSettings.getUser().name);
        tvUserEmail.setText(appSettings.getUser().email);

        findViewById(R.id.btnNewBooking).setOnClickListener(this);
        findViewById(R.id.btnEditBooking).setOnClickListener(this);
        findViewById(R.id.btnContactUs).setOnClickListener(this);
        findViewById(R.id.btnAboutMe).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnNewBooking) {
            makeNewBooking();
        } else if (viewId == R.id.btnEditBooking) {
            editBooking();
        } else if (viewId == R.id.btnContactUs) {
            startActivity(new Intent(mContext, ContactUsActivity.class));
        } else if (viewId == R.id.btnAboutMe) {
            startActivity(new Intent(mContext, ProfileActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_newbooking) {
            makeNewBooking();
        } else if (id == R.id.nav_editbooking) {
            editBooking();
        } else if (id == R.id.nav_contactus) {
            startActivity(new Intent(mContext, ContactUsActivity.class));
        } else if (id == R.id.nav_aboutme) {
            startActivity(new Intent(mContext, ProfileActivity.class));
        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_logout) {
            Signout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    }

    public void deleteBooking(String key) {
        DatabaseReference _mDB = FirebaseDatabase.getInstance().getReference("bookings/" + key);
        _mDB.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                showToastMessage("Booking is canceled");
            }
        });
    }

    private void makeNewBooking() {
        showProgressDialog();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseInfo.BOOKINGS);

        showProgressDialog();
        mPostReference.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                hideProgressDialog();

                boolean hasPendingBook = false;
                for (DataSnapshot infoSnip : dataSnapshot.getChildren()) {
                    if (infoSnip.exists()) {
                        FBbooking bookingData = infoSnip.getValue(FBbooking.class);
                        bookingData.setKey(infoSnip.getKey());

                        boolean isExpired = checkIfExpiredBooking(bookingData.getCaravanCheckIn(), bookingData.getCaravanCheckOut());
                        if(isExpired != true) {
                            hasPendingBook = true;
                            break;
                        }
                    }
                }

                // Check Pending Booking
                if (hasPendingBook) {
                    showToastMessage("Booking is not expired yet.");
                } else {
                    Intent bookingIntent = new Intent(mContext, NewBookingActivity.class);

                    bookingIntent.putExtra("caravanId", 0);
                    startActivity(bookingIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
                if (databaseError != null) {
                    showToastMessage(databaseError.getMessage());
                }
            }
        });

    }

    private void editBooking() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseInfo.BOOKINGS);

        showProgressDialog();
        mPostReference.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                hideProgressDialog();

                FBbooking currentBookingData = null;
                for (DataSnapshot infoSnip : dataSnapshot.getChildren()) {
                    if (infoSnip.exists()) {
                        FBbooking bookingData = infoSnip.getValue(FBbooking.class);
                        bookingData.setKey(infoSnip.getKey());
                        boolean isExpired = checkIfExpiredBooking(bookingData.getCaravanCheckIn(), bookingData.getCaravanCheckOut());
                        if(isExpired != true) {
                            currentBookingData = bookingData;
                            break;
                        }
                    }
                }

                if (currentBookingData != null) {
                    Intent editBookingIntent = new Intent(mContext, EditBookingActivity.class);

                    editBookingIntent.putExtra("caravanId", currentBookingData.getCaravanId());
                    editBookingIntent.putExtra("key", currentBookingData.getKey());
                    editBookingIntent.putExtra("booking", currentBookingData);
                    startActivity(editBookingIntent);
                } else {
                    showToastMessage("You have no available booking data!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
                if (databaseError != null) {
                    showToastMessage(databaseError.getMessage());
                }
            }
        });

    }

    public void cancelBookingIfExists() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseInfo.BOOKINGS);

        showProgressDialog();
        mPostReference.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                FBbooking currentBookingData = null;
                for (DataSnapshot infoSnip : dataSnapshot.getChildren()) {
                    if (infoSnip.exists()) {
                        FBbooking bookingData = infoSnip.getValue(FBbooking.class);
                        bookingData.setKey(infoSnip.getKey());

                        boolean isExpired = checkIfExpiredBooking(bookingData.getCaravanCheckIn(), bookingData.getCaravanCheckOut());
                        if(isExpired != true) {
                            currentBookingData = bookingData;
                            break;
                        }
                    }
                }

                if (currentBookingData != null) {

                    // User have current booking and cancel it
                    AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
                    RequestParams params = new RequestParams();
                    params.put("transactionId", currentBookingData.getTransactionId());

                    final FBbooking finalCurrentBookingData = currentBookingData;

                    client.post(RentalCaravans.SERVERURL + "/refund", params, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("Debug", "456");
                            hideProgressDialog();
                            showToastMessage("Cancel booking failed");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            hideProgressDialog();
                            deleteBooking(finalCurrentBookingData.getKey());
                        }
                    });
                } else if (dataSnapshot.getChildrenCount() > 0) {

                    // All bookings was already expired
                    hideProgressDialog();
                    showToastMessage("Booking is already expired.");
                } else {

                    // user doesn't have any booking
                    hideProgressDialog();
                    showToastMessage("You have no available booking data!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
                if (databaseError != null) {
                    showToastMessage(databaseError.getMessage());
                }
            }
        });

    }

    // Back Button Action
    boolean isFinish = false;
    class FinishTimer extends CountDownTimer {
        public FinishTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            isFinish = true;
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            isFinish = false;
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!isFinish) {

                showToastMessage(R.string.finish_message);
                FinishTimer timer = new FinishTimer(2000, 1);
                timer.start();
            } else {
                finish();
            }
        }
    }
}

