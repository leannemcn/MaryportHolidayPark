package com.example.b00682737.maryportholidaypark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.example.b00682737.maryportholidaypark.Adapters.ImageAdapter;
import com.example.b00682737.maryportholidaypark.Models.FBbooking;
import com.example.b00682737.maryportholidaypark.RentalCaravans;
import com.example.b00682737.maryportholidaypark.design.PageNavigator;
import com.example.b00682737.maryportholidaypark.R;

public class BookingDetailsActivity extends BaseActivity {
    // Booking Information
    FBbooking currentBooking;

    // Image Fragment
    private FragmentStatePagerAdapter adapter;
    private ViewPager viewPager;
    PageNavigator navigator;

    TextView tvCaravanName;
    TextView tvDate;
    TextView tvDays;
    TextView tvPrice;
    TextView tvBeds;
    TextView tvFuniture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingdetails);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        Intent intent = getIntent();
        currentBooking = intent.getParcelableExtra("booking_info");
        if (currentBooking == null) {
            finish();
            return;
        }

        // Show Information
        tvCaravanName = findViewById(R.id.tvCaravanName);
        tvDate = findViewById(R.id.tvDate);
        tvDays = findViewById(R.id.tvDays);
        tvPrice = findViewById(R.id.tvPrice);
        tvBeds = findViewById(R.id.tvBeds);
        tvFuniture = findViewById(R.id.tvFuniture);

        tvCaravanName.setText(currentBooking.getCaravanName());
        tvDate.setText(currentBooking.getCaravanCheckIn());
        tvDays.setText(String.format("%d Day(s)", currentBooking.getCaravanCheckOut()));
        tvPrice.setText(String.valueOf(getAmount(currentBooking.getCaravanCheckOut())));
        tvBeds.setText(currentBooking.getCaravanBedrooms());
        tvFuniture.setText(currentBooking.getExtras());

        // Caravan Images
        int[] imageResources;
        if (currentBooking.getCaravanId() == 1) {
            imageResources = RentalCaravans.carnabyImages;
        } else {
            imageResources = RentalCaravans.deltaImages;
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        navigator = (PageNavigator) findViewById(R.id.navigator);
        navigator.setSize(imageResources.length);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                navigator.setPosition(position);
                navigator.invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // init viewpager adapter and attach
        adapter = new ImageAdapter(getSupportFragmentManager(), imageResources);
        viewPager.setAdapter(adapter);
    }

    public int getAmount(int days) {
        int result = 70;
        int length = days;
        if(length > 0) {
            int extras = (length - 1) * 30;
            result += extras;
        }
        return result;
    }
}


