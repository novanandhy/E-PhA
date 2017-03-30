package com.example.novan.tugasakhir.profile_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.novan.tugasakhir.R;

/**
 * Created by Novan on 13/03/2017.
 */

public class ProfileFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);


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
