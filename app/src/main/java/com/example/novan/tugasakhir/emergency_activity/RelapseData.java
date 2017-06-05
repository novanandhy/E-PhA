package com.example.novan.tugasakhir.emergency_activity;

import android.content.Context;
import android.util.Log;

import com.example.novan.tugasakhir.models.Locations;
import com.example.novan.tugasakhir.models.User;
import com.example.novan.tugasakhir.util.database.DataHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Novan on 05/06/2017.
 */

public class RelapseData {
    Context context;
    Calendar calendar;

    String TAG = "TAGapp";
    String date, month, year, hour, minute;

    DataHelper dataHelper;
    ArrayList<Locations> locations;
    ArrayList<User> users;

    int count = 0;

    public RelapseData(Context context){
        this.context = context;
    }

    public void setRelapse(String latitude, String longitude) {
        calendar = Calendar.getInstance();
        locations = new ArrayList<>();
        users = new ArrayList<>();

        dataHelper = new DataHelper(context);
        locations = dataHelper.getLocation();
        users = dataHelper.getUserDetail();

        date = String.valueOf(calendar.get(Calendar.DATE));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        year = String.valueOf(calendar.get(Calendar.YEAR));
        hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        minute = String.valueOf(calendar.get(Calendar.MINUTE));

        String uid = users.get(count).getUnique_id();

        dataHelper.addRelapseHistory(uid,latitude,longitude,date,month,year,hour,minute);
    }
}
