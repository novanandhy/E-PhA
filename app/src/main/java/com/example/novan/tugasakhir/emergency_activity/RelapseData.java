package com.example.novan.tugasakhir.emergency_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.novan.tugasakhir.models.Locations;
import com.example.novan.tugasakhir.models.User;
import com.example.novan.tugasakhir.util.database.AppConfig;
import com.example.novan.tugasakhir.util.database.AppController;
import com.example.novan.tugasakhir.util.database.DataHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Novan on 05/06/2017.
 */

public class RelapseData {
    Context context;
    Calendar calendar;

    String TAG;
    String date, month, year, hour, minute;

    DataHelper dataHelper;
    ArrayList<Locations> locations;
    ArrayList<User> users;

    ProgressDialog progressDialog;

    int count = 0;

    public RelapseData(Context context){

        this.context = context;
        TAG = "TAGapp "+context.getClass().getSimpleName();
    }

    public void setRelapse(String latitude, String longitude) {
        calendar = Calendar.getInstance();
        locations = new ArrayList<>();
        users = new ArrayList<>();

        dataHelper = new DataHelper(context);
        progressDialog = new ProgressDialog(context);
        locations = dataHelper.getLocation();
        users = dataHelper.getUserDetail();

        date = String.valueOf(calendar.get(Calendar.DATE));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        year = String.valueOf(calendar.get(Calendar.YEAR));
        hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        minute = String.valueOf(calendar.get(Calendar.MINUTE));

        String uid = users.get(count).getUnique_id();



        storeRelapseHistory(uid,latitude,longitude,date,month,year,hour,minute);
    }

    private void storeRelapseHistory(final String uid, final String latitude, final String longitude, final String date,
                                     final String month, final String year, final String hour, final String minute) {

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STORE_RELAPSE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        dataHelper.addRelapseHistory(uid,latitude,longitude,date,month,year,hour,minute);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("unique_id", uid);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("date", date);
                params.put("month", month);
                params.put("year", year);
                params.put("hour", hour);
                params.put("minute", minute);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}