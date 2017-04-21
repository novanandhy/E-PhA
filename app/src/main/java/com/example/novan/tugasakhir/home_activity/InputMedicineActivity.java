package com.example.novan.tugasakhir.home_activity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.util.DataHelper;

public class InputMedicineActivity extends AppCompatActivity {
    EditText input1, input2, input3, input4;
    String name;
    int amount, dosage, time;
    public String layout;
    DataHelper dataHelper;
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
            if (TextUtils.isEmpty(input1.getText()) || TextUtils.isEmpty(input2.getText()) || TextUtils.isEmpty(input3.getText()) || TextUtils.isEmpty(input4.getText())){
                Toast.makeText(InputMedicineActivity.this,"fill all form", Toast.LENGTH_SHORT).show();
            } else {
                name = input1.getText().toString();
                amount = Integer.parseInt(input2.getText().toString());
                dosage = Integer.parseInt(input3.getText().toString());
                time = Integer.parseInt(input4.getText().toString());

                //insert into database
                try{
                    Medicine medicine = dataHelper.save_medicine(name,amount,dosage,amount,time);
                    Intent intent = new Intent();
                    intent.putExtra("medicine",medicine);
                    intent.putExtra("h",10);
                    setResult(RESULT_OK,intent);
                    finish();
                }catch (SQLException e){
                    e.printStackTrace();
                    Toast.makeText(InputMedicineActivity.this, "data not inserted", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }});
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
