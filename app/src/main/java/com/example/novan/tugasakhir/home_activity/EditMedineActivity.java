package com.example.novan.tugasakhir.home_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.novan.tugasakhir.MainActivity;
import com.example.novan.tugasakhir.R;

public class EditMedineActivity extends AppCompatActivity {
    EditText input1,input2,input3,input4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        input1 = (EditText) findViewById(R.id.edit_medicine);
        input2 = (EditText) findViewById(R.id.edit_number_medicine);
        input3 = (EditText) findViewById(R.id.edit_dosage_medicine);
        input4 = (EditText) findViewById(R.id.edit_time_medicine);

        Button button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MedicineActivity.class);
                startActivity(intent);
            }
        });
    }

}
