package com.example.b00682737.maryportholidaypark.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.b00682737.maryportholidaypark.Models.UserInfo;

public class AppSettings {
    private static final String APP_SHARED_PREFS = "kinetTx_prefs";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private static final String LOGGED_IN = "logged_in";
    private static final String DEVICE_ID = "device_id";
    private static final String DEVICE_ID_SET = "device_id_set";
    private static final String DEVICE_TOKEN = "device_token";
    private static final String PIN = "pin";
    private String USEI_ID = "userid";
    private String FN = "FN";
    private String LAT = "LAT";
    private String LNG = "LNG";

    public AppSettings(Context context){
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
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
