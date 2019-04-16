package com.example.b00682737.maryportholidaypark.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.b00682737.maryportholidaypark.R;

import com.example.b00682737.maryportholidaypark.Models.CarnabyModel;

import java.util.ArrayList;

public class CarnabyAdapter extends RecyclerView.Adapter {

    private Context _context;
    private ArrayList<CarnabyModel> images;

    public CarnabyAdapter(Context context, ArrayList<CarnabyModel> images) {
        this._context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carnaby, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        initViewHolder((ViewHolder) viewHolder, i);
    }

    @Override
    public int getItemCount() {
        return images != null?images.size(): 0;
    }

    public void initViewHolder(ViewHolder holder, int pos) {
        holder.imgCarnaby.setImageResource(images.get(pos).getImage());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCarnaby;
        int position;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCarnaby = itemView.findViewById(R.id.img_carnaby);
        }
    }
}

