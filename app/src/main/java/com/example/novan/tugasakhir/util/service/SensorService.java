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
    public double a_n;

    public int i = 0;
    public int x = 0;
    public String TAG = "TAGapp SensorService";
    private int ONGOING_NOTIFICATION_ID = 101;

    static int TEMP_SIZE = 2;
    static public double[] temp = new double[TEMP_SIZE];

    public double GRAVITY = 9.8;
    public double threshold = (0.5*GRAVITY);

    public int LIMIT_RECOVER = 30;
    public int LIMIT_NONE = 50;

    private SensorManager sensorManager;
    private Context context;

    public static String curr_state = "none";
    public static String curr_state2;
    public int none = 0;
    public int recover = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

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
//                .setColor(ContextCompat.getColor(context,R.color.custom_primary_color))
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

            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];

            if (curr_state.contains("none") || curr_state.contains("recover")){
                curr_state = fall_detection(ax,ay,az);
            }else{
                FallDetected(ax, ay, az);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private String fall_detection(double ax, double ay, double az) {
        String state = "none";

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

        if (dif > threshold){
            state = "fall";
        }else if (dif > 0.1 && dif < threshold){
            state =  "recover";
        }else{
            state = "none";
        }
    }

        if (x == 1){
        x = 0;
    }else{
        x++;
    }
        return state;
}

    private void FallDetected(double ax, double ay, double az) {
        // TODO Auto-generated method stub
        Log.d(TAG, "FallDetected: ");

        //check for filter fall detection
        curr_state2 = fall_detection(ax,ay,az);

        if (curr_state2.contains("recover")){
            recover++;
        }else if (curr_state2.contains("none")){
            none++;
        }else {
            curr_state = "fall";
            recover = 0;
            none = 0;
        }

        if (recover == LIMIT_RECOVER){
            Log.d(TAG, "User Already Recovered");
            curr_state = "none";
            recover = 0;
            none = 0;
        }else if(none == LIMIT_NONE){
            ShowDialog();
            curr_state = "none";
            recover = 0;
            none = 0;
        }

    }

    private void ShowDialog() {
        Intent intent = new Intent(this, CountDown.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
