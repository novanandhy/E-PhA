package com.example.novan.tugasakhir.contact_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.home_activity.HomescreenFragment;
import com.example.novan.tugasakhir.home_activity.InputMedicineActivity;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.rey.material.widget.Switch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novan on 05/03/2017.
 */

public class ContactFragment extends Fragment {
    private View view;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
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

        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(new MyListAdapter(getActivity().getApplicationContext(),R.layout.content_contact_list,list));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_contact);
        fab.attachToListView(listView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrollUp() {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private class MyListAdapter extends ArrayAdapter<String> {
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
                viewHolder.text = (TextView) convertView.findViewById(R.id.contact);
                viewHolder.text.setText(getItem(position));
                viewHolder.subtext = (TextView) convertView.findViewById(R.id.contact2);
                viewHolder.subtext.setText(getItem(position));
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_contact);
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
        ImageView imageView;
    }
}
