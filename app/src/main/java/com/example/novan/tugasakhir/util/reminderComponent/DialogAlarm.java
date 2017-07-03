package com.example.novan.tugasakhir.util.reminderComponent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.models.Schedule;
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

public class DialogAlarm extends Activity {
    private Button okbutton, cancelbutton;
    private TextView message;
    private String name;
    private String TAG = "TAGapp";

    private DataHelper dataHelper;
    private ArrayList<Medicine> medicines;
    private ArrayList<User> users;
    private ArrayList<Schedule> schedules;
    TimePickerFragment timePickerFragment = new TimePickerFragment();

    Calendar calendar;

    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.dialog_alarm);

        dataHelper = new DataHelper(this);
        medicines = new ArrayList<>();
        users = new ArrayList<>();
        schedules = new ArrayList<>();

        users = dataHelper.getUserDetail();
        int count = 0;

        uid = users.get(count).getUnique_id();

        name = getIntent().getStringExtra("name");
        Log.d(TAG,"Name medicine dialog "+name);

        message = (TextView) findViewById(R.id.dialog_text);
        message.setText("Saatnya meminum obat "+name);

        okbutton = (Button) findViewById(R.id.ok_dialog);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatusAndRemain(name,1);
            }
        });
        cancelbutton = (Button) findViewById(R.id.cancel_dialog);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatusAndRemain(name,0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()){
            Log.d(TAG,"Outside dialog touched");
        }
        return false;
    }

    private void changeStatusAndRemain(String name, int status){
        medicines = dataHelper.getAllMedicine();

        calendar = Calendar.getInstance();

        String date = String.valueOf(calendar.get(Calendar.DATE));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String year = String.valueOf(calendar.get(Calendar.YEAR));

        for (int i = 0; i <medicines.size() ; i++){
            if(name.equalsIgnoreCase(medicines.get(i).getMedicine_name())){
                int id = medicines.get(i).getId();
                String name_medicine = medicines.get(i).getMedicine_name();
                int amount = medicines.get(i).getAmount();
                int dosage = medicines.get(i).getDosage();
                int remain = medicines.get(i).getRemain();
                int count = medicines.get(i).getCount();

                schedules = dataHelper.getAllSchedule(medicines.get(i).getUid());

                Log.d(TAG,"Name medicine = "+name_medicine);
                Log.d(TAG,"remain medicine = "+remain);
                if(status == 1 && (remain - dosage) > 0){
                    remain = remain-dosage;
                    storeRelapseHistory(uid,id,"1",date,month,year);
                    dataHelper.update_medicine(id,name_medicine,amount,dosage,remain,count);
                }else if(status == 0 && (remain - dosage) > 0){
                    storeRelapseHistory(uid,id,"0",date,month,year);
                    dataHelper.update_medicine(id,name_medicine,amount,dosage,remain,count);
                }else{
                    dataHelper.delete_medicine(id);
                    removeSchedule(schedules.size());
                }

                Log.d(TAG,"remain last stock = "+remain);
                finish();
            }
        }
    }

    private void removeSchedule(int size) {
        for (int i = 0 ; i< size ; i++){
            timePickerFragment.cancelAlarm(DialogAlarm.this,schedules.get(i).getId());
        }
    }

    private void storeRelapseHistory(final String uid, final int id_medicine, final String status, final String date, final String month, final String year) {

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STORE_MEDICINE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        dataHelper.addStatusHistory(uid,id_medicine,status,date,month,year);
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("unique_id", uid);
                params.put("id_medicine", String.valueOf(id_medicine));
                params.put("status", status);
                params.put("date", date);
                params.put("month", month);
                params.put("year", year);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
