package com.example.novan.tugasakhir.emergency_activity;

import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.emergency_activity.SendMessage;

public class CountDown extends AppCompatActivity implements View.OnClickListener{

    private TextView countdown;
    private Button ok;

    private CountDownTimer countDownTimer;

    MediaPlayer ringtone;

    SendMessage sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_count_down);

        countdown = (TextView) findViewById(R.id.dialog_countdown);
        ok = (Button) findViewById(R.id.ok_countdown);

        sendMessage = new SendMessage(this);


        //get resource media to play ringtone
        ringtone = MediaPlayer.create(this, R.raw.alarm);
        ringtone.start();

        ok.setOnClickListener(this);

        countdown.setText("10");
        countDownTimer = new CountDownTimer(10*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(""+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                countdown.setText("Done");
                ringtone.stop();
                sendMessage.sendSMSByManager();
                finish();
            }
        };
        countDownTimer.start();

    }

    @Override
    public void onClick(View v) {
        if(v == ok) {
            ringtone.stop();
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {    }
}
