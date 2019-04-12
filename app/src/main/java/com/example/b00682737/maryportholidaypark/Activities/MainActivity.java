package com.example.b00682737.maryportholidaypark.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.b00682737.maryportholidaypark.Fragments.ParkFragment;
import com.example.b00682737.maryportholidaypark.Fragments.ProfileFragment;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressBar pbBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pbBooking = findViewById(R.id.pb_booking);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_main_content, new ParkFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar will handle clicks on the Home/Up button
        int id = item.getItemId();

        if (id == R.id.create_booking) {
            checkCurrentBooking();
            return true;
        } else if(id == R.id.edit_booking) {
            checkIfBookingExists();
            return true;
        } else if(id == R.id.cancel_booking) {
            cancelBookingIfExists();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_main) {
            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            ft.replace(R.id.layout_main_content, new ParkFragment());
            ft.commit();
        } else if (id == R.id.nav_profile) {
            // Handle the camera action
            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            ft.replace(R.id.layout_main_content, new ProfileFragment());
            ft.commit();
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder altBuilder = new AlertDialog.Builder(this);
            altBuilder.setTitle("Log Out");
            altBuilder.setMessage("You want to log out?");
            altBuilder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utils utils = new Utils();
                    utils.logOut();
                    Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(logoutIntent);
                    finish();
                }
            });

            altBuilder.setNegativeButton("Cancel", null);
            AlertDialog altDialog = altBuilder.create();
            altDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkIfBookingExists() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference _mDB = FirebaseDatabase.getInstance().getReference("bookings/" + uid);
        _mDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    FBbooking bookingData = dataSnapshot.getValue(FBbooking.class);
                    Intent editBookingIntent = new Intent(MainActivity.this, Booking.class);
                    editBookingIntent.putExtra("type", 1);
                    editBookingIntent.putExtra("caravanId", bookingData.getCaravanId());
                    startActivity(editBookingIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Booking Data does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cancelBookingIfExists() {
        if(pbBooking.getVisibility() == View.GONE) {
            pbBooking.setVisibility(View.VISIBLE);
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference _mDB = FirebaseDatabase.getInstance().getReference("bookings/" + uid);
        _mDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    FBbooking bookingData = dataSnapshot.getValue(FBbooking.class);
                    boolean isExpired = checkIfExpiredBooking(bookingData.getCaravanCheckIn(), bookingData.getCaravanCheckOut());
                    if(isExpired == false) {
                        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
                        RequestParams params = new RequestParams();
                        params.put("transactionId", bookingData.getTransactionId());
                        client.post(RentalCaravans.SERVERURL + "/refund", params, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.d("Debug", "456");
                                if(pbBooking.getVisibility() == View.VISIBLE) {
                                    pbBooking.setVisibility(View.GONE);
                                }
                                Toast.makeText(MainActivity.this, "Canceled! booking has failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                if(pbBooking.getVisibility() == View.VISIBLE) {
                                    pbBooking.setVisibility(View.GONE);
                                }
                                deleteBooking(uid);
                            }
                        });
                    } else {
                        if(pbBooking.getVisibility() == View.VISIBLE) {
                            pbBooking.setVisibility(View.GONE);
                        }
                        Toast.makeText(MainActivity.this, "Booking has already expired.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(pbBooking.getVisibility() == View.VISIBLE) {
                        pbBooking.setVisibility(View.GONE);
                    }
                    Toast.makeText(MainActivity.this, "Booking Data does not exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
    }

    public void deleteBooking(String uid) {
        DatabaseReference _mDB = FirebaseDatabase.getInstance().getReference("bookings/" + uid);
        _mDB.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(MainActivity.this, "Booking is canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkCurrentBooking() {
        if(pbBooking.getVisibility() == View.GONE) {
            pbBooking.setVisibility(View.VISIBLE);
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference _mDB = FirebaseDatabase.getInstance().getReference("bookings/" + uid);
        _mDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    FBbooking bookingData = dataSnapshot.getValue(FBbooking.class);
                    boolean isExpired = checkIfExpiredBooking(bookingData.getCaravanCheckIn(), bookingData.getCaravanCheckOut());
                    if(isExpired == true) {
                        if(pbBooking.getVisibility() == View.VISIBLE) {
                            pbBooking.setVisibility(View.GONE);
                        }
                        Intent bookingIntent = new Intent(MainActivity.this, Booking.class);
                        bookingIntent.putExtra("type", 0);
                        bookingIntent.putExtra("caravanId", 0);
                        startActivity(bookingIntent);
                    } else {
                        if(pbBooking.getVisibility() == View.VISIBLE) {
                            pbBooking.setVisibility(View.GONE);
                        }
                        Toast.makeText(MainActivity.this, "Booking is not expired yet.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(pbBooking.getVisibility() == View.VISIBLE) {
                        pbBooking.setVisibility(View.GONE);
                    }
                    Intent bookingIntent = new Intent(MainActivity.this, Booking.class);
                    bookingIntent.putExtra("type", 0);
                    bookingIntent.putExtra("caravanId", 0);
                    startActivity(bookingIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
