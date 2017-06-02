package com.example.novan.tugasakhir.util.reminderComponent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.models.User;
import com.example.novan.tugasakhir.util.database.DataHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogAlarm extends AppCompatActivity {
    private Button okbutton, cancelbutton;
    private TextView message;
    private String name;
    private String TAG = "TAGapp";
    private DataHelper dataHelper;
    private ArrayList<Medicine> medicines;
    private ArrayList<User> users;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_alarm);

        dataHelper = new DataHelper(this);
        medicines = new ArrayList<>();
        users = new ArrayList<>();

        users = dataHelper.getUserDetail();
        int count = 0;

        uid = users.get(count).getUnique_id();

        name = getIntent().getStringExtra("name");
        Log.d(TAG,"Name medicine dialog "+name);

        message = (TextView) findViewById(R.id.dialog_text);
        message.setText("Time to consume "+name);

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

        for (int i = 0; i <medicines.size() ; i++){
            if(name.equalsIgnoreCase(medicines.get(i).getMedicine_name())){
                int id = medicines.get(i).getId();
                String name_medicine = medicines.get(i).getMedicine_name();
                int amount = medicines.get(i).getAmount();
                int dosage = medicines.get(i).getDosage();
                int remain = medicines.get(i).getRemain();
                int count = medicines.get(i).getCount();

                Log.d(TAG,"Name medicine = "+name_medicine);
                Log.d(TAG,"remain medicine = "+remain);
                if(status == 1 && (remain - dosage) > 0){
                    remain = remain-dosage;
                    dataHelper.addStatusHistory(uid,id,1);
                    dataHelper.update_medicine(id,name_medicine,amount,dosage,remain,count);
                }else if(status == 0 && (remain - dosage) > 0){
                    dataHelper.addStatusHistory(uid,id,0);
                    dataHelper.update_medicine(id,name_medicine,amount,dosage,remain,count);
                }else{
                    dataHelper.delete_medicine(id);
                }

                Log.d(TAG,"remain last stock = "+remain);
                finish();
            }
        }
    }
}
