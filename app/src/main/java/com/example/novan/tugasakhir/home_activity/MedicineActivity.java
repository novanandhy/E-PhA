package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.models.Schedule;
import com.example.novan.tugasakhir.util.reminderComponent.TimePickerFragment;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.example.novan.tugasakhir.util.interfaceUtil.TimePickerInterface;
import com.rey.material.widget.Switch;

import java.util.ArrayList;

public class MedicineActivity extends AppCompatActivity implements TimePickerInterface{
    private ListView lvHomePage;
    private CircleProgressView circleProgressView;
    private Medicine medicine;
    private int id;
    private TextView name, dosage, time;
    private String name_medicine, uid_name_medicine;
    private int dosage_medicine, time_medicine;
    int amount, remain;
    float percentage;
    private DataHelper dataHelper;
    String TAG = "TAGapp";

    ArrayList<Schedule> schedules;
    MyListAdapter myListAdapter;
    TimePickerFragment newFragment = new TimePickerFragment();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain_medicine);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataHelper = new DataHelper(this);
        schedules = new ArrayList<>();

        //get value from intent
        medicine = getIntent().getParcelableExtra("medicine");
        id = getIntent().getIntExtra("id", 0);
        uid_name_medicine = getIntent().getStringExtra("uid");

        //create percentage of stock
        amount = medicine.getAmount();
        remain = medicine.getRemain();
        percentage = (remain*100)/amount;

        circleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_view_medicine);
        circleProgressView.setProgressWithAnimation(percentage, 2000);
        if (percentage <= 10) {
            circleProgressView.setCircleColor(getResources().getColor(R.color.custom_progress_red_progress));
        } else if(percentage > 10 && percentage < 20){
            circleProgressView.setCircleColor(getResources().getColor(R.color.custom_progress_orange_progress));
        }else{
            circleProgressView.setCircleColor(getResources().getColor(R.color.custom_progress_green_progress));
        }

        //get value of form
        name_medicine = medicine.getMedicine_name();
        dosage_medicine = medicine.getDosage();
        time_medicine = medicine.getCount();

        //set text of form
        name = (TextView) findViewById(R.id.medicine_name);
        dosage = (TextView) findViewById(R.id.medicine_dosage);
        time = (TextView) findViewById(R.id.medicine_time);
        SetText(name_medicine,dosage_medicine,time_medicine);


        //call list view of alarm
        callListviewAlarm();
    }

    public void callListviewAlarm() {

        schedules = dataHelper.getAllSchedule(uid_name_medicine);

        lvHomePage = (ListView) findViewById(R.id.list_alarm_medicine);
        lvHomePage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("id",schedules.get(i).getId());
                bundle.putString("name", medicine.getMedicine_name());
                bundle.getInt("status", schedules.get(i).getStatus());

                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(),"TimePicker");

            }
        });
        myListAdapter = new MyListAdapter(getApplicationContext(), R.layout.content_alarm_list, schedules);
        lvHomePage.setAdapter(myListAdapter);
    }

    public void SetText(String name_param, int dosage_param, int time_param) {
        name.setText(name_param);
        dosage.setText(""+dosage_param);
        time.setText(""+time_param);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_medicine, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_remove:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure to remove this data ?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataHelper.delete_medicine(medicine.getId());
                        dataHelper.delete_schedule(medicine.getUid());
                        removeSchedule(context,schedules);

                        setResult(RESULT_OK);
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
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditMedineActivity.class);
                intent.putExtra("medicine", medicine);
                intent.putExtra("id", id);
                startActivityForResult(intent,30);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null) {
            if (resultCode == 10) {
                finish();
                startActivity(getIntent());
            }
            else if(requestCode == 30){
                Medicine med = data.getParcelableExtra("medicine");
                SetText(med.getMedicine_name(),med.getDosage(),med.getCount());
                OnTimeUpdate();
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void OnTimeUpdate() {
        schedules = dataHelper.getAllSchedule(uid_name_medicine);
        myListAdapter.notifyDataSetChanged();
        Log.d(TAG,"time updated");
    }

    public class MyListAdapter extends ArrayAdapter<Schedule> {
        private int layout;

        public MyListAdapter(Context context, int resource, ArrayList<Schedule> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            String time = null;

            final int id = schedules.get(position).getId();
            final String name = medicine.getMedicine_name();
            final int minute = schedules.get(position).getMinute();
            final int hour = schedules.get(position).getHour();
            int status = schedules.get(position).getStatus();
            time = checkIfTime(minute, hour);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.alarm_time = (TextView) convertView.findViewById(R.id.alarm_time);
            viewHolder.medicine_name = (TextView) convertView.findViewById(R.id.alarm_medicine);
            viewHolder.switch_alarm = (Switch) convertView.findViewById(R.id.switch_alarm);
            viewHolder.alarm_time.setText(time);
            viewHolder.medicine_name.setText(name);
            if(status != 0){
                viewHolder.switch_alarm.setChecked(true);
            }else{
                viewHolder.switch_alarm.setChecked(false);
            }
            viewHolder.switch_alarm.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(Switch view, boolean checked) {
                    if(checked){
                        viewHolder.alarm_time.setTextColor(getResources().getColor(R.color.custom_primary_color));
                        dataHelper.update_schedule(id,hour,minute,1);
                        newFragment.setAlarm(MedicineActivity.this,name,hour,minute,id);
                        Toast.makeText(MedicineActivity.this, "Alarm active", Toast.LENGTH_SHORT).show();
                    }else{
                        viewHolder.alarm_time.setTextColor(getResources().getColor(R.color.custom_primary_text));
                        dataHelper.update_schedule(id,hour,minute,0);
                        newFragment.cancelAlarm(MedicineActivity.this,id);
                    }
                }
            } );
            convertView.setTag(viewHolder);
            return convertView;
        }

        public class ViewHolder {
            TextView alarm_time;
            TextView medicine_name;
            Switch switch_alarm;
        }
    }

    // Set time to list view into String
    private String checkIfTime(int minute, int hour) {
        String time = null;
        if((minute < 10) && (hour >= 10)){
            time = ""+hour+":0"+minute;
            return time;
        }
        else if ((hour < 10) && (minute >= 10)){
            time = "0"+hour+":"+minute;
            return time;
        }
        else if ((hour < 10) && (minute < 10)){
            time = "0"+hour+":0"+minute;
            return time;
        }
        else{
            time = ""+hour+":"+minute;
            return time;
        }
    }

    public void removeSchedule(Context context, ArrayList<Schedule> schedules) {
        for (int i = 0 ; i < schedules.size() ; i++){
            Log.d(TAG,"ID schedule deleted = "+schedules.get(i).getId());
            newFragment.cancelAlarm(context, schedules.get(i).getId());
        }
    }
}
