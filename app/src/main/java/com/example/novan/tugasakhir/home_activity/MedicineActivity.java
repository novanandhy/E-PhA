package com.example.novan.tugasakhir.home_activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.rey.material.widget.Switch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MedicineActivity extends AppCompatActivity {
    private ListView lvHomePage;
    private CircleProgressView circleProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain_medicine);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < itemname.length; ++i) {
            list.add(itemname[i]);
        }

        Random random = new Random();
        float randomval = 100 * random.nextFloat();
        circleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_view_medicine);
        circleProgressView.setProgressWithAnimation(randomval, 2000);
        if (randomval <= 20) {
            circleProgressView.setCircleColor(getResources().getColor(R.color.custom_progress_red_progress));
        } else {
            circleProgressView.setCircleColor(getResources().getColor(R.color.custom_progress_green_progress));
        }

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
                        Toast.makeText(MedicineActivity.this,"yes clicked",Toast.LENGTH_SHORT).show();
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
