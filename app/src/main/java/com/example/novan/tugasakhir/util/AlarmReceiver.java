package com.example.novan.tugasakhir.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;

/**
 * Created by Novan on 08/05/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = "APPname";
    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getIntExtra("requestCode",0);
        String name = intent.getStringExtra("name");

        Toast.makeText(context, "Alarm code "+requestCode, Toast.LENGTH_SHORT).show();
        Log.d(TAG,"Yee muncul alarm untuk obat "+ name);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setSmallIcon(R.drawable.medicine)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(name)
                .setContentText("Saatnya minum obat"+name)
                .setContentInfo("Alarm");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, builder.getNotification());
    }
}
