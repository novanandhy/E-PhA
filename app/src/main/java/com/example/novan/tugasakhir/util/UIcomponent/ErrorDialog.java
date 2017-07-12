package com.example.novan.tugasakhir.util.UIcomponent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;

public class ErrorDialog extends Activity {
    Button btn;
    TextView msg;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_error);

        Intent intent = getIntent();
        String text = intent.getExtras().getString("message");

        btn = (Button) findViewById(R.id.button_close_dialog);
        msg = (TextView) findViewById(R.id.dialog_text_alert);
        img = (ImageView) findViewById(R.id.image_alert);

        setMessage(text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setMessage(String message){

        msg.setText(message);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }
}
