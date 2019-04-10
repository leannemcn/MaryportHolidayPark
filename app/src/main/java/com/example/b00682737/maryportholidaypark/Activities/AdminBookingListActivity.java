package com.example.b00682737.maryportholidaypark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.b00682737.maryportholidaypark.Adapters.BookingListAdapter;
import com.example.b00682737.maryportholidaypark.FirebaseInfo;
import com.example.b00682737.maryportholidaypark.Models.FBbooking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminBookingListActivity extends BaseActivity {
    ListView lvData;
    ArrayList<FBbooking> bookingList = new ArrayList<>();
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
                //This stores data into "Booking_info" in Firebase
            }
        });

        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseInfo.BOOKINGS);

        showProgressDialog();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();

                // Read Data
                bookingList.clear();
                for (DataSnapshot infoSnip : dataSnapshot.getChildren()) {
                    if (infoSnip.exists()) {
                        FBbooking request = infoSnip.getValue(FBbooking.class);
                        String key = infoSnip.getKey();
                        //This will get data from Firebase

                        if (request != null) {
                            request.setKey(key);
                            bookingList.add(request);
                        }
                    }
                }

                bookingListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                hideProgressDialog();
            }
        };

        mPostReference.addValueEventListener(postListener);
    }
}
