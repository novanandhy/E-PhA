package com.example.novan.tugasakhir.home_activity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.util.DataHelper;

public class EditMedineActivity extends AppCompatActivity {
    EditText input1,input2,input3,input4;
    private Medicine medicine;
    String name;
    int amount, dosage, time, id, remain;
    DataHelper dataHelper;
    String TAG = "TAGapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        medicine = getIntent().getParcelableExtra("medicine");
        id = getIntent().getIntExtra("id",0);
        dataHelper = new DataHelper(this);

        input1 = (EditText) findViewById(R.id.edit_medicine);
        input2 = (EditText) findViewById(R.id.edit_number_medicine);
        input3 = (EditText) findViewById(R.id.edit_dosage_medicine);
        input4 = (EditText) findViewById(R.id.edit_time_medicine);

//        SetText to form
        input1.setText(""+medicine.getMedicine_name());
        input2.setText(""+medicine.getAmount());
        input3.setText(""+medicine.getDosage());
        input4.setText(""+medicine.getCount());

        Button button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(input1.getText()) || TextUtils.isEmpty(input2.getText()) || TextUtils.isEmpty(input3.getText()) || TextUtils.isEmpty(input4.getText())){
                    Toast.makeText(EditMedineActivity.this,"fill all form", Toast.LENGTH_SHORT).show();
                } else {
                    name = input1.getText().toString();
                    amount = Integer.parseInt(input2.getText().toString());
                    dosage = Integer.parseInt(input3.getText().toString());
                    time = Integer.parseInt(input4.getText().toString());
                    remain = medicine.getRemain();

                    //insert into database
                    try{
                        //try Log
                        Medicine newMedicine = dataHelper.update_medicine(id,name,amount,dosage,remain,time); //update data medicine
                        dataHelper.update_schedule_name(medicine.getUid(), name); //update data schedule
                        Intent intent = new Intent();
                        intent.putExtra("medicine",newMedicine);
                        setResult(RESULT_OK,intent);
                        finish();
                    }catch (SQLException e){
                        e.printStackTrace();
                        Toast.makeText(EditMedineActivity.this, "data not inserted", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });
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
