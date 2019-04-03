package com.example.b00682737.maryportholidaypark.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.b00682737.maryportholidaypark.Adapters.DeltaAdapter;
import com.example.b00682737.maryportholidaypark.Models.DeltaModel;
import com.example.b00682737.maryportholidaypark.RentalCaravans;

import java.util.ArrayList;

public class DeltaList extends BaseActivity {
    RecyclerView rvDelta;
    DeltaAdapter adapter;
    ArrayList<DeltaModel> deltaImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delta_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        deltaImages = new ArrayList<>();
        rvDelta = findViewById(R.id.rv_deltaList);
        adapter = new DeltaAdapter(this, deltaImages);
        rvDelta.setAdapter(adapter);
        rvDelta.setLayoutManager(new LinearLayoutManager(this));
        setImageData();
    }

    public void setImageData() {
        int[] images = RentalCaravans.deltaImages;
        deltaImages.clear();
        for(int image: images) {
            DeltaModel item = new DeltaModel();
            item.setImage(image);
            deltaImages.add(item);
        }
        adapter.notifyDataSetChanged();
    }

}
