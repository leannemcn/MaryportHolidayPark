package com.example.b00682737.maryportholidaypark;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BookingApplication extends Application {
    private static BookingApplication INSTANCE;
    public static BookingApplication getInstance() {
        return INSTANCE;
    }

    private AppSettings appSettings;

    // 1 second
    public static final int GPS_MIN_TIME = 1000;
    // 1 meter
    public static final int GPS_MIN_DISTANCE = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
        appSettings = new AppSettings(getApplicationContext());
    }
}