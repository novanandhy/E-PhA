package com.example.novan.tugasakhir.util.UIcomponent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Novan on 21/07/2017.
 */

public class TutorialDialog extends Activity {
    Button btn;
    TextView title, description;
    GifImageView gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tutorial);

        Intent intent = getIntent();
        final String text = intent.getExtras().getString("title");
        final String desc = intent.getExtras().getString("description");
        final int image = intent.getIntExtra("image",0);

        btn = (Button) findViewById(R.id.button_close_dialog);
        title = (TextView) findViewById(R.id.tutorial_title);
        description = (TextView) findViewById(R.id.description_tutorial);
        gif = (GifImageView) findViewById(R.id.gifImageView);

        gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMessage(text, desc, image);
            }
        });

        setMessage(text, desc, image);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setMessage(String text, String message, int image){

        title.setText(text);
        description.setText(message);
        gif.setImageResource(image);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
