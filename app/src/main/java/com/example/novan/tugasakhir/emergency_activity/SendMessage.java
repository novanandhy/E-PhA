package com.example.novan.tugasakhir.emergency_activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.novan.tugasakhir.models.Locations;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by Novan on 25/05/2017.
 */

public class SendMessage implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private String TAG = "TAGapp";
    private String number_phone = "085646780564";
    Context context;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationManager locationManager;

    private String latitude, longitude;

    DataHelper dataHelper;
    ArrayList<Locations> locations;

    public SendMessage(Context context) {
        this.context = context;
    }

    public void sendSMSByManager() {
        dataHelper = new DataHelper(context);
        locations = new ArrayList<>();

        //create google api connection
        buildGoogleApiClient();

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
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        //create request for update location
        createLocationRequest();

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null){
            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
            try{
               delivSMS(latitude, longitude);
                Log.d(TAG,"deliver by last known location");
            }catch (Exception ex){
                Log.d(TAG,"Failed to deliver SMS");
                ex.printStackTrace();
            }
        }else {
            locations = dataHelper.getLocation();
            for (int i = 0 ; i < locations.size() ; i++){
                latitude = locations.get(i).getLatitude();
                longitude = locations.get(i).getLongitude();
                try{
                    delivSMS(latitude, longitude);
                    Log.d(TAG,"deliver by sqlite location");
                }catch (Exception ex){
                    Log.d(TAG,"Failed to deliver SMS");
                    ex.printStackTrace();
                }
            }
        }
    }

    private void delivSMS(String latitude, String longitude) {
        String link = "http://google.com/maps/place/"+latitude+","+longitude;
        String message = "Hi, I'm novan. I'm in trouble now\nPlease check me at my location:\n"+
                link+"\n\n\nvia E-Pha Apps";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number_phone,null,message,null,null);
        Log.d(TAG,message);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
