package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;
import com.eralp.circleprogressview.ProgressAnimationListener;
import com.example.novan.tugasakhir.R;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novan on 01/02/2017.
 */

public class OverviewFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.progressbar_circle, container, false);

        CircleProgressView itemProgressBar = (CircleProgressView) view.findViewById(R.id.circle_progress_view);
        itemProgressBar.setProgressWithAnimation(20, 3000);



//        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab2);
//        fab.attachToListView(lv, new ScrollDirectionListener() {
//            @Override
//            public void onScrollDown() {
//
//            }
//
//            @Override
//            public void onScrollUp() {
//
//            }
//        });
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        return view;
    }
}