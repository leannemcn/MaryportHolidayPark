package com.example.b00682737.maryportholidaypark.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.b00682737.maryportholidaypark.Models.DeltaModel;

import java.util.ArrayList;

public class DeltaAdapter extends RecyclerView.Adapter {
    private Context _context;
    private ArrayList<DeltaModel> images;

    public DeltaAdapter(Context context, ArrayList<DeltaModel> images) {
        this._context = context;
        this.images = images;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delta, parent, false);
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
        int image = images.get(pos).getImage();
        holder.deltaImage.setImageResource(image);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView deltaImage;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            deltaImage = itemView.findViewById(R.id.img_delta);
        }
    }
}
