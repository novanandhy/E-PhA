package com.example.novan.tugasakhir;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.novan.tugasakhir.home_activity.MedicineActivity;
import com.example.novan.tugasakhir.home_activity.TabFragment;
import com.example.novan.tugasakhir.login_activity.LoginregisterActivity;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.models.Schedule;
import com.example.novan.tugasakhir.models.User;
import com.example.novan.tugasakhir.profile_activity.ProfileFragment;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.example.novan.tugasakhir.util.database.SessionManager;
import com.example.novan.tugasakhir.util.service.SensorService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    boolean doubleBackToExitPressedOnce = false;
    String TAG = "TAGapp";

    private DataHelper dataHelper;
    private SessionManager session;

    MedicineActivity medicineActivity;
    Context context;

    ArrayList<User> users;
    ArrayList<Medicine> medicines;
    ArrayList<Schedule> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        //SQLite database handler second
        dataHelper = new DataHelper(getApplicationContext());

        //declaration of object
        users = new ArrayList<>();
        medicines = new ArrayList<>();
        schedules = new ArrayList<>();
        medicineActivity = new MedicineActivity();

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from sqlite
        users = dataHelper.getUserDetail();

        //fetching all medicine
        medicines = dataHelper.getAllMedicine();

        //start service
        Intent myService = new Intent(this, SensorService.class);
        if (!isMyServiceRunning(SensorService.class)) {
            startService(myService);
        }

        int count = 0;
        Log.d(TAG,"previllage user = "+users.get(count).getPrevillage());

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
         * Apply if there is intent
         */
        try{
            Intent intent = getIntent();
            if (intent != null){
                String tag = (String) intent.getSerializableExtra("TAG");

                if(tag.equalsIgnoreCase("profile")){
                    OpenProfileFragment();
                }
            }
        }catch (Exception e){}

        /**
         * Setup Drawer Toggle of the Toolbar
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    private void ShowLogoutConfirmation() {

        /**
         * create alert dialog
         */
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("Logging Out causes loosing of all data. Are you sure to log out ?");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //get all medicine list
                for (int j = 0; j < medicines.size(); j++){
                    schedules = dataHelper.getAllSchedule(medicines.get(j).getUid());

                    //remove all schedule
                    medicineActivity.removeSchedule(context, schedules);
                }

                /**
                 * clear all session and database after logout
                 */
                session.setLogin(false);
                dataHelper.deleteUsers();
                dataHelper.clear_medicine();
                dataHelper.clear_contact();
                dataHelper.clear_schedule();
                dataHelper.clear_relapse();

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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        /**
         * create double press for exiting
         */
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }
}