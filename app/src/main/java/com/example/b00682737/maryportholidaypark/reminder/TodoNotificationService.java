package com.example.b00682737.maryportholidaypark.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.example.b00682737.maryportholidaypark.Activities.UserHomeActivity;

public class TodoNotificationService extends IntentService {

    public static final String TODOTEXT = "toDoTextString";
    public static final String TODOUUID = "toDoUUIDString";

    private String mTodoText;
    private int mChecklistId;
    private Context mContext;

    public TodoNotificationService() {
        super("TodoNotificationService");
    }

}
