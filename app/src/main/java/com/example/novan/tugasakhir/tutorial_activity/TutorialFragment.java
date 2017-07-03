package com.example.novan.tugasakhir.tutorial_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.novan.tugasakhir.R;

/**
 * Created by Novan on 20/06/2017.
 */

public class TutorialFragment extends Fragment {
    Context context;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        return view;
    }
}
