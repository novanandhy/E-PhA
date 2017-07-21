package com.example.novan.tugasakhir.util.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

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
    public String TAG = "TAGapp";
    private int ONGOING_NOTIFICATION_ID = 101;

    static int BUFF_SIZE = 50;
    static int TEMP_SIZE = 2;
    static public double[] temp = new double[TEMP_SIZE];

    public double GRAVITY = 9.8;
    double th3 = (1.2*GRAVITY);

    private SensorManager sensorManager;

    public static String curr_state;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(TAG, "Received Start Foreground Intent ");
            showNotification();
        }else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }


        return START_STICKY;
    }

    private void showNotification(){
        Intent nextIntent = new Intent(this, SensorService.class);
        nextIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Epha Fall Detector")
                .setContentText("Deteksi jatuh berjalan")
                .setSmallIcon(R.drawable.medicine)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.stop, "Stop",pnextIntent)
                .setColor(getResources().getColor(R.color.custom_primary_color))
                .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);

        //create sensor listener
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);

        Log.d(TAG,"Service is started");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            curr_state="none";

            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];

//            Log.d(TAG,"SERVICE IS RUNNING");
            fall_detection(ax,ay,az);
            SystemState(curr_state);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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

    private void SystemState(String curr_state1) {
        // TODO Auto-generated method stub

        //Fall !!
        if(curr_state1.equalsIgnoreCase("fall")){
            Intent intent = new Intent(this, CountDown.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
