package com.example.b00682737.maryportholidaypark.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class FBbooking implements Parcelable {
    int caravanId, caravanCheckOut;
    String caravanCheckIn, transactionId, caravanBedrooms, extras, caravanName;

    String key;

    public FBbooking() {

    }

    protected FBbooking(Parcel in) {
        caravanId = in.readInt();
        caravanCheckOut = in.readInt();
        caravanCheckIn = in.readString();
        transactionId = in.readString();
        caravanBedrooms = in.readString();
        extras = in.readString();
        caravanName = in.readString();

        key = in.readString();
    }

    public static final Creator<FBbooking> CREATOR = new Creator<FBbooking>() {
        @Override
        public FBbooking createFromParcel(Parcel in) {
            return new FBbooking(in);
        }

        @Override
        public FBbooking[] newArray(int size) {
            return new FBbooking[size];
        }
    };

    public String getCaravanBedrooms() {
        return caravanBedrooms;
    }

    public void setCaravanBedrooms(String caravanBedrooms) {
        this.caravanBedrooms = caravanBedrooms;
    }

    public int getCaravanId() {
        return caravanId;
    }

    public void setCaravanId(int caravanId) {
        this.caravanId = caravanId;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public int getCaravanCheckOut() {
        return caravanCheckOut;
    }

    public void setCaravanCheckOut(int caravanCheckOut) {
        this.caravanCheckOut = caravanCheckOut;
    }

    public String getCaravanCheckIn() {
        return caravanCheckIn;
    }

    public void setCaravanCheckIn(String caravanCheckIn) {
        this.caravanCheckIn = caravanCheckIn;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCaravanName() {
        return caravanName;
    }

    public void setCaravanName(String caravanName) {
        this.caravanName = caravanName;
    }


    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(caravanId);
        dest.writeInt(caravanCheckOut);
        dest.writeString(caravanCheckIn);
        dest.writeString(transactionId);
        dest.writeString(caravanBedrooms);
        dest.writeString(extras);
        dest.writeString(caravanName);
        dest.writeString(key);
    }
}
}

