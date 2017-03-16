package com.example.novan.tugasakhir.home_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;

import java.util.ArrayList;
import java.util.List;

public class InputMedicineActivity extends AppCompatActivity {
    EditText input1, input2, input3, input4;
    public String layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_medicine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        input1 = (EditText) findViewById(R.id.input_medicine);
        input2 = (EditText) findViewById(R.id.input_number_medicine);
        input3 = (EditText) findViewById(R.id.input_dosage_medicine);
        input4 = (EditText) findViewById(R.id.input_time_medicine);

        Button button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
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
