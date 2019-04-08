package com.example.b00682737.maryportholidaypark.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class MessageInfo implements Parcelable {
    public String email;
    public String phone;
    public String id;
    public String message;
    public String feedback;
    public String key;

    public MessageInfo() {}

    public MessageInfo(Parcel in) {
        email = in.readString();
        phone = in.readString();
        id = in.readString();
        message = in.readString();
        feedback = in.readString();
        key = in.readString();
    }

    public static final Creator<MessageInfo> CREATOR = new Creator<MessageInfo>() {
        @Override
        public MessageInfo createFromParcel(Parcel in) {
            return new MessageInfo(in);
        }

        @Override
        public MessageInfo[] newArray(int size) {
            return new MessageInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(id);
        dest.writeString(message);
        dest.writeString(feedback);
        dest.writeString(key);
    }
}

