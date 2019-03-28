package com.example.b00682737.maryportholidaypark;

import android.app.Application;

public class BookingApplication extends Application {
    private static BookingApplication INSTANCE;
    public static BookingApplication getInstance() {
        return INSTANCE;
    }

    private MobileSettings appSettings;

    // 1 second
    public static final int GPS_MIN_TIME = 1000;
    // 1 meter
    public static final int GPS_MIN_DISTANCE = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
        appSettings = new MobileSettings(getApplicationContext());
    }
}