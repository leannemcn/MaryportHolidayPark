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

    public String getString(String key) {
        return appSharedPrefs.getString(key, "");
    }

    public int getInt(String key) {
        return appSharedPrefs.getInt(key, 0);
    }

    public long getLong(String key) {
        return appSharedPrefs.getLong(key, 0);
    }

    public boolean getBoolean(String key) {
        return appSharedPrefs.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public void putInt(String key, int value) {
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public void putLong(String key, long value) {
        prefsEditor.putLong(key, value);
        prefsEditor.commit();
    }

    public void putBoolean(String key, boolean value) {
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public void remove(String key){
        prefsEditor.remove(key);
        prefsEditor.commit();
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


    public String getFN() {
        return appSharedPrefs.getString(FN,"");
    }

    public void setFN(String userId) {
        prefsEditor.putString(FN,userId);
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

    public boolean isLoggedIn() {
        return appSharedPrefs.getBoolean(LOGGED_IN,false);
    }

    public boolean logOut() {
        prefsEditor.putBoolean(LOGGED_IN,false);
        return prefsEditor.commit();
    }

    public void setPIN(String pin) {
        prefsEditor.putString(PIN,pin);
        prefsEditor.apply();
    }

    public String getPIN() {
        return appSharedPrefs.getString(PIN,"");
    }

    public void setDeviceToken(String deviceToken) {
        prefsEditor.putString(DEVICE_TOKEN,deviceToken);
        prefsEditor.apply();
    }

    public String getDeviceLat() {
        return appSharedPrefs.getString(LAT,"0.0");
    }

    public String getDeviceLng() {
        return appSharedPrefs.getString(LNG, "0.0");
    }

    public void setDeviceLat(String deviceLat) {
        prefsEditor.putString(LAT, deviceLat);
        prefsEditor.apply();
    }

    public void setDeviceLng(String deviceLng) {
        prefsEditor.putString(LNG, deviceLng);
        prefsEditor.apply();
    }

    public String getDeviceToken() {
        return appSharedPrefs.getString(DEVICE_TOKEN,"");
    }


    private static final String IS_CLOCKED_IN = "is_p_in";
    private static final String IN_TIME = "in_time";
    private static final String LUNCH_IN_TIME = "lunch_in_time";
    private static final String LUNCH_END_TIME = "lunch_end_time";
    private static final String REMAINING_OUT_TIME = "r_o_time";
    private static final String IS_HAVING_LUNCH = "is_h_lunch";
    private static final String IS_LUNCH_TIME_OVER = "is_lunch_t_ovver";

    public void setInTime(String inTime) {
        prefsEditor.putString(IN_TIME,inTime);
        prefsEditor.commit();
    }

    public String getInTime() {
        return appSharedPrefs.getString(IN_TIME,"");
    }

    public void setLunchInTime(String inTime) {
        prefsEditor.putString(LUNCH_IN_TIME,inTime);
        prefsEditor.commit();
    }

    public String getLunchInTime() {
        return appSharedPrefs.getString(LUNCH_IN_TIME,"");
    }

    public void setLunchEndTime(String inTime) {
        prefsEditor.putString(LUNCH_END_TIME,inTime);
        prefsEditor.commit();
    }

    public String getLunchEndTime() {
        return appSharedPrefs.getString(LUNCH_END_TIME,"");
    }

    public void setClockedIn() {
        prefsEditor.putBoolean(IS_CLOCKED_IN,true);
        prefsEditor.apply();
    }

    public void setClockedOut() {
        prefsEditor.putBoolean(IS_CLOCKED_IN,false);
        prefsEditor.apply();
    }

    public void setIsHavingLunch() {
        prefsEditor.putBoolean(IS_HAVING_LUNCH,true);
        prefsEditor.apply();
    }

    public void setCompletedHavingLunch() {
        prefsEditor.putBoolean(IS_HAVING_LUNCH,false);
        prefsEditor.apply();
    }

    public boolean isHavingLunch() {
        return appSharedPrefs.getBoolean(IS_HAVING_LUNCH,false);
    }

    public boolean isClockedIn() {
        return appSharedPrefs.getBoolean(IS_CLOCKED_IN,false);
    }

    public void setRemainingTime(long remainingTime) {
        prefsEditor.putLong(REMAINING_OUT_TIME,remainingTime);
        prefsEditor.commit();
    }

    public void resetRemainingTime() {
        prefsEditor.putLong(REMAINING_OUT_TIME,30*60*1000);
        prefsEditor.commit();
    }

    public long getRemainingTime() {
        return  appSharedPrefs.getLong(REMAINING_OUT_TIME,30*60*1000);
    }

    public void setLunchTimeOver(boolean isLunchTimeOver) {
        prefsEditor.putBoolean(IS_LUNCH_TIME_OVER,isLunchTimeOver);
        prefsEditor.apply();
    }

    public boolean isLunchTimeOver() {
        return appSharedPrefs.getBoolean(IS_LUNCH_TIME_OVER,false);
    }

    private String LN = "LN";
    private String zip = "zip";
    private String Street = "Street";
    private String StreetNum = "StreetNum";
    private String City = "City";
    private String St = "St";
    private String Email = "Email";
    private String Deliveries = "Deliveries";
    private String DRIVER_ID = "driverid";

    private String CP = "CP";

    private String APPOINTMENT = "Appointment";

    public String getLN() {
        return appSharedPrefs.getString(LN,"");
    }

    public void setLN(String userId) {
        prefsEditor.putString(LN,userId);
        prefsEditor.commit();
    }

    public String getZip() {
        return appSharedPrefs.getString(zip,"");
    }

    public void setZip(String _zip) {
        prefsEditor.putString(zip,_zip);
        prefsEditor.commit();
    }

    public String getStreet() {
        return appSharedPrefs.getString(Street,"");
    }

    public void setStreet(String street) {
        prefsEditor.putString(Street,street);
        prefsEditor.commit();
    }

    public String getStreetNum() {
        return appSharedPrefs.getString(StreetNum,"");
    }

    public void setStreetNum(String streetNum) {
        prefsEditor.putString(StreetNum,streetNum);
        prefsEditor.commit();
    }

    public String getCity() {
        return appSharedPrefs.getString(City,"");
    }

    public void setCity(String city) {
        prefsEditor.putString(City,city);
        prefsEditor.commit();
    }

    public String getSt() {
        return appSharedPrefs.getString(St,"");
    }

    public void setSt(String _st) {
        prefsEditor.putString(St,_st);
        prefsEditor.commit();
    }

    public String getEmail() {
        return appSharedPrefs.getString(Email,"");
    }

    public void setEmail(String _Email) {
        prefsEditor.putString(Email,_Email);
        prefsEditor.commit();
    }

    public String getCP() {
        return appSharedPrefs.getString(CP,"");
    }

    public void setCP(String _CP) {
        prefsEditor.putString(CP,_CP);
        prefsEditor.commit();
    }

    public String getDriverID() {
        return appSharedPrefs.getString(DRIVER_ID,"");
    }

    public void setDriverID(String _driverId) {
        prefsEditor.putString(DRIVER_ID, _driverId);
        prefsEditor.commit();
    }

    public ArrayList<String> getMyDeliveries() {

        String deliveriesArchives = appSharedPrefs.getString(Deliveries,"");
        if (TextUtils.isEmpty(deliveriesArchives)) {
            return null;
        } else {
            String[] deliveriIDs = deliveriesArchives.split("_");
            if (deliveriIDs != null && deliveriIDs.length > 0) {
                ArrayList<String> deliveriIDList = new ArrayList<>();
                for (String delID : deliveriIDs) {
                    deliveriIDList.add(delID);
                }
                return deliveriIDList;
            } else {
                return null;
            }
        }
    }

    public void addMyDelivery(String newDel) {
        String deliveries = appSharedPrefs.getString(Deliveries,"");
        deliveries += "_" + newDel;

        prefsEditor.putString(Deliveries, deliveries);
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
