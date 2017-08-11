package com.example.novan.tugasakhir.friend_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.database.AppConfig;
import com.example.novan.tugasakhir.util.database.DataHelper;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriendActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView text;
    Button button;
    DataHelper dataHelper;

    private Context context;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        context = this;
        TAG = "TAGapp "+context.getClass().getSimpleName();

        dataHelper = new DataHelper(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        final String uid = intent.getStringExtra("uid");
        String image = intent.getStringExtra("image");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        circleImageView = (CircleImageView) findViewById(R.id.image_add_firend);
        Picasso.with(context).invalidate("http://"+ AppConfig.DOMAIN+"/android_api/upload/"+image);
        Picasso.with(context).load("http://"+AppConfig.DOMAIN+"/android_api/upload/"+image).into(circleImageView);

        text = (TextView) findViewById(R.id.id_name);
        text.setText(name);

        button = (Button) findViewById(R.id.confirm_friend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataHelper.addFriend(uid);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
