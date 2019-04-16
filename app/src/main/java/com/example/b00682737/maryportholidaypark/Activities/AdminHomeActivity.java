package com.example.b00682737.maryportholidaypark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.b00682737.maryportholidaypark.R;

public class AdminHomeActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);

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

        findViewById(R.id.btnAllBooking).setOnClickListener(this);
        findViewById(R.id.btnAllMessages).setOnClickListener(this);
        findViewById(R.id.btnHistogram).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnAllBooking) {
            showAllBookings();
        } else if (viewId == R.id.btnAllMessages) {
            showAllMessages();
        }else if (viewId == R.id.btnHistogram) {
            showHistogram();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_allbooking) {
            showAllBookings();
        } else if (id == R.id.nav_allmessages) {
            showAllMessages();
        }else if (id == R.id.nav_histogram) {
            showHistogram();
        }
        else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_logout) {
            Signout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAllBookings() {
        startActivity(new Intent(mContext, AdminBookingListActivity.class));
    }

    private void showAllMessages() {
        startActivity(new Intent(mContext, AdminMessageListActivity.class));
    }

    private void showHistogram() {
        startActivity(new Intent(mContext, AdminHistogramActivity.class));
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
