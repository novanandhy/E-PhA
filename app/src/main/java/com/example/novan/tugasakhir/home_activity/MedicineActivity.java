package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;
import com.example.novan.tugasakhir.R;
import com.rey.material.widget.Switch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MedicineActivity extends AppCompatActivity {
    private ListView lvHomePage;
    private CircleProgressView circleProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

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
            circleProgressView.setCircleColor(getResources().getColor(R.color.custom_progress_blue_progress));
        }

        lvHomePage = (ListView) findViewById(R.id.list2);
        lvHomePage.setAdapter(new MyListAdapter(getApplicationContext(), R.layout.content_alarm_list, list));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_remove:
                Toast.makeText(MedicineActivity.this, "remove clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditMedineActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.subtext = (TextView) convertView.findViewById(R.id.textView2);
            viewHolder.switch2 = (Switch) convertView.findViewById(R.id.switch2);
            viewHolder.text.setText(getItem(position));
            viewHolder.subtext.setText(getItem(position));
            viewHolder.switch2.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(Switch view, boolean checked) {
                    if (checked) {
                        Toast.makeText(getContext(), position + " is checked", Toast.LENGTH_SHORT);
                    }
                }
            });
            convertView.setTag(viewHolder);
            return convertView;
        }

        public class ViewHolder {
            TextView text;
            TextView subtext;
            Switch switch2;
        }
    }
}