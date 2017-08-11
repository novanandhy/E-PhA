package com.example.novan.tugasakhir.information_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.database.AppConfig;
import com.squareup.picasso.Picasso;

public class InformationActivity extends AppCompatActivity {
    private TextView subtitle;
    private ImageView image_information;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subtitle = (TextView) findViewById(R.id.text_information_content);
        image_information = (ImageView) findViewById(R.id.image_information);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String sub = intent.getStringExtra("subtitle");
        String image = intent.getStringExtra("image");

        getSupportActionBar().setTitle(title);
        subtitle.setText(sub);

        Picasso.with(this).invalidate("http://"+ AppConfig.DOMAIN+"/android_api/upload/"+image);
        Picasso.with(this).load("http://"+AppConfig.DOMAIN+"/android_api/upload/"+image).into(image_information);
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
