package com.example.novan.tugasakhir.home_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.util.DataHelper;

import java.util.ArrayList;
import java.util.Random;

public class InputMedicineActivity extends AppCompatActivity {
    EditText input1, input2, input3, input4;
    String name, uid;
    int amount, dosage, time;
    public String layout;
    DataHelper dataHelper;
    ArrayList<Medicine> medicines;
    String TAG = "TAGapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //create connection with database
        dataHelper = new DataHelper(this);

        //get all medicines
        medicines = dataHelper.getAllMedicine();

        //declaration of form
        input1 = (EditText) findViewById(R.id.input_medicine);
        input2 = (EditText) findViewById(R.id.input_number_medicine);
        input3 = (EditText) findViewById(R.id.input_dosage_medicine);
        input4 = (EditText) findViewById(R.id.input_time_medicine);

        //if submit button clicked
        Button button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //show toast if form not filled
            if (TextUtils.isEmpty(input1.getText()) || TextUtils.isEmpty(input2.getText()) ||
                    TextUtils.isEmpty(input3.getText()) || TextUtils.isEmpty(input4.getText())){
                alertCreator("please fil all form");
            }else if (Integer.parseInt(input2.getText().toString()) < Integer.parseInt(input3.getText().toString())){
                alertCreator("Dosage should be less than amount");
            }else {
                name = input1.getText().toString();
                amount = Integer.parseInt(input2.getText().toString());
                dosage = Integer.parseInt(input3.getText().toString());
                time = Integer.parseInt(input4.getText().toString());
                uid = random();
                Log.d(TAG,"uid = "+uid);

                //insert into database
                try{
                    if(isNameExist(name)==false){
                        Medicine medicine = dataHelper.save_medicine(uid, name,amount,dosage,amount,time);
                        Intent intent = new Intent();
                        intent.putExtra("medicine",medicine);
                        intent.putExtra("h",10);
                        setResult(RESULT_OK,intent);
                        finish();
                    }else{
                        alertCreator("this user is already exists");
                    }

                }catch (SQLException e){
                    e.printStackTrace();
                    alertCreator("sorry, data not inserted");
                    return;
                }
            }
        }});
    }

    private boolean isNameExist(String name) {
        int count = 0;
        for (int i = 0 ; i < medicines.size() ; i++){
            if (medicines.get(i).getMedicine_name().equalsIgnoreCase(name)){
                count++;
            }
        }if(count>0){
            return true;
        }else{
            return false;
        }
    }

    private void alertCreator(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static String random() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
