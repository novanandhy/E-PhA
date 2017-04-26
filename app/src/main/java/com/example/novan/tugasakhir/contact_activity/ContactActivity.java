package com.example.novan.tugasakhir.contact_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.models.Contact;
import com.example.novan.tugasakhir.util.DataHelper;

public class ContactActivity extends AppCompatActivity {
    FragmentManager mFragmentManager;
    int homeFragmentIdentifier;
    private DataHelper dataHelper;
    private Contact contact;
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
        

        Log.d(TAG,"id= "+getIntent().getExtras().getString("id"));

        name = (TextView) findViewById(R.id.contact_name_preview);
        name.setText(getIntent().getExtras().getString("name"));
        number = (TextView) findViewById(R.id.contact_number_preview);
        number.setText(getIntent().getExtras().getString("number"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_contact_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, EditContactActivity.class);
                startActivity(intent);
            }
        });

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
                        Log.d(TAG,"id ="+getIntent().getExtras().getString("id"));
                    }
                });
                alertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ContactActivity.this,"no clicked",Toast.LENGTH_SHORT).show();
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
