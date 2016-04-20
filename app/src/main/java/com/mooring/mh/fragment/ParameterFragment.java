package com.mooring.mh.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.activity.ParameterDetailActivity;
import com.mooring.mh.views.other.WeatherView;

/**
 * Created by Will on 16/3/24.
 */
public class ParameterFragment extends BaseFragment implements View.OnClickListener {

    private WeatherView giftRainView;
    private boolean isStart;

    private View layout_heart_rate;
    private View layout_breathing_rate;
    private View layout_body_movement;
    private View layout_humidity;
    private View layout_temperature;
    private View layout_bed_temperature;
    private View layout_light;
    private View layout_noise;

    private TextView tv_heart_rate;
    private TextView tv_breathing_rate;
    private TextView tv_body_movement;
    private TextView tv_humidity;
    private TextView tv_temperature;
    private TextView tv_bed_temperature;
    private TextView tv_light;
    private TextView tv_noise;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_parameter;
    }

    @Override
    protected void initView() {
//        giftRainView = (GiftRainView) rootView.findViewById(R.id.giftRainView);
//
//
//        giftRainView.setImages(R.mipmap.ico_gold_money, R.mipmap.ico_money, R.mipmap.ic_launcher);
//
//        giftRainView.startRain();
//        isStart = true;
//
//        giftRainView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isStart) {
//                    giftRainView.startRain();
//                    isStart = true;
//                } else {
//                    giftRainView.stopRainDely();
//                    isStart = false;
//                }
//            }
//        });

        layout_heart_rate = rootView.findViewById(R.id.layout_heart_rate);
        layout_breathing_rate = rootView.findViewById(R.id.layout_breathing_rate);
        layout_body_movement = rootView.findViewById(R.id.layout_body_movement);
        layout_humidity = rootView.findViewById(R.id.layout_humidity);
        layout_temperature = rootView.findViewById(R.id.layout_temperature);
        layout_bed_temperature = rootView.findViewById(R.id.layout_bed_temperature);
        layout_light = rootView.findViewById(R.id.layout_light);
        layout_noise = rootView.findViewById(R.id.layout_noise);

        layout_heart_rate.setOnClickListener(this);
        layout_breathing_rate.setOnClickListener(this);
        layout_body_movement.setOnClickListener(this);
        layout_humidity.setOnClickListener(this);
        layout_temperature.setOnClickListener(this);
        layout_bed_temperature.setOnClickListener(this);
        layout_light.setOnClickListener(this);
        layout_noise.setOnClickListener(this);

        tv_heart_rate = (TextView) rootView.findViewById(R.id.tv_heart_rate);
        tv_breathing_rate = (TextView) rootView.findViewById(R.id.tv_breathing_rate);
        tv_body_movement = (TextView) rootView.findViewById(R.id.tv_body_movement);
        tv_humidity = (TextView) rootView.findViewById(R.id.tv_humidity);
        tv_temperature = (TextView) rootView.findViewById(R.id.tv_temperature);
        tv_bed_temperature = (TextView) rootView.findViewById(R.id.tv_bed_temperature);
        tv_light = (TextView) rootView.findViewById(R.id.tv_light);
        tv_noise = (TextView) rootView.findViewById(R.id.tv_noise);


    }


    @Override
    protected void lazyLoad() {

        //判断用户是否以切换


    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent();
        it.setClass(getActivity(), ParameterDetailActivity.class);
        switch (v.getId()) {
            case R.id.layout_heart_rate:
                it.putExtra("number", 0);
                it.putExtra("param", "88");
                it.putExtra("title", "Heart rate");
                break;
            case R.id.layout_breathing_rate:
                it.putExtra("number", 1);
                it.putExtra("param", "88");
                it.putExtra("title", "Breathing rate");
                break;
            case R.id.layout_body_movement:
                it.putExtra("number", 2);
                it.putExtra("param", "88");
                it.putExtra("title", "Body movement");
                break;
            case R.id.layout_humidity:
                it.putExtra("number", 3);
                it.putExtra("param", "88");
                it.putExtra("title", "Humidity");
                break;
            case R.id.layout_temperature:
                it.putExtra("number", 4);
                it.putExtra("param", "88");
                it.putExtra("title", "Temperature");
                break;
            case R.id.layout_bed_temperature:
                it.putExtra("number", 5);
                it.putExtra("param", "88");
                it.putExtra("title", "Bed temperature");
                break;
            case R.id.layout_light:
                it.putExtra("number", 6);
                it.putExtra("param", "LOW");
                it.putExtra("title", "Light");
                break;
            case R.id.layout_noise:
                it.putExtra("number", 7);
                it.putExtra("param", "LOW");
                it.putExtra("title", "Noise");
                break;
        }
        getActivity().startActivity(it);
    }
}
