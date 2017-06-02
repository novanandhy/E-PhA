package com.example.novan.tugasakhir.util.countDownComponent;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.novan.tugasakhir.R;
import com.example.novan.tugasakhir.emergency_activity.SendMessage;

public class CountDown extends AppCompatActivity implements View.OnClickListener{

    private TextView countdown;
    private Button ok, cancel;

    private CountDownTimer countDownTimer;

    SendMessage sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_count_down);

        countdown = (TextView) findViewById(R.id.dialog_countdown);
        ok = (Button) findViewById(R.id.ok_countdown);
        cancel = (Button) findViewById(R.id.cancel_countdown);

        sendMessage = new SendMessage(this);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

        countdown.setText("10");
        countDownTimer = new CountDownTimer(10*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(""+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                countdown.setText("Done");
            }
        };
        countDownTimer.start();

    }

    @Override
    public void onClick(View v) {
        if(v == ok){
            finish();
        }else{
            sendMessage.sendSMSByManager();
        }
    }
}
