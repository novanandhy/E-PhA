package com.example.novan.tugasakhir.emergency_activity;

import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by Novan on 25/05/2017.
 */

public class SendMessage {
    private String TAG = "TAGapp";

    public void sendSMSByManager(String number_phone, String latitude, String  longitude) {
        try{
            String link = "http//google.com/maps/place/"+latitude+","+longitude;
            String message = "Hi, I'm novan. I'm in trouble now\n Please check me at my location:\n"+
                    link+"\n via E-Pha Apps";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number_phone,null,message,null,null);
            Log.d(TAG,"SMS Delivered");
            Log.d(TAG,message);
        }catch (Exception ex){
            Log.d(TAG,"Failed to deliver SMS");
            ex.printStackTrace();
        }
    }
}
