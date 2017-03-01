package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
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

        view = inflater.inflate(R.layout.homescreen_layout,container,false);

        lvHomePage = (ListView) view.findViewById(R.id.list);
        lvHomePage.setAdapter(new MyListAdapter(getActivity().getApplicationContext(),R.layout.alarm_list,list));

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
                viewHolder.text = (TextView) convertView.findViewById(R.id.textView);
                viewHolder.text.setText(getItem(position));
                viewHolder.subtext = (TextView) convertView.findViewById(R.id.textView2);
                viewHolder.subtext.setText(getItem(position));
                viewHolder.switch2 = (Switch) convertView.findViewById(R.id.switch2);
                viewHolder.switch2.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
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
                mainViewHolder.text.setText(getItem(position));
            }

            return convertView;
        }
    }

    public class ViewHolder{
        TextView text;
        TextView subtext;
        Switch switch2;
    }
}

