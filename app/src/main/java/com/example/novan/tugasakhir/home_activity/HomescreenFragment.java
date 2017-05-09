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

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.SystemUtil.TimePickerFragment;
import com.rey.material.widget.Switch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novan on 01/02/2017.
 */

public class HomescreenFragment extends Fragment{
    private ListView lvHomePage;
    private View view;

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

