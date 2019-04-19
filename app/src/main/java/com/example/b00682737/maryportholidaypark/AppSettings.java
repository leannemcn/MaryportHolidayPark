package com.example.b00682737.maryportholidaypark;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.b00682737.maryportholidaypark.Models.UserInfo;

import java.util.ArrayList;

public class AppSettings {
    private static final String APP_SHARED_PREFS = "kinetTx_prefs";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private static final String LOGGED_IN = "logged_in";
    private static final String DEVICE_ID = "device_id";
    private static final String DEVICE_ID_SET = "device_id_set";
    private String USEI_ID = "userid";


    public AppSettings(Context context){
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public String getString(String key) {
        return appSharedPrefs.getString(key, "");
    }




    public void clear(){
        prefsEditor.clear();
        prefsEditor.commit();
    }


    public String getUserId() {
        return appSharedPrefs.getString(USEI_ID,"0");
    }

    public void setUserId(String userId) {
        prefsEditor.putString(USEI_ID,userId);
        prefsEditor.commit();
    }




    public void setDeviceIdSet(boolean isAdded) {
        prefsEditor.putBoolean(DEVICE_ID_SET,isAdded);
        prefsEditor.apply();
    }

    public boolean isDeviceIdSet() {
        return appSharedPrefs.getBoolean(DEVICE_ID_SET,false);
    }



    public void setDeviceId(String deviceId) {
        if (!isDeviceIdSet()) {
            setDeviceIdSet(true);
            prefsEditor.putString(DEVICE_ID,deviceId);
            prefsEditor.apply();
        }
    }

    public String getDeviceId() {
        return appSharedPrefs.getString(DEVICE_ID,"");
    }

    public void setLoggedIn() {
        prefsEditor.putBoolean(LOGGED_IN,true);
        prefsEditor.apply();
    }







    private String Email = "Email";
    private String APPOINTMENT = "Appointment";



    public String getEmail() {
        return appSharedPrefs.getString(Email,"");
    }

    public void setEmail(String _Email) {
        prefsEditor.putString(Email,_Email);
        prefsEditor.commit();
    }



    // Save Appointment Data
    public void setAppointmentData(String dumpData) {
        prefsEditor.putString(APPOINTMENT, dumpData);
        prefsEditor.commit();
    }

    // Restore Appointment Data
    public String getAppointmentData() {
        return appSharedPrefs.getString(APPOINTMENT,"");
    }

    private String USER_NAME = "USER_NAME";
    private String USER_EMAIL = "USER_EMAIL";
    private String USER_PHONE = "USER_PHONE";
    private String USER_ADDRESS = "USER_ADDRESS";
    private String USER_ID = "USER_ID";
    private String USER_ROLE = "USER_ROLE";

    public void saveUser(UserInfo userInfo) {
        if (userInfo != null) {
            prefsEditor.putString(USER_NAME, userInfo.name);
            prefsEditor.putString(USER_EMAIL, userInfo.email);
            prefsEditor.putString(USER_PHONE, userInfo.phone);
            prefsEditor.putString(USER_ADDRESS, userInfo.address);
            prefsEditor.putString(USER_ID, userInfo.id);
            prefsEditor.putString(USER_ROLE, userInfo.uRole);

            prefsEditor.commit();
        } else {
            prefsEditor.putString(USER_NAME, "");
            prefsEditor.putString(USER_EMAIL, "");
            prefsEditor.putString(USER_PHONE, "");
            prefsEditor.putString(USER_ADDRESS, "");
            prefsEditor.putString(USER_ID, "");
            prefsEditor.putString(USER_ROLE, "");

            prefsEditor.commit();
        }
    }

    public UserInfo getUser() {
        UserInfo user = new UserInfo();
        user.name = appSharedPrefs.getString(USER_NAME,"");
        user.email = appSharedPrefs.getString(USER_EMAIL,"");
        user.phone = appSharedPrefs.getString(USER_PHONE,"");
        user.address = appSharedPrefs.getString(USER_ADDRESS,"");
        user.id = appSharedPrefs.getString(USER_ID,"");
        user.uRole = appSharedPrefs.getString(USER_ROLE,"");

        return user;
    }
}
