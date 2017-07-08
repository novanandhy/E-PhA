package com.example.novan.tugasakhir.emergency_activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.tutorial_activity.TutorialActivity;

public class CountDown extends Activity implements View.OnClickListener{

    private TextView countdown;
    private Button ok;

    private CountDownTimer countDownTimer;

    MediaPlayer ringtone;

    SendMessage sendMessage;

    private Context context;

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_count_down);

        context = this;

        TAG = "TAGapp "+context.getClass().getSimpleName();

        countdown = (TextView) findViewById(R.id.dialog_countdown);
        ok = (Button) findViewById(R.id.ok_countdown);

        sendMessage = new SendMessage(this);

        //get resource media to play ringtone
        ringtone = MediaPlayer.create(this, R.raw.alarm);
        ringtone.start();

        ok.setOnClickListener(this);

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(this.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(this.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        countdown.setText("10");
        countDownTimer = new CountDownTimer(10*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(""+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                countdown.setText("Selesai");
                ringtone.stop();
                sendMessage.sendSMSByManager();
                Intent intent = new Intent(CountDown.this, TutorialActivity.class);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();

    }

    @Override
    public void onClick(View v) {
        if(v == ok) {
            ringtone.stop();
            countDownTimer.cancel();
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {}
}
