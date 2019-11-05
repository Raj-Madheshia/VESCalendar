package com.example.vescalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;

public class Notificationreceiver extends BroadcastReceiver {
    public Notificationreceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String s = extras.getString("title");
        String de = extras.getString("desp");
        Intent intent1 = new Intent(context, NotificationClass.class);
        Bundle b = new Bundle();
        b.putString("title",s);
        b.putString("desp",de);
        intent1.putExtras(b);
        context.startService(intent1);
    }
}
