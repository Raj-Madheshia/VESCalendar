package com.example.vescalendar;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;

public class NotificationClass extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public NotificationClass() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String s = extras.getString("title");
        String de = extras.getString("desp");
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(s);
        builder.setContentText(de);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setAutoCancel(true);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        b.putString("notification","1");
        notifyIntent.putExtras(b);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}
