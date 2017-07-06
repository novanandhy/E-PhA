package com.example.novan.tugasakhir.history_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.UIcomponent.MyValueFormatter;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Novan on 30/03/2017.
 */

public class HistoryFragment extends Fragment {
    View view;
    private PieChart pieChart;
    private LineChart lineChart;
    private Button filter, cancel, confirm;
    private ScrollChoice scrollChoice;
    Calendar calendar = Calendar.getInstance();

    private List<String> data = new ArrayList<>();

    public static final int[] COLORS = {
            ColorTemplate.rgb("#2ecc71"), ColorTemplate.rgb("#e74c3c")
    };

    private String month, month_parameter;

    private DataHelper dataHelper;
    private Context context;

    private String TAG = "TAGapp";

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        context = getActivity().getApplicationContext();

        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        lineChart = (LineChart) view.findViewById(R.id.lineGraph);
        filter = (Button) view.findViewById(R.id.filter_button);

        dataHelper = new DataHelper(getActivity());

        Bundle arguments = getArguments();
        month = arguments.getString("month");

        if (month == null){
            month = String.valueOf(calendar.get(Calendar.MONTH));
        }

        Log.d(TAG,"month = "+month);

        //get value of history from sqlite
        int consumed = dataHelper.getAllHistoryWhere(1, month).size();
        int not_consumed = dataHelper.getAllHistoryWhere(0, month).size();

        //create Line Chart
        createLineChart();

        //create Pie Chart
        createPieChart(consumed, not_consumed);

        loadData();

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View parentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter,null);
                bottomSheetDialog.setContentView(parentView);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
                bottomSheetBehavior.setPeekHeight(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,380,context.getResources().getDisplayMetrics()));
                bottomSheetDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                bottomSheetDialog.show();

                scrollChoice = (ScrollChoice) parentView.findViewById(R.id.scroll_choice);
                scrollChoice.addItems(data,Integer.valueOf(month)-1);
                scrollChoice.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {
                        month_parameter = String.valueOf(position+1);
                    }
                });

                cancel = (Button) parentView.findViewById(R.id.cancel_sheet);
                confirm = (Button) parentView.findViewById(R.id.confirm_sheet);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("month",month_parameter);
                        startActivity(intent);
                        getActivity().finish();
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

    private void loadData() {
        data.add("Januari");
        data.add("Februari");
        data.add("Maret");
        data.add("April");
        data.add("Mei");
        data.add("Juni");
        data.add("Juli");
        data.add("Agustus");
        data.add("September");
        data.add("Oktober");
        data.add("November");
        data.add("Desember");
    }

    private void createLineChart(){
        // create description text
        lineChart.getDescription().setEnabled(true);
        lineChart.getDescription().setText("Data 7 hari terakhir");

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        l.setEnabled(false);

        //create layout graph
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setSpaceTop(50f);
        lineChart.getAxisLeft().setSpaceBottom(50f);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawLabels(true);

        // add data
        setData(30, 10);

        lineChart.animateY(2000);

        // dont forget to refresh the drawing
        lineChart.invalidate();
    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            int val = (int) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "tono");

            // set the line to be drawn like this "- - - - - -"
            set1.setLineWidth(3f);
            set1.setCircleRadius(5f);
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.WHITE);
            set1.setDrawValues(true);
            set1.setValueFormatter(new MyValueFormatter());
            set1.setValueTextColor(Color.WHITE);
            set1.setValueTextSize(15);
//            set1.setMode(set1.getMode() == LineDataSet.Mode.CUBIC_BEZIER
//                    ? LineDataSet.Mode.LINEAR
//                    : LineDataSet.Mode.CUBIC_BEZIER);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            lineChart.setData(data);
        }
    }

    private void createPieChart(int consumed, int not_consumed){
        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        // add a selection listener
        setPieData(consumed, not_consumed);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend legend = new Legend();
        legend.setEnabled(false);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Riwayat\nKonsumsi Obat");
        return s;
    }

    private void setPieData(int consumed, int not_consumed) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        //set data value of pie chart
        entries.add(new PieEntry((float) consumed, "diminum"));
        entries.add(new PieEntry((float) not_consumed, "tidak diminum"));

        PieDataSet dataSet = new PieDataSet(entries,"");

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }
}
