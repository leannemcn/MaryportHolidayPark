package com.example.b00682737.maryportholidaypark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.b00682737.maryportholidaypark.Adapters.BookingListAdapter;
import com.example.b00682737.maryportholidaypark.Models.FBbooking;

import java.util.ArrayList;

public class BookingListActivity extends BaseActivity {
    ListView lvData;
    ArrayList<FBbooking> bookingList;
    BookingListAdapter bookingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinglist);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // Check Data
        Intent intent = getIntent();
        bookingList = intent.getParcelableArrayListExtra("booking_list");

        if (bookingList == null || bookingList.isEmpty()) {
            finish();
            return;
        }

        lvData = findViewById(R.id.lvData);
        bookingListAdapter = new BookingListAdapter(mContext, bookingList);
        lvData.setAdapter(bookingListAdapter);
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FBbooking eventInfo = bookingList.get(position);
                Intent intent = new Intent(mContext, BookingDetailsActivity.class);
                intent.putExtra("booking_info", eventInfo);
                startActivity(intent);
            }
        });
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
}

