package com.example.novan.tugasakhir.home_activity;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.emergency_activity.CountDown;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Novan on 01/02/2017.
 */

public class HomescreenFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, SensorEventListener {
    private View view;
    private Button emergency;
    String TAG = "TAGapp";
    Context context;

    LocationManager mLocationManager;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation, mUpdateLocation, mBestLocation;
    LocationRequest mLocationRequest;
    LocationListener mLocationListener;

    boolean gps_enabled, network_enabled;

    DataHelper dataHelper;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    
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
    double sigma = 0.5,th = 10,th1 = 5,th2 = 2, th3 = (1.2*GRAVITY);

    private SensorManager sensorManager;
    public static String curr_state,prev_state;
    public MediaPlayer m1_fall,m2_sit,m3_stand,m4_walk;

    TextView xCoor, yCoor, zCoor, total, max, min, diff, max_diff, status;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.activity_temporary_emergency, container, false);
        dataHelper = new DataHelper(context);

        //create google API
        buildGoogleApiClient();

        //create sensor listener
        sensorManager=(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        initialize();

        emergency = (Button) view.findViewById(R.id.emergency);
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CountDown.class);
                startActivityForResult(intent,10);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "on start");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"on stop");
        if(mGoogleApiClient.isConnected() && gps_enabled && network_enabled ){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,mLocationListener);
        }
        mGoogleApiClient.disconnect();
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected boolean isBetterLocation(Location updateLocation, Location lastLocation){
        if(lastLocation == null){
            //a new location is always better than no last location
            return true;
        }

        //check if the updated location is newer or older than last location
        long timeDelta = updateLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        //if it's been 2 minutes, the new loction likely used
        //if the user is moved
        if(isSignificantlyNewer){
            return true;
        }else if(isSignificantlyOlder){
            return false;
        }

        //check if the new location is fix accurate or not
        int accuracyDelta = (int) (updateLocation.getAccuracy() - lastLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        //check if the old and new location is from the same provider
        boolean isSameProvider = isSameProvider(updateLocation.getProvider(), lastLocation.getProvider());

        //determine location quality using a combination of timeliness and accuracy
        if(isMoreAccurate){
            return true;
        }else if(isNewer && !isLessAccurate){
            return true;
        }else  if(isNewer && !isSignificantlyLessAccurate){
            return true;
        }

        return false;
    }

    private boolean isSameProvider(String update, String last) {
        if(update == null){
            return last == null;
        }
        return update.equals(last);
    }

    @Override
    public void onConnected(Bundle bundle) {
        /**
         * Setup Location utilites
         */
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        /**
         * check if location or network enbled
         * if not enabled, then enter to settings
         */
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("Location is not enabled. Please active it");
            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 10);
                }
            });
            dialog.show();
        }else {
            createLocationRequest();

            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mUpdateLocation = location;

                    boolean status = isBetterLocation(mUpdateLocation, mLastLocation);

                    if (status) {
                        Log.d("TAG", "update location");
                        mBestLocation = mUpdateLocation;
                        dataHelper.update_location(String.valueOf(mBestLocation.getLatitude()),
                                String.valueOf(mBestLocation.getLongitude()));
                    } else {
                        Log.d("TAG", "last location");
                        mBestLocation = mLastLocation;
                    }

                    Log.d("TAG", "Best location latitude = " + mBestLocation.getLatitude());
                    Log.d("TAG", "Best location longitude = " + mBestLocation.getLongitude());
                }
            };
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];

            AddData(ax,ay,az);
            posture_recognition(window,ay);
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
        m1_fall=MediaPlayer.create(context, R.raw.fall);
        m2_sit=MediaPlayer.create(context, R.raw.sitting);
        m3_stand=MediaPlayer.create(context, R.raw.standing);
        m4_walk=MediaPlayer.create(context, R.raw.walking);

        xCoor = (TextView)view.findViewById(R.id.xcoor);
        yCoor = (TextView)view.findViewById(R.id.ycoor);
        zCoor = (TextView)view.findViewById(R.id.zcoor);
        total = (TextView)view.findViewById(R.id.total);
        max = (TextView)view.findViewById(R.id.maximum);
        min = (TextView)view.findViewById(R.id.minimum);
        diff = (TextView)view.findViewById(R.id.difference);
        max_diff = (TextView)view.findViewById(R.id.max_difference);
        status = (TextView)view.findViewById(R.id.status_position);
    }

    private void fall_detection(double ax, double ay, double az) {
        double maximum = GRAVITY;
        double minimum = GRAVITY;

        a_n=Math.sqrt((ax*ax)+(ay*ay)+(az*az));

        xCoor.setText("X: "+ax);
        yCoor.setText("Y: "+ay);
        zCoor.setText("Z: "+az);
        total.setText("TA :"+a_n);


        if (maximum < a_n){
            maximum = a_n;
        }else if(minimum > a_n){
            minimum = a_n;
        }

        max.setText("maximum ="+maximum);
        min.setText("minimum ="+minimum);

        if (x == 0){
            temp[0] = minimum;
        }else{
            temp[1] = maximum;
            double dif = temp[1] - temp[0];
            diff.setText("difference ="+dif);

            if(max_dif < dif){
                max_dif = dif;
                max_diff.setText("maximum difference ="+max_dif);
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

    private void posture_recognition(double[] window2,double ay2) {
        // TODO Auto-generated method stub
        int zrc=compute_zrc(window2);
        if(zrc==0){

            if(Math.abs(ay2)<th1){
                curr_state="sitting";
            }else{
                curr_state="standing";
            }

        }else{

            if(zrc>th2){
                curr_state="walking";
            }else{
                curr_state="none";
            }

        }
    }

    private int compute_zrc(double[] window2) {
        // TODO Auto-generated method stub
        int count=0;
        for(i=1;i<=BUFF_SIZE-1;i++){

            if((window2[i]-th)<sigma && (window2[i-1]-th)>sigma){
                count=count+1;
            }

        }
        return count;
    }

    private void SystemState(String curr_state1,String prev_state1) {
        // TODO Auto-generated method stub

        //Fall !!
        if(!prev_state1.equalsIgnoreCase(curr_state1)){
            if(curr_state1.equalsIgnoreCase("fall")){
                m1_fall.start();
                status.setText("Fall");
                Intent intent = new Intent(context, CountDown.class);
                startActivityForResult(intent,10);
            }
            if(curr_state1.equalsIgnoreCase("sitting")){
//                m2_sit.start();
                status.setText("Sit");
            }
            if(curr_state1.equalsIgnoreCase("standing")){
//                m3_stand.start();
                status.setText("Stand");
            }
            if(curr_state1.equalsIgnoreCase("walking")){
//                m4_walk.start();
                status.setText("Walk");
            }
        }
    }
    private void AddData(double ax2, double ay2, double az2) {
        // TODO Auto-generated method stub
        a_norm=Math.sqrt((ax2*ax2)+(ay2*ay2)+(az2*az2));
        for(i=0;i<=BUFF_SIZE-2;i++){
            window[i]=window[i+1];
        }
        window[BUFF_SIZE-1]=a_norm;
    }
}