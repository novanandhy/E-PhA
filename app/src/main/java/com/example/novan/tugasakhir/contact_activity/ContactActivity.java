package com.example.novan.tugasakhir.contact_activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.DataHelper;

public class ContactActivity extends AppCompatActivity {
    FragmentManager mFragmentManager;
    private DataHelper dataHelper;
    private TextView name, number;
    private String TAG="TAGapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataHelper = new DataHelper(this);

        name = (TextView) findViewById(R.id.contact_name_preview);
        name.setText(getIntent().getExtras().getString("name"));
        number = (TextView) findViewById(R.id.contact_number_preview);
        number.setText(getIntent().getExtras().getString("number"));

        mFragmentManager = getSupportFragmentManager();

        Button button = (Button) findViewById(R.id.button_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactActivity.this);
                alertDialogBuilder.setMessage("Are you sure to remove this data ?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataHelper.delete_contact((Integer) getIntent().getSerializableExtra("id"));
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
            case R.id.action_remove_contact:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
