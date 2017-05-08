package com.example.novan.tugasakhir.home_activity;

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

import com.example.novan.tugasakhir.util.AlarmReceiver;
import com.example.novan.tugasakhir.util.DataHelper;
import com.example.novan.tugasakhir.util.TimePickerInterface;

import java.util.Calendar;

/**
 * Created by Novan on 16/03/2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    private String TAG = "TAGapp";
    DataHelper dataHelper;
    int id, status;
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
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        time = calendar.getTimeInMillis();
        Log.d(TAG,"milis = "+time);

        setAlarm(name, time, id);

        status = 1;
        dataHelper.update_schedule(id,hour,minute,status);
        timePickerInterface.OnTimeUpdate();
    }

    private void setAlarm(String name, long time, int requestCode) {
        Intent intent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("name", name);
        intent.putExtra("requestCode", requestCode);

        PendingIntent pendingintent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), requestCode, intent,0);

        AlarmManager alarmManager  = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingintent);
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 1, intent,0);

        AlarmManager alarmManager  = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingintent);
    }

}
