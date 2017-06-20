package com.example.novan.tugasakhir.tutorial_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.novan.tugasakhir.R;

/**
 * Created by Novan on 20/06/2017.
 */

public class TutorialFragment5 extends Fragment {
    Context context;
    View view;
    Button button;

    TutorialActivity tutorialActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_tutorial_5, container, false);
        tutorialActivity = new TutorialActivity();

        button = (Button) view.findViewById(R.id.btn_finish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorialActivity.finish();
            }
        });

        return view;
    }
}
