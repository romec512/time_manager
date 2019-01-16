package com.example.roman.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import DataBase.TaskDistribution;

public class ListenerService extends Service {
    public ListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals("STOP_FOREGROUND")) {
                Intent notifyService = new Intent(this, NotificationService.class);
                stopService(notifyService);
                stopSelf();
            }
        }
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent1 = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent1, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000, 5000, pIntent);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo2, options);
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, ListenerService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Date date = new Date();
        Format format = new SimpleDateFormat("dd-MM-yyyy");
        long tasksCount = TaskDistribution.count(TaskDistribution.class, "task_date = ?",
                new String[]{format.format(date)});
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo1)
                        .setContentTitle("Сканирование!")
                        .setAutoCancel(true)
                        .setLargeIcon(bitmap)
                        .setContentIntent(pendingIntent);
        startForeground(2, builder.build());
        return START_STICKY;
    }
}
