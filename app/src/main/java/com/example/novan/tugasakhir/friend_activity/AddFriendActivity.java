package com.example.novan.tugasakhir.friend_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.util.database.DataHelper;

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
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        circleImageView.setImageBitmap(decodedByte);

        text = (TextView) findViewById(R.id.id_name);
        text.setText(name);

        button = (Button) findViewById(R.id.confirm_friend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataHelper.addFriend(uid);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
