package com.example.b00682737.maryportholidaypark.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.example.b00682737.maryportholidaypark.FirebaseInfo;
import com.example.b00682737.maryportholidaypark.Models.FBbooking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHistogramActivity extends BaseActivity{

    ArrayList<FBbooking> bookingList = new ArrayList<>();
    private TextView level;
    private int jan=0,feb=0,mar=0,apr=0,may=0,jun=0,jul=0,aug=0,sep=0,oct=0,nov=0,dec=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        level=findViewById(R.id.textView2);

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

                        if (request != null) {
                            request.setKey(key);
                            bookingList.add(request);
                        }
                    }
                }
                //updateBarChart
                level.setText("Total Booking : "+bookingList.size());
                if(bookingList.size()>0)
                    calculateDate();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
        };

        mPostReference.addValueEventListener(postListener);

    }

    void calculateDate()
    {
        for (int i=0;i<bookingList.size();i++)
        {
            if(bookingList.get(i).getCaravanCheckIn().contains("-1-") ||
                    bookingList.get(i).getCaravanCheckIn().contains("-01-"))jan++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-2-") ||
                    bookingList.get(i).getCaravanCheckIn().contains("-02-"))feb++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-3-") ||
                    bookingList.get(i).getCaravanCheckIn().contains("-03-"))mar++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-4-") ||
                    bookingList.get(i).getCaravanCheckIn().contains("-04-"))apr++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-5-") ||
                    bookingList.get(i).getCaravanCheckIn().contains("-05-"))may++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-6-") ||
                    bookingList.get(i).getCaravanCheckIn().contains("-06-"))jun++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-7-") ||
                    bookingList.get(i).getCaravanCheckIn().contains("-07-"))jul++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-8-") ||
                    bookingList.get(i).getCaravanCheckIn().contains("-08-"))aug++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-9-") ||
                    bookingList.get(i).getCaravanCheckIn().contains("-09-"))sep++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-10-"))oct++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-11-"))nov++;
            else if(bookingList.get(i).getCaravanCheckIn().contains("-12-"))dec++;

        }

        DrawChart();

    }


    void DrawChart()
    {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        BarChart mChart = (BarChart) findViewById(R.id.chart);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);

        XAxis xaxis = mChart.getXAxis();
        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(1f);
        xaxis.setDrawLabels(true);
        xaxis.setDrawAxisLine(true);
        xaxis.setTextSize(10f);
        xaxis.setLabelCount(months.length);
        xaxis.setValueFormatter(new IndexAxisValueFormatter(months));

        YAxis yAxisLeft = mChart.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setStartAtZero(true);
        yAxisLeft.setEnabled(true);

        mChart.getAxisRight().setEnabled(false);
        mChart.setExtraOffsets(10, 0, 0, 30);
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();


        valueSet1.add( new BarEntry(0, jan));
        valueSet1.add( new BarEntry(1, feb));
        valueSet1.add( new BarEntry(2, mar));
        valueSet1.add( new BarEntry(3, apr));
        valueSet1.add( new BarEntry(4, may));
        valueSet1.add( new BarEntry(5, jun));
        valueSet1.add( new BarEntry(6, jul));
        valueSet1.add( new BarEntry(7, aug));
        valueSet1.add( new BarEntry(8, sep));
        valueSet1.add( new BarEntry(9, oct));
        valueSet1.add( new BarEntry(10, nov));
        valueSet1.add( new BarEntry(11, dec));


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(valueSet1, " ");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setDrawValues(true);

        dataSets.add(barDataSet);

        BarData data = new BarData(dataSets);
        mChart.animateY(3000);
        mChart.setDrawValueAboveBar(true);
        mChart.setData(data);
        mChart.invalidate();
    }
}
