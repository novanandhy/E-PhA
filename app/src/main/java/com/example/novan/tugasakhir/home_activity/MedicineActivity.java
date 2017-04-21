package com.example.novan.tugasakhir.home_activity;

import android.app.DialogFragment;
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
import com.example.novan.tugasakhir.util.DataHelper;
import com.rey.material.widget.Switch;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MedicineActivity extends AppCompatActivity {
    private ListView lvHomePage;
    private CircleProgressView circleProgressView;
    private Medicine medicine;
    int amount, remain;
    float percentage;
    private DataHelper dataHelper;
    String TAG = "TAGapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain_medicine);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dataHelper = new DataHelper(this);
        medicine = getIntent().getParcelableExtra("medicine");
        final ArrayList<String> list = new ArrayList<String>();
        String[] itemname = new String[]{
                "Safari",
                "Camera",
                "Global",
                "FireFox",
                "UC Browser",
                "Android Folder",
                "VLC Player",
                "Cold War"
        };
        for (int i = 0; i < itemname.length; ++i) {
            list.add(itemname[i]);
        }

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

        String name_medicine = medicine.getMedicine_name();
        int dosage_medicine = medicine.getDosage();
        int time_medicine = medicine.getCount();

        //set text of form
        TextView name = (TextView) findViewById(R.id.medicine_name);
        TextView dosage = (TextView) findViewById(R.id.medicine_dosage);
        TextView time = (TextView) findViewById(R.id.medicine_time);
        name.setText(name_medicine);
        dosage.setText(""+dosage_medicine);
        time.setText(""+time_medicine);

        lvHomePage = (ListView) findViewById(R.id.list_alarm_medicine);
        lvHomePage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        lvHomePage.setAdapter(new MyListAdapter(getApplicationContext(), R.layout.content_alarm_list, list));
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
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MedicineActivity.this,"no clicked",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditMedineActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;

        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.alarm_time = (TextView) convertView.findViewById(R.id.alarm_time);
            viewHolder.medicine_name = (TextView) convertView.findViewById(R.id.alarm_medicine);
            viewHolder.switch_alarm = (Switch) convertView.findViewById(R.id.switch_alarm);
            viewHolder.alarm_time.setText(getItem(position));
            viewHolder.medicine_name.setText(getItem(position));
            viewHolder.switch_alarm.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(Switch view, boolean checked) {
                    if(checked){
                        viewHolder.alarm_time.setTextColor(getResources().getColor(R.color.custom_primary_color));
                    }else{
                        viewHolder.alarm_time.setTextColor(getResources().getColor(R.color.custom_primary_text));
                    }
                }
            });
            convertView.setTag(viewHolder);
            return convertView;
        }

        public class ViewHolder {
            TextView alarm_time;
            TextView medicine_name;
            Switch switch_alarm;
        }
    }
}
