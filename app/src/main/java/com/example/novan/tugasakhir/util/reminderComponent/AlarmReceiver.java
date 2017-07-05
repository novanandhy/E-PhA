package com.example.novan.tugasakhir.util.reminderComponent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.util.database.DataHelper;

import java.util.ArrayList;

/**
 * Created by Novan on 08/05/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = "APPname";
    String name;

    private ArrayList<Medicine> medicines;
    private DataHelper dataHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        dataHelper = new DataHelper(context);
        medicines = new ArrayList<>();

        medicines = dataHelper.getAllMedicine();

        int requestCode = intent.getIntExtra("requestCode",0);
        String uid = intent.getStringExtra("uid");

        for (int i = 0 ; i < medicines.size() ; i++){
            if (uid.equalsIgnoreCase(medicines.get(i).getUid())){
                name = medicines.get(i).getMedicine_name();
            }
        }

        Log.d(TAG,"Alarm code "+requestCode);

        //Uri sound = Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.alarm);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        Intent notificationIntent = new Intent(context, DialogAlarm.class);
        notificationIntent.putExtra("uid",uid);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setSmallIcon(R.drawable.medicine)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("E-PhA")
                .setContentText("Saatnya minum obat "+name)
                .setContentInfo("")
                .setSound(notification);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, builder.getNotification());
    }
}
