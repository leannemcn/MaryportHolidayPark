package com.example.b00682737.maryportholidaypark.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.b00682737.maryportholidaypark.Models.MessageInfo;
import java.util.List;

public class MessageListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MessageInfo> mDataList;

    public MessageListAdapter(Context mContext, List<MessageInfo> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message,
                    null);
        }

        // Get Item Data
        MessageInfo messageInfo = (MessageInfo) getItem(position);

        TextView tvMessage = convertView.findViewById(R.id.tvMessage);
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);

        tvMessage.setText(messageInfo.message);
        tvEmail.setText(messageInfo.email);
        tvPhone.setText(messageInfo.phone);

        View btnRemove = convertView.findViewById(R.id.btnRemove);
        btnRemove.setTag(position);
        btnRemove.setOnClickListener(btnRemoveListner);

        return convertView;
    }

    View.OnClickListener btnRemoveListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = (int) v.getTag();

            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
            alertDialogBuilder.setTitle("Confirm remove");
            alertDialogBuilder.setMessage("Would you like to remove this message?")
                    .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();

                    ((AdminMessageListActivity) mContext).removeEvent(position);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    };
}
