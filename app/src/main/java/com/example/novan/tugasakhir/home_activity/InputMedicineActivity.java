package com.example.novan.tugasakhir.home_activity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.DataHelper;

public class InputMedicineActivity extends AppCompatActivity {
    EditText input1, input2, input3, input4;
    public String layout;
    DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataHelper = new DataHelper(this);

        input1 = (EditText) findViewById(R.id.input_medicine);
        input2 = (EditText) findViewById(R.id.input_number_medicine);
        input3 = (EditText) findViewById(R.id.input_dosage_medicine);
        input4 = (EditText) findViewById(R.id.input_time_medicine);

        Button button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SQLiteDatabase db = dataHelper.getWritableDatabase();
            try{
                db.execSQL("insert into db_medic(name_medicine,amount_medicine,dosage_medicine,remain_medicine,count_medicine) value('"+ input1.getText().toString()+"','"+ input2.getText().toString()+"','"+ input3.getText().toString()+"','"+ input2.getText().toString()+"','"+ input4.getText().toString()+"','"+ "')");
            }catch (SQLException e){
                Toast.makeText(InputMedicineActivity.this, "data not inserted", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(InputMedicineActivity.this, MainActivity.class);
            startActivity(intent);
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
