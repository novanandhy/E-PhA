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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Novan on 25/05/2017.
 */

public class SendMessage implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private String TAG = "TAGapp";
    private String number_phone = "085646780564";
    Context context;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation, mUpdateLocation, mBestLocation;
    LocationRequest mLocationRequest;
    LocationManager locationManager;

    private String latitude, longitude;

    private static final int TWO_MINUTES = 1000*60*2;
    private static final int FIVE_SECONDS = 1000*5;

    public SendMessage(Context context) {
        this.context = context;
    }

    public void sendSMSByManager() {

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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        createLocationRequest();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                mUpdateLocation = location;
//
//                boolean status = isBetterLocation(mUpdateLocation, mLastLocation);
//
//                if(status){
//                    Log.d("TAG","update location");
//                    mBestLocation = mUpdateLocation;
//                }else {
//                    Log.d("TAG","last location");
//                    mBestLocation = mLastLocation;
//                }
//
//                Log.d("TAG","Best location latitude = "+mBestLocation.getLatitude());
//                Log.d("TAG","Best location longitude = "+mBestLocation.getLongitude());
//            }
//        };
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,locationListener);

        if(mLastLocation != null){
            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
        }

        try{
            String link = "http://google.com/maps/place/"+latitude+","+longitude;
            String message = "Hi, I'm novan. I'm in trouble now\nPlease check me at my location:\n"+
                    link+"\n\n\nvia E-Pha Apps";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number_phone,null,message,null,null);
            Log.d(TAG,"SMS Delivered");
            Log.d(TAG,message);
        }catch (Exception ex){
            Log.d(TAG,"Failed to deliver SMS");
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
}
