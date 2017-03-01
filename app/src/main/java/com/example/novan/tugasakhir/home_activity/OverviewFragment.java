package com.example.novan.tugasakhir.home_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.ChartItem;
import com.example.novan.tugasakhir.util.PieChartItem;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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

        view = inflater.inflate(R.layout.overview_layout, container, false);

        ListView lv = (ListView) view.findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        // 30 items
        for (int i = 0; i < 10; i++) {
            list.add(new PieChartItem(generateDataPie(i + 1), getContext()));
        }

        ChartDataAdapter cda = new ChartDataAdapter(getContext(), list);
        lv.setAdapter(cda);

       FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab.attachToListView(lv, new ScrollDirectionListener() {
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return view;
    }

    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 1; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(int cnt) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < 2; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);
        return cd;
    }
}