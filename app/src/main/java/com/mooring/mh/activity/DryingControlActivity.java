package com.mooring.mh.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.views.CircleProgress.DryingCircleView;
import com.mooring.mh.views.CommonDialog;
import com.mooring.mh.views.CustomToggle;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Will on 16/4/12.
 */
public class DryingControlActivity extends BaseActivity {

    private DryingCircleView drying;
    private TextView tv_drying_times;
    private CustomToggle toggle_drying;

    private int time = 0;
    private Timer timer;
    private TimerTask timerTask;

    private long times = 30 * 60 * 1000;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_drying_control;
    }

    @Override
    protected String getTitleName() {
        return "Drying";
    }

    @Override
    protected void initActivity() {


        drying = (DryingCircleView) findViewById(R.id.drying);
        tv_drying_times = (TextView) findViewById(R.id.tv_drying_times);
        toggle_drying = (CustomToggle) findViewById(R.id.toggle_drying);
        if (toggle_drying != null)
            toggle_drying.setChecked(false);

        toggle_drying.setOnCheckedChange(new CustomToggle.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View v, boolean isChecked) {
                if (isChecked) {
                    toggle_drying.setChecked(true);
                    initTimer();
                    timer.schedule(timerTask, 0, 1000);
//                    drying.setAnimDuration(times);
//                    drying.startAnim();

                    drying.setAnimDuration(10 * 60 * 1000);
                    float tt = (float)20/30;
                    drying.startAnim(tt);
                    time = 20*60;

                } else {
                    //弹出提示
                    showDialog();
                }
            }
        });

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

    /**
     * 格式化时间显示
     *
     * @param times
     * @return
     */
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


    /**
     * show提示dialog
     */
    private void showDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage("stop drying?");
        builder.setLogo(R.drawable.img_close_drying);
        builder.setCanceledOnTouchOtherPlace(false);
        builder.setPositiveButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drying.resetView();
                clearTimer();
                tv_drying_times.setText("00:00:00");
                time = 0;
                toggle_drying.setChecked(false);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
