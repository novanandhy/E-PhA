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

import com.example.novan.tugasakhir.util.database.DataHelper;
import com.example.novan.tugasakhir.util.interfaceUtil.SetStatusAlarm;
import com.example.novan.tugasakhir.util.interfaceUtil.TimePickerInterface;

import java.util.Calendar;

/**
 * Created by Novan on 16/03/2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, SetStatusAlarm{
    private String TAG = "TAGapp";
    DataHelper dataHelper;
    int id, status, hourNow, minuteNow;
    String name;
    TimePickerInterface timePickerInterface;
    Calendar calendar = Calendar.getInstance();
    long time;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

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
        return new TimePickerDialog(getActivity(),this, hour, minute,
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

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,00);

        time = calendar.getTimeInMillis();

//        if(hourNow < calendar.get(Calendar.HOUR_OF_DAY) || hourNow == calendar.get(Calendar.HOUR_OF_DAY) && minuteNow < calendar.get(Calendar.MINUTE)){
//            time = time+1000*60*60*24;
//        }
        Log.d(TAG,"milis = "+time);

        PendingIntent pendingintent = PendingIntent.getBroadcast(context, requestCode, intent,0);

        AlarmManager alarmManager  = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingintent);
    }

    public void cancelAlarm(Context context, int requestCode) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(context, requestCode, intent,0);

        AlarmManager alarmManager  = (AlarmManager)context .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingintent);
    }

}
