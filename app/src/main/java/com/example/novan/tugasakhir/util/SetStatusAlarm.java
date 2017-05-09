package com.example.novan.tugasakhir.util;

import android.content.Context;

/**
 * Created by Novan on 08/05/2017.
 */

public interface SetStatusAlarm {
    public void setAlarm(Context context, String name, int hour, int minute, int requestCode);
    public void cancelAlarm(Context context,int requestCode);
}
