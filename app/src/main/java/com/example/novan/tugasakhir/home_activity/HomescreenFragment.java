package com.example.novan.tugasakhir.home_activity;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.emergency_activity.SendMessage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.rey.material.widget.Switch;

import java.util.List;

/**
 * Created by Novan on 01/02/2017.
 */

public class HomescreenFragment extends Fragment {
    private ListView lvHomePage;
    private View view;
    private Button emergency;
    SendMessage sendMessage;
    String latitude, longitude;
    String TAG = "TAGapp";
    MainActivity ma;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        String[] itemname = new String[]{
//                "Safari",
//                "Camera",
//                "Global",
//                "FireFox",
//                "UC Browser",
//                "Android Folder",
//                "VLC Player",
//                "Cold War"
//        };
//        final ArrayList<String> list = new ArrayList<String>();
//        for (int i = 0; i < itemname.length; ++i) {
//            list.add(itemname[i]);
//        }

        view = inflater.inflate(R.layout.activity_temporary_emergency, container, false);
        sendMessage = new SendMessage();
        ma = new MainActivity();

        emergency = (Button) view.findViewById(R.id.emergency);
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = ma.latitude;
                longitude = ma.longitude;
                Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
                sendMessage.sendSMSByManager("085646780564", latitude, longitude);
            }
        });

//        lvHomePage = (ListView) view.findViewById(R.id.list_alarm);
//        lvHomePage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                DialogFragment newFragment = new TimePickerFragment();
//                newFragment.show(getActivity().getFragmentManager(),"TimePicker");
//            }
//        });
//        lvHomePage.setAdapter(new MyListAdapter(getActivity().getApplicationContext(),R.layout.content_alarm_list,list));
        return view;
    }

    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout,parent,false);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.alarmTime = (TextView) convertView.findViewById(R.id.alarm_time);
            viewHolder.alarmTime.setText(getItem(position));
            viewHolder.alarmMedicine = (TextView) convertView.findViewById(R.id.alarm_medicine);
            viewHolder.alarmMedicine.setText(getItem(position));
            viewHolder.alarmSwitch = (Switch) convertView.findViewById(R.id.switch_alarm);
            viewHolder.alarmSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(Switch view, boolean checked) {
                    if(checked){
                        viewHolder.alarmTime.setTextColor(getResources().getColor(R.color.custom_primary_color));
                    }else{
                        viewHolder.alarmTime.setTextColor(getResources().getColor(R.color.custom_primary_text));
                    }
                }
            });

            convertView.setTag(viewHolder);

            return convertView;
        }
    }

    public class ViewHolder{
        TextView alarmTime;
        TextView alarmMedicine;
        Switch alarmSwitch;
    }
}

