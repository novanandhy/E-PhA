package com.example.novan.tugasakhir.profile_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Contact;
import com.example.novan.tugasakhir.util.DataHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Novan on 13/03/2017.
 */

public class ProfileFragment extends Fragment {
    private View view;
    TextView contactCount, namePreview;
    int contactSize;
    DataHelper dataHelper;
    ArrayList<Contact> contacts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        dataHelper = new DataHelper(getActivity().getApplicationContext());
        contacts = new ArrayList<>();
        contacts = dataHelper.getAllContacts();
        contactSize = contacts.size();

        contactCount = (TextView) view.findViewById(R.id.id_contacts);
        contactCount.setText(""+contactSize);

        HashMap<String, String> user = dataHelper.getUserDetails();
        String name = user.get("username");

        namePreview = (TextView) view.findViewById(R.id.name_profile);
        namePreview.setText(name);


        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_profile);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),EditProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
