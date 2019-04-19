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
import com.example.b00682737.maryportholidaypark.R;

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

    @Override
    protected void onHandleIntent(Intent intent) {
        mTodoText = intent.getStringExtra(TODOTEXT);
        mChecklistId = intent.getIntExtra(TODOUUID, -1);

        if (!TextUtils.isEmpty(mTodoText) && mChecklistId != -1) {
            Log.d("Leanne", "onHandleIntent called");

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent mainIntent = new Intent(this, UserHomeActivity.class);
            mainIntent.putExtra(TodoNotificationService.TODOUUID, mChecklistId);



            Notification notification = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.ic_title_reminder))
                    .setContentText(mTodoText)
                    .setSmallIcon(R.drawable.ic_add_alarm_grey_200_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setPriority(Notification.PRIORITY_MAX)

                    .setContentIntent(PendingIntent.getActivity(this, mChecklistId, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();

            manager.notify(mChecklistId, notification);
        }
    }

}
