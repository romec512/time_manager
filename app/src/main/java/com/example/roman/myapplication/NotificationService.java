package com.example.roman.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import DataBase.TaskDistribution;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Date nowTime = new Date();
        Format format = new SimpleDateFormat("HH:mm");
        String nowTimeStr = format.format(nowTime);
        format = new SimpleDateFormat("dd-MM-yyyy");
        List<TaskDistribution> distributions = TaskDistribution.find(TaskDistribution.class, "task_date = ?",
                new String[]{format.format(nowTime)});
        //Обновляем кол-во уведомлений в уведомлении с кол-вом задач на день
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo2, options);
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        String message = "";
        if(distributions.size() == 0){
            message = "На сегодня у вас не запланировано ни одной задачи.";
        } else if(distributions.size() == 1) {
            message = "На сегодня у вас запланирована 1 задача.";
        } else if(distributions.size() > 1 && distributions.size() < 5)
        {
            message = "На сегодня у вас запланировано " + distributions.size() + " задачи.";
        } else if(distributions.size() > 5)
        {
            message = "На сегодня у вас запланировано " + distributions.size() + " задач.";
        }
        Intent stopTrackingService = new Intent(this, ListenerService.class);
        stopTrackingService.setAction("STOP_FOREGROUND");
        PendingIntent pIntent = PendingIntent.getService(this, 0, stopTrackingService, 0);
        NotificationCompat.Builder foregroundBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo1)
                .setContentTitle("Сканирование!")
                .setContentText(message)
                .setLargeIcon(bitmap)
                .addAction(R.drawable.logo1,"Не уведомлять", pIntent);
        nm.notify(2, foregroundBuilder.build());


        format = new SimpleDateFormat("HH:mm");
        String currentTime = format.format(nowTime);
        String[] split = currentTime.split(":");
        int currentHours = Integer.parseInt(split[0]);
        int currentMinutes = Integer.parseInt(split[1]);
        for(TaskDistribution distribution : distributions){
            split = distribution.startTime.split(":");
            int distrStartHour = Integer.parseInt(split[0]);
            int distrStartMinutes = Integer.parseInt(split[1]);
//            split = distribution.stopTime.split(":");
//            int distrStopHour = Integer.parseInt(split[0]);
//            int distrStopMinutes = Integer.parseInt(split[1]);
            if(distrStartHour == currentHours && distrStartMinutes < currentMinutes && currentMinutes < 10){
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.logo1)
                                .setContentTitle("Пора браться за выполнение задачи!")
                                .setContentText(distribution.task.comment)
                                .setAutoCancel(true)
                                .setLargeIcon(bitmap)
                                .setVibrate(new long[]{1000, 2000, 1000, 2000});
                Notification notification = builder.build();
                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(3, notification);
                stopSelf();
            }
        }
        return Service.START_NOT_STICKY;
    }
}
