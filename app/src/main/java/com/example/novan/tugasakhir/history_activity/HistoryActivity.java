package com.example.novan.tugasakhir.history_activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.UIcomponent.MyValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private PieChart pieChart;
    private LineChart lineChart;
    public static final int[] COLORS = {
            ColorTemplate.rgb("#2ecc71"), ColorTemplate.rgb("#e74c3c")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pieChart = (PieChart) findViewById(R.id.pieChart);
        lineChart = (LineChart) findViewById(R.id.lineGraph);

        //create Line Chart
        createLineChart();

        //create Pie Chart
        createPieChart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void createLineChart(){
        // create description text
        lineChart.getDescription().setEnabled(true);
        lineChart.getDescription().setText("Last 7 Day's Record Data");

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
        final ArrayList<String> xLabel = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            xLabel.add(""+i);
        }

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setSpaceTop(50f);
        lineChart.getAxisLeft().setSpaceBottom(50f);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value);
            }
        });

        // add data
        setData(7, 10);

        lineChart.animateY(2500);

        // dont forget to refresh the drawing
        lineChart.invalidate();
    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            int val = (int) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
            labels.add(""+i);
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
            set1 = new LineDataSet(values, "");

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
            set1.setMode(set1.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                    ? LineDataSet.Mode.LINEAR
                    : LineDataSet.Mode.CUBIC_BEZIER);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            lineChart.setData(data);
        }
    }

    private void createPieChart(){
        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(70f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        // add a selection listener
        setPieData(2, 100);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Statistik\nKonsumsi Obat");
        return s;
    }

    private void setPieData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");

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
