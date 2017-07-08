package com.example.novan.tugasakhir.home_activity;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.models.Schedule;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by Novan on 01/02/2017.
 */

public class HomescreenFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private View view;
    String TAG = "TAGapp";
    Context context;

    LocationManager mLocationManager;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation, mUpdateLocation, mBestLocation;
    LocationRequest mLocationRequest;
    LocationListener mLocationListener;

    boolean gps_enabled, network_enabled;

    DataHelper dataHelper;

    private ArrayList<Schedule> schedules;
    private ArrayList<Medicine> medicines;

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_homescreen, container, false);
        dataHelper = new DataHelper(context);
        schedules = new ArrayList<>();
        medicines = new ArrayList<>();

        medicines = dataHelper.getAllMedicine();

        //create google API
        buildGoogleApiClient();

        initView();

        return view;
    }

    @Override
    public void onResume() {
        initView();
        super.onResume();
    }

    private void initView() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        schedules.clear();
        schedules = dataHelper.getAllActiveSchedule();

        RecyclerView.Adapter adapter = new DataAdapter(schedules);
        recyclerView.setAdapter(adapter);
    }

    private class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
        private ArrayList<Schedule> schedules;

        public DataAdapter(ArrayList<Schedule> schedules){
            this.schedules = schedules;
        }


        @Override
        public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_cardview_event_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
            int hour = schedules.get(i).getHour();
            int minute = schedules.get(i).getMinute();
            String time;
            String uid_medicine = schedules.get(i).getUid();

            if (hour<10 && minute<10){
                time = "0"+hour+":0"+minute;
            }else if(minute<10 && hour>=10){
                time = +hour+":0"+minute;
            }else if(hour<10 && minute>=10){
                time = "0"+hour+":"+minute;
            }else{
                time = hour+":"+minute;
            }

            viewHolder.time.setText(time);

            //get data for print cardview
            for (int count = 0; count < medicines.size(); count++){
                if (uid_medicine.equalsIgnoreCase(medicines.get(count).getUid())){
                    String name = medicines.get(count).getMedicine_name();
                    int dosage = medicines.get(count).getDosage();
                    byte[] img = medicines.get(count).getImage();

                    viewHolder.name.setText(name);
                    viewHolder.dosage.setText(""+dosage);
                    viewHolder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(img,0,img.length));
                }
            }

        }

        @Override
        public int getItemCount() {
            return schedules.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView time;
            TextView dosage;
            ImageView imageView;

            public ViewHolder(View view) {
                super(view);
                name = (TextView)view.findViewById(R.id.medicine_name_cardview);
                time = (TextView)view.findViewById(R.id.time_cardview);
                dosage = (TextView)view.findViewById(R.id.dosage_cardview);
                imageView =(ImageView) view.findViewById(R.id.image_cardview);
            }
        }
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
            dialog.setMessage("Location tidak aktif, tolong aktifkan terlebih dahulu");
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
}