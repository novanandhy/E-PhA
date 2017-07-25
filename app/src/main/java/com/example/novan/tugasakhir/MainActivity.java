package com.example.novan.tugasakhir;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novan.tugasakhir.contact_activity.ContactFragment;
import com.example.novan.tugasakhir.friend_activity.FriendFragment;
import com.example.novan.tugasakhir.history_activity.HistoryFragment;
import com.example.novan.tugasakhir.home_activity.MedicineActivity;
import com.example.novan.tugasakhir.home_activity.TabFragment;
import com.example.novan.tugasakhir.information_activity.InformationFragment;
import com.example.novan.tugasakhir.login_activity.LoginregisterActivity;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.models.Schedule;
import com.example.novan.tugasakhir.models.User;
import com.example.novan.tugasakhir.profile_activity.ProfileFragment;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.example.novan.tugasakhir.util.database.SessionManager;
import com.example.novan.tugasakhir.util.service.Constants;
import com.example.novan.tugasakhir.util.service.SensorService;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    CircleImageView image_toolbar;
    TextView text_toolbar;
    boolean doubleBackToExitPressedOnce = false;
    String TAG = "TAGapp";
    public String month;

    private DataHelper dataHelper;
    private SessionManager session;

    MedicineActivity medicineActivity;
    Context context;
    Intent myService;
    AlertDialog alertDialog;

    ArrayList<User> users;
    ArrayList<Medicine> medicines;
    ArrayList<Schedule> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image_toolbar = (CircleImageView) findViewById(R.id.image_toolbar);
        text_toolbar  = (TextView) findViewById(R.id.text_toolbar);

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

        //start service
        myService = new Intent(this, SensorService.class);
        if (!isMyServiceRunning(SensorService.class)) {
            //cretae alert dialog
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setMessage("mohon tempatkan smartphone anda di tempat datar");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    myService.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(myService);
                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        //create custom toolbar
        createCustomToolbar();

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

        if (users.get(0).getPrevillage().contains("Penderita")){
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.drawermenu);
            mFragmentTransaction.add(R.id.containerView, new TabFragment()).commit();
        }else{
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.drawermenu_2);
            mFragmentTransaction.add(R.id.containerView, new ProfileFragment()).commit();
        }


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

                if (menuItem.getItemId() == R.id.nav_information) {
                    OpenInformationFragment();
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
            String tag = (String) intent.getSerializableExtra("TAG");
            if (tag != null){
                if(tag.equalsIgnoreCase("profile")){
                    OpenProfileFragment();
                }
            }

            month = (String) intent.getSerializableExtra("month");
            if (month != null){
                Log.d(TAG,"month parameter = "+month);
                OpenHistoryFragment();
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

    private void createCustomToolbar() {
        // get data user from datahelper
        users.clear();
        users = dataHelper.getUserDetail();

        //set identitiy component toolbar
        byte[] image_data = users.get(0).getImage();
        text_toolbar.setText("Hai, "+users.get(0).getName());
        image_toolbar.setImageBitmap(BitmapFactory.decodeByteArray(image_data,0,image_data.length));
        image_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenProfileFragment();
            }
        });
    }

    private void ShowLogoutConfirmation() {

        /**
         * create alert dialog
         */
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("Setelah log out semua data akan terhapus ?");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //fetching all medicine
                medicines = dataHelper.getAllMedicine();

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
                dataHelper.clear_friend();

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
        HistoryFragment historyFragment = new HistoryFragment();
        Bundle arguments = new Bundle();
        arguments.putString("month",month);
        historyFragment.setArguments(arguments);

        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
        xfragmentTransaction.replace(R.id.containerView, historyFragment).commit();
    }

    public void OpenFriendFragment() {
        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
        xfragmentTransaction.replace(R.id.containerView, new FriendFragment()).commit();
    }

    public void OpenProfileFragment() {
        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
        xfragmentTransaction.replace(R.id.containerView, new ProfileFragment()).commit();
    }

    public void OpenInformationFragment() {
        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
        xfragmentTransaction.replace(R.id.containerView, new InformationFragment()).commit();
    }

    @Override
    public void onStart() {
        Log.d(TAG,"onStart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG,"onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume()");
        createCustomToolbar();
        super.onResume();
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