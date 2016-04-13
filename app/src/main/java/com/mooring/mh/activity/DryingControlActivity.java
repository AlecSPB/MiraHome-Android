package com.mooring.mh.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.views.CircleProgress.DryingCircleView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Will on 16/4/12.
 */
public class DryingControlActivity extends AppCompatActivity {

    private DryingCircleView drying;
    private Button button;
    private Button button1;
    private TextView tv_drying_times;


    private int time = 0;
    private Timer timer;
    private TimerTask timerTask;

    private long times = 30 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drying);

        drying = (DryingCircleView) findViewById(R.id.drying);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.schedule(timerTask, 0, 1000);
                drying.setAnimDuration(times);
                drying.startAnim();
            }
        });

        button1 = (Button) findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                drying.pauseAnim();
            }
        });

        tv_drying_times = (TextView) findViewById(R.id.tv_drying_times);

//        tv_drying_times.setText();

        initTimer();
    }

    private void Countdown() {

    }

    /**
     * 发送消息
     */
    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv_drying_times.setText(calculateTimes(times - time * 1000));
            time++;
            if (time >= 30 * 60) {

                //加载完毕执行重置
                drying.resetView();

                clearTimer();
                time = 0;
            }
        }
    };

    private String calculateTimes(long times) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return sdf.format(times);
    }

    /**
     * 初始化计时
     */
    private void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                han.sendEmptyMessage(0x01);
            }
        };
    }

    /**
     * 清除计时
     */
    private void clearTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearTimer();
    }
}
