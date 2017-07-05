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
import com.example.novan.tugasakhir.util.database.DataHelper;

public class ContactActivity extends AppCompatActivity {
    FragmentManager mFragmentManager;
    private DataHelper dataHelper;
    private TextView number;
    private String TAG="TAGapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // identifying database helper
        dataHelper = new DataHelper(this);
        number = (TextView) findViewById(R.id.contact_number_preview);
        number.setText(getIntent().getExtras().getString("number"));

        getSupportActionBar().setTitle(getIntent().getExtras().getString("name"));

        mFragmentManager = getSupportFragmentManager();

        Button button = (Button) findViewById(R.id.button_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * create alert of deleting contact
                 */
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactActivity.this);
                alertDialogBuilder.setMessage("Ingin menghapus kontak ini ?");
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataHelper.delete_contact((Integer) getIntent().getSerializableExtra("id"));
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
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
