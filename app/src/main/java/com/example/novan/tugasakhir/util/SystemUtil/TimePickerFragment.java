package com.example.novan.tugasakhir.util.SystemUtil;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.novan.tugasakhir.util.database.DataHelper;
import com.example.novan.tugasakhir.util.interfaceUtil.SetStatusAlarm;
import com.example.novan.tugasakhir.util.interfaceUtil.TimePickerInterface;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Novan on 16/03/2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, SetStatusAlarm{
    private String TAG = "TAGapp";
    DataHelper dataHelper;
    int id, status, hourNow, minuteNow;
    String name;
    TimePickerInterface timePickerInterface;
    Calendar calendar ;
    long time, timeTmp;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        calendar = Calendar.getInstance();
        hourNow = calendar.get(Calendar.HOUR_OF_DAY);
        minuteNow = calendar.get(Calendar.MINUTE);

        Log.d(TAG, "Hour = "+hourNow+" Minute "+minuteNow);

        dataHelper = new DataHelper(getActivity().getApplicationContext());

        timePickerInterface = (TimePickerInterface) getActivity();

        id = getArguments().getInt("id");
        name = getArguments().getString("name");
        Log.d(TAG,"id schedule = "+id);
        Log.d(TAG,"name schedule = "+name);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hourNow, minuteNow,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        setAlarm(getActivity(),name, hour, minute, id);
        status = 1;
        dataHelper.update_schedule(id,hour,minute,status);
        timePickerInterface.OnTimeUpdate();
    }

    public void setAlarm(Context context, String name, int hour, int minute, int requestCode) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("name", name);
        intent.putExtra("requestCode", requestCode);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);

        timeTmp = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);

        time = calendar.getTimeInMillis();

        if((time - timeTmp) < 0){
            time = time+1000*60*60*24;
        }
        Log.d(TAG,"milis = "+(time - timeTmp));
        int minuteTmp = (int) TimeUnit.MILLISECONDS.toMinutes(time-timeTmp);
        int hoursString = minuteTmp / 60;
        int minuteString = minuteTmp - (hoursString * 60);

        PendingIntent pendingintent = PendingIntent.getBroadcast(context, requestCode, intent,0);

        AlarmManager alarmManager  = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingintent);
        Toast.makeText(context, "Alarm set for "+hoursString+" hours and "+minuteString+" minutes from now", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, int requestCode) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(context, requestCode, intent,0);

        AlarmManager alarmManager  = (AlarmManager)context .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingintent);
    }

}
