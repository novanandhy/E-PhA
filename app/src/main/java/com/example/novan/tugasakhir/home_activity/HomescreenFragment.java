package com.example.novan.tugasakhir.home_activity;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novan.tugasakhir.R;
import com.rey.material.widget.Switch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novan on 01/02/2017.
 */

public class HomescreenFragment extends Fragment{
    private ListView lvHomePage;
    private View view;
    private int hour,min;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        view = inflater.inflate(R.layout.fragment_homescreen,container,false);

        lvHomePage = (ListView) view.findViewById(R.id.list_alarm);
        lvHomePage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity().getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getFragmentManager(),"TimePicker");
            }
        });
        lvHomePage.setAdapter(new MyListAdapter(getActivity().getApplicationContext(),R.layout.content_alarm_list,list));
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
            ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.alarmTime = (TextView) convertView.findViewById(R.id.alarm_time);
                viewHolder.alarmTime.setText(getItem(position));
                viewHolder.alarmMedicine = (TextView) convertView.findViewById(R.id.alarm_medicine);
                viewHolder.alarmMedicine.setText(getItem(position));
                viewHolder.alarmSwitch = (Switch) convertView.findViewById(R.id.switch_alarm);
                viewHolder.alarmSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(Switch view, boolean checked) {
                        if(checked){
                            Toast.makeText(getActivity().getApplicationContext(),position+" is checked", Toast.LENGTH_SHORT);
                        }
                    }
                });

                convertView.setTag(viewHolder);
            }else {
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.alarmTime.setText(getItem(position));
            }

            return convertView;
        }
    }

    public class ViewHolder{
        TextView alarmTime;
        TextView alarmMedicine;
        Switch alarmSwitch;
    }
}

