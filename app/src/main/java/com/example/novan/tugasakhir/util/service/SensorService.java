package com.example.novan.tugasakhir.util.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.emergency_activity.CountDown;

/**
 * Created by Novan on 20/06/2017.
 */

public class SensorService extends Service implements SensorEventListener{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    //variable for sensor
    public double ax,ay,az;
    public double a_norm;
    public double a_n;

    public int i = 0;
    public int x = 0;
    public double max_dif = 0;

    static int BUFF_SIZE = 50;
    static int TEMP_SIZE = 2;
    static public double[] window = new double[BUFF_SIZE];
    static public double[] temp = new double[TEMP_SIZE];

    public double GRAVITY = 9.8;
    double th3 = (1.2*GRAVITY);

    private SensorManager sensorManager;

    public static String curr_state,prev_state;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // sudah selesai, siapkan notif builder
        NotificationCompat.Builder notifBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My Service")
                        .setContentText("Service is running");

// explisit intent untuk memulai activity
        Intent resultIntent = new Intent(this, MainActivity.class);
// Gunakan taskstack agar saat user menekan tombol back tetap konsisten
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,PendingIntent.FLAG_UPDATE_CURRENT
                );
        notifBuilder.setContentIntent(resultPendingIntent);
        notifBuilder.setAutoCancel(true);//agar setelah ditap tutup otomatis

//notifmanager untuk menampilkan
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// 999 id untuk jika perlu modif nanti (bagusnya jadi konstanta).
        mNotificationManager.notify(999, notifBuilder.build());


        //create sensor listener
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        initialize();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];

            fall_detection(ax,ay,az);
            SystemState(curr_state,prev_state);
            if(!prev_state.equalsIgnoreCase(curr_state)){
                prev_state=curr_state;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void initialize() {
        // TODO Auto-generated method stub
        for(i=0;i<BUFF_SIZE;i++){
            window[i]=0;
        }
        prev_state="none";
        curr_state="none";
    }

    private void fall_detection(double ax, double ay, double az) {
        double maximum = GRAVITY;
        double minimum = GRAVITY;

        a_n=Math.sqrt((ax*ax)+(ay*ay)+(az*az));

        if (maximum < a_n){
            maximum = a_n;
        }else if(minimum > a_n){
            minimum = a_n;
        }

        if (x == 0){
            temp[0] = minimum;
        }else{
            temp[1] = maximum;
            double dif = temp[1] - temp[0];

            if(max_dif < dif){
                max_dif = dif;
            }

            if (temp[1]-temp[0] > th3){
                curr_state="fall";
            }
        }

        if (x == 1){
            x = 0;
        }else{
            x++;
        }
    }

    private void SystemState(String curr_state1,String prev_state1) {
        // TODO Auto-generated method stub

        //Fall !!
        if(!prev_state1.equalsIgnoreCase(curr_state1)){
            if(curr_state1.equalsIgnoreCase("fall")){
                Intent intent = new Intent(this, CountDown.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

}
