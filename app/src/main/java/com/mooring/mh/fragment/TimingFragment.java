package com.mooring.mh.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mooring.mh.R;
import com.mooring.mh.views.other.ArcProgress;
import com.mooring.mh.views.other.CircleProgress;
import com.mooring.mh.views.other.DonutProgress;
import com.mooring.mh.views.other.WeatherView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Will on 16/3/24.
 */
public class TimingFragment extends BaseFragment {
    private ImageView imgView_cloud;
    private ImageView imgView_cloud2;
    private ImageView imgView_cloud3;
    private ImageView imgView_cloud4;


    private Timer timer;
    private TimerTask timerTask;
    private DonutProgress donutProgress;
    private CircleProgress circleProgress;
    private ArcProgress arcProgress;
    WeatherView rain;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timing;
    }

    int rainNum = WeatherView.LIGHT_SNOW;

    @Override
    protected void initView() {
//
        rain = (WeatherView) rootView.findViewById(R.id.rain);
//        rain.setZOrderOnTop(true);
//        rain.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        rain.startRain();


        Button btn_switch = (Button) rootView.findViewById(R.id.btn_switch);
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainNum++;
                rain.switchWeather(rain.SHOWER_RAIN);
            }
        });


//        imgView_cloud = (ImageView) rootView.findViewById(R.id.imgView_cloud);
//        imgView_cloud2 = (ImageView) rootView.findViewById(R.id.imgView_cloud2);
//        imgView_cloud3 = (ImageView) rootView.findViewById(R.id.imgView_cloud3);
//        imgView_cloud4 = (ImageView) rootView.findViewById(R.id.imgView_cloud4);
//
//        TranslateAnimation translateAnimation = new TranslateAnimation(0, -50, 0, 0);
//        translateAnimation.setInterpolator(new OvershootInterpolator());
//        translateAnimation.setDuration(3000);
//        translateAnimation.setRepeatCount(-1);
//        translateAnimation.setRepeatMode(Animation.REVERSE);
//
//
//        TranslateAnimation translateAnimation2 = new TranslateAnimation(-50, 0, 0, 0);
////        translateAnimation2.setInterpolator(new OvershootInterpolator());
//        translateAnimation2.setDuration(3000);
//        translateAnimation2.setRepeatCount(-1);
//        translateAnimation2.setRepeatMode(Animation.REVERSE);
//        imgView_cloud.startAnimation(translateAnimation);
//        imgView_cloud3.startAnimation(translateAnimation);
//
//
//        imgView_cloud2.startAnimation(translateAnimation2);
//        imgView_cloud4.startAnimation(translateAnimation2);
//
//
//        donutProgress = (DonutProgress) rootView.findViewById(R.id.donut_progress);
//        circleProgress = (CircleProgress) rootView.findViewById(R.id.circle_progress);
//        arcProgress = (ArcProgress) rootView.findViewById(R.id.arc_progress);
//
//        timer = new Timer();
//
////        timer.schedule(new TimerTask() {
////            @Override
////            public void run() {
////                donutProgress.setProgress(donutProgress.getProgress() + 1);
////                circleProgress.setProgress(circleProgress.getProgress() + 1);
////                arcProgress.setProgress(arcProgress.getProgress() + 1);
////            }
////        }, 1000, 100);
//
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                han.sendEmptyMessage(0x01);
//            }
//        };
//
//        timer.schedule(timerTask, 1000, 100);


    }

//    Handler han = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            donutProgress.setProgress(donutProgress.getProgress() + 1);
//            circleProgress.setProgress(circleProgress.getProgress() + 1);
//            arcProgress.setProgress(arcProgress.getProgress() + 1);
//        }
//    };
//
//    private void clearTimer() {
//        if (timerTask != null) {
//            timerTask.cancel();
//            timerTask = null;
//        }
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        clearTimer();
    }

    @Override
    protected void lazyLoad() {
//判断用户是否以切换
    }

    @Override
    public void onPause() {
        super.onPause();
//        rain.onPauseThread();
    }

    @Override
    public void onResume() {
        super.onResume();
//        rain.onRestartThread();
    }
}
