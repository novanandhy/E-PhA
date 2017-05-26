package com.example.novan.tugasakhir;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.novan.tugasakhir.contact_activity.ContactFragment;
import com.example.novan.tugasakhir.friend_activity.FriendFragment;
import com.example.novan.tugasakhir.history_activity.HistoryFragment;
import com.example.novan.tugasakhir.home_activity.TabFragment;
import com.example.novan.tugasakhir.login_activity.LoginregisterActivity;
import com.example.novan.tugasakhir.profile_activity.ProfileFragment;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.example.novan.tugasakhir.util.database.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    boolean doubleBackToExitPressedOnce = false;

    private DataHelper dataHelper;
    private SessionManager session;


    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SQLite database handler second
        dataHelper = new DataHelper(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = dataHelper.getUserDetails();
        String string = user.get("previllage");

        Log.d("TAG", "previllage = " + string);

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.containerView, new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_item_home) {
                    OpenHomeFragment();
                }

                if (menuItem.getItemId() == R.id.nav_item_contact) {
                    OpenContactFragment();
                }

                if (menuItem.getItemId() == R.id.nav_item_history) {
                    OpenHistoryFragment();
                }

                if (menuItem.getItemId() == R.id.nav_item_friend) {
                    OpenFriendFragment();
                }

                if (menuItem.getItemId() == R.id.nav_item_profile) {
                    OpenProfileFragment();
                }
                if (menuItem.getItemId() == R.id.nav_item_logout) {
                    ShowLogoutConfirmation();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        /**
         * Setup Location utilites
         */
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network_enbled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps_enabled && !network_enbled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Location is not enabled. Please active it");
            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 10);
                }
            });
            dialog.show();
        }
    }

    private void ShowLogoutConfirmation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("Logging Out causes loosing of all data. Are you sure to log out ?");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                session.setLogin(false);
                dataHelper.deleteUsers();
                dataHelper.clear_medicine();
                dataHelper.clear_contact();

                Intent intent = new Intent(MainActivity.this, LoginregisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void OpenHomeFragment() {
        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
        xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
    }

    public void OpenContactFragment() {
        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
        xfragmentTransaction.replace(R.id.containerView, new ContactFragment()).commit();
    }

    public void OpenHistoryFragment() {
        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
        xfragmentTransaction.replace(R.id.containerView, new HistoryFragment()).commit();
    }

    public void OpenFriendFragment() {
        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
        xfragmentTransaction.replace(R.id.containerView, new FriendFragment()).commit();
    }

    public void OpenProfileFragment() {
        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
        xfragmentTransaction.replace(R.id.containerView, new ProfileFragment()).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}