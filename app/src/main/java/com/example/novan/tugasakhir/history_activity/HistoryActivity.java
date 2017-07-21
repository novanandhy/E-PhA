package com.example.novan.tugasakhir.history_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.UIcomponent.ErrorDialog;
import com.example.novan.tugasakhir.util.UIcomponent.MyValueFormatter;
import com.example.novan.tugasakhir.util.database.AppConfig;
import com.example.novan.tugasakhir.util.database.AppController;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class HistoryActivity extends AppCompatActivity {
    private PieChart pieChart;
    private LineChart lineChart;
    private Button filter, cancel, confirm;
    private ScrollChoice scrollChoice;
    private Calendar calendar = Calendar.getInstance();
    private ProgressDialog progressDialog;
    private TextView title;

    private List<String> data = new ArrayList<>();

    public static final int[] COLORS = {
            ColorTemplate.rgb("#2ecc71"), ColorTemplate.rgb("#e74c3c")
    };
    public static String[] months = {
            "Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"
    };

    private String uid,name,month, year, month_parameter, consumed, not_consumed;
    private int count_history, error_counter = 0;
    private Context context;

    private String TAG ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain_history);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        context = this;
        TAG = "TAGapp "+context.getClass().getSimpleName();

        pieChart = (PieChart) findViewById(R.id.pieChart);
        lineChart = (LineChart) findViewById(R.id.lineGraph);
        filter = (Button) findViewById(R.id.filter_button);
        title = (TextView) findViewById(R.id.history_title);

        //progress dialog show
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        uid = intent.getExtras().getString("uid");
        name = intent.getExtras().getString("name");

        //set toolbar title
        getSupportActionBar().setTitle("Riwayat "+name);

        consumed = null;
        not_consumed = null;

        if (month == null){
            month = String.valueOf(calendar.get(Calendar.MONTH));
        }

        //generate data from json
        PopulateAdapter(uid, month);

        loadData();

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View parentView = getLayoutInflater().inflate(R.layout.dialog_filter,null);
                bottomSheetDialog.setContentView(parentView);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
                bottomSheetBehavior.setPeekHeight(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,380,context.getResources().getDisplayMetrics()));
                bottomSheetDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                bottomSheetDialog.show();

                scrollChoice = (ScrollChoice) parentView.findViewById(R.id.scroll_choice);
                scrollChoice.addItems(data,Integer.valueOf(month));
                scrollChoice.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {
                        month_parameter = String.valueOf(position);
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
                        PopulateAdapter(uid, month_parameter);
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });
    }

    private void PopulateAdapter(String uid, String month) {
        Log.d(TAG,"Month = "+month);
        error_counter =0;
        title.setText("Riwayat bulan "+months[Integer.parseInt(month)]);

        year = String.valueOf(calendar.get(Calendar.YEAR));

        //get value of history from server
        getMedicineHistory(uid,month,year,"1");
        getMedicineHistory(uid,month,year,"0");

        //get value of relapse history from server
        getRelapsseHistory(uid,month,year);
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

    private void createLineChart(ArrayList<Entry> values){
        // create description text
        lineChart.getDescription().setEnabled(true);
        lineChart.getDescription().setText("Riwayat Jatuh");
        lineChart.getDescription().setTextSize(13);

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
        lineChart.getXAxis().setGranularity(1f);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawLabels(true);

        // add data
        setData(values);

        lineChart.animateY(2000);

        // dont forget to refresh the drawing
        lineChart.invalidate();
    }

    private void setData(ArrayList<Entry> values) {
        LineDataSet set1;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "relapse history");

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
        Log.d(TAG,"consumed = "+consumed);
        Log.d(TAG,"not consumed = "+not_consumed);

        pieChart.getDescription().setText("Riwayat Konsumsi Obat");
        pieChart.getDescription().setTextSize(13);

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

    private void getMedicineHistory(final String uid_user, final String month, final String year, final String status) {
        // Tag used to cancel the request
        String tag_string_req = "req_data";

        progressDialog.setMessage("mohon tunggu sebentar...");
        showDialog();

        //netralizer count history
        count_history = 0;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_HISTORY_MEDICINE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    boolean null_value = jObj.getBoolean("null");

                    Log.d(TAG,"error = "+error);
                    Log.d(TAG,"null = "+null_value);

                    // Check for error node in json
                    if (!error) {
                        if (!null_value){
                            // Now store the user in SQLite
                            JSONArray medicine = jObj.getJSONArray("medicine");
                            Log.d(TAG,""+medicine);

                            count_history = medicine.length();

                            if (status.equalsIgnoreCase("1")){
                                consumed = String.valueOf(count_history);
                            }else{
                                not_consumed = String.valueOf(count_history);
                            }
                        }else {
                            count_history = 0;

                            if (status.equalsIgnoreCase("1")){
                                consumed = String.valueOf(count_history);
                            }else{
                                not_consumed = String.valueOf(count_history);
                            }
                        }
                        //check if variable already filled
                        if (consumed != null){
                            if (not_consumed != null){
                                //create Pie Chart
                                createPieChart(Integer.valueOf(consumed), Integer.valueOf(not_consumed));
                            }else {
                                return;
                            }
                        }else {
                            return;
                        }
                    } else {
                        // Error in getting data. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Log.d(TAG,errorMsg);
                        error_counter++;
                    }

                    if (error_counter > 1){
                        ErrorMessage("riwayat obat tidak tersedia", R.drawable.sad_mood);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    ErrorMessage("gagal mendapatkan data", R.drawable.sad_mood);
                    Log.d(TAG,"JSON erro"+e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid_user", uid_user);
                params.put("month", month);
                params.put("year", year);
                params.put("status", status);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void getRelapsseHistory(final String uid_user, final String month, final String year) {
        // Tag used to cancel the request
        String tag_string_req = "req_data";

        //netralizer count history
        count_history = 0;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_HISTORY_RELAPSE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                ArrayList<Integer> date = new ArrayList<>();
                ArrayList<Entry> values = new ArrayList<Entry>();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    Log.d(TAG,"error = "+error);

                    // Check for error node in json
                    if (!error) {
                        // Now store the user in SQLite
                        JSONArray relapse = jObj.getJSONArray("relapse");
                        Log.d(TAG,""+relapse);

                        count_history = relapse.length();

                        for (int i = 0 ; i < count_history ; i++){
                            JSONObject d = relapse.getJSONObject(i);

                            date.add(i,d.getInt("date"));
                        }

                        values = generateDateSize(date);

                        //create Line Chart
                        createLineChart(values);
                    } else {
                        // Error in getting data. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Log.d(TAG,errorMsg);

                        ErrorMessage("riwayat kambuh tidak tersedia", R.drawable.sad_mood);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    ErrorMessage("gagal mendapatkan data", R.drawable.sad_mood);
                    Log.d(TAG,"JSON error"+e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                ErrorMessage("cek kembali koneksi anda", R.drawable.no_connection);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid_user", uid_user);
                params.put("month", month);
                params.put("year", year);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void ErrorMessage(String s, int img) {
        Intent intent = new Intent(context, ErrorDialog.class);
        intent.putExtra("message",s);
        intent.putExtra("image",img);
        startActivity(intent);
    }

    //generate count of each date
    private ArrayList<Entry> generateDateSize(ArrayList<Integer> date) {
        int len = date.size();
        ArrayList<Entry> values = new ArrayList<Entry>();

        //get size of each date
        Map<Integer, Integer> numDate = new HashMap<Integer, Integer>(Math.min(len,31));

        for (int i = 0 ; i < len ; i++){
            int dateAt = date.get(i);

            if (!numDate.containsKey(dateAt)){
                numDate.put(dateAt, 1);
            }else{
                numDate.put(dateAt, numDate.get(dateAt)+1);
            }
        }

        //sort the key of map
        SortedSet<Integer> keys = new TreeSet<Integer>(numDate.keySet());

        //get value of each key
        Iterator myIterator = keys.iterator();
        while (myIterator.hasNext()){
            int cal = (int) myIterator.next();
            int size = (int) numDate.get(cal);

            values.add(new Entry(cal,size));
        }

        return values;
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
