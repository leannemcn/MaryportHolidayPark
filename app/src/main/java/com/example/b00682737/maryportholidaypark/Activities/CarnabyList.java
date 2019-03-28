package com.example.b00682737.maryportholidaypark.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.b00682737.maryportholidaypark.Adapters.CarnabyAdapter;
import com.example.b00682737.maryportholidaypark.Models.CarnabyModel;
import com.example.b00682737.maryportholidaypark.RentalCaravans;

import java.util.ArrayList;

public class CarnabyList extends BaseActivity{

    private ArrayList<CarnabyModel> carnabyImages;
    private RecyclerView rvCarnaby;
    private CarnabyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carnaby_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        rvCarnaby = findViewById(R.id.rv_carnabyList);
        carnabyImages = new ArrayList<>();
        adapter = new CarnabyAdapter(this, carnabyImages);
        rvCarnaby.setAdapter(adapter);
        rvCarnaby.setLayoutManager(new LinearLayoutManager(this));

        setImageData();
    }

    public void setImageData() {
        int[] tmpData = RentalCaravans.carnabyImages;
        carnabyImages.clear();
        for(int image: tmpData) {
            CarnabyModel item = new CarnabyModel();
            item.setImage(image);
            carnabyImages.add(item);
        }
        adapter.notifyDataSetChanged();
    }
}

