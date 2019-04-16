package com.example.b00682737.maryportholidaypark.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.b00682737.maryportholidaypark.R;

import com.example.b00682737.maryportholidaypark.Models.FBbooking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookingListAdapter extends BaseAdapter {
    private Context mContext;
    private List<FBbooking> mDataList;

    public BookingListAdapter(Context mContext, List<FBbooking> dataList) {
        this.mContext = mContext;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        if (mDataList == null)
            return 0;
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_booking,
                    null);
        }

        // Get Item Data
        FBbooking currentBooking = (FBbooking) getItem(position);

        // Gas Image
        final ImageView ivEvent = (ImageView) convertView.findViewById(R.id.ivEvent);
        if (currentBooking.getCaravanId() == 1) {
            ivEvent.setImageResource(R.drawable.carnaby0);
        } else {
            ivEvent.setImageResource(R.drawable.delta0);
        }

        TextView tvCaravanName = convertView.findViewById(R.id.tvCaravanName);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvDays = convertView.findViewById(R.id.tvDays);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        TextView tvBeds = convertView.findViewById(R.id.tvBeds);
        TextView tvFuniture = convertView.findViewById(R.id.tvFuniture);
        ImageView ivExpired = convertView.findViewById(R.id.ivExpired);

        tvCaravanName.setText(currentBooking.getCaravanName());
        tvDate.setText(currentBooking.getCaravanCheckIn());
        tvDays.setText(String.format("%d Day(s)", currentBooking.getCaravanCheckOut()));
        tvPrice.setText(String.valueOf(getAmount(currentBooking.getCaravanCheckOut())));
        tvBeds.setText(currentBooking.getCaravanBedrooms());
        tvFuniture.setText(currentBooking.getExtras());

        ivExpired.setVisibility(checkIfExpiredBooking(currentBooking.getCaravanCheckIn(), currentBooking.getCaravanCheckOut()) ? View.VISIBLE : View.GONE);

        return convertView;
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

