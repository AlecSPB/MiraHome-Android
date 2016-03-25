package com.mooring.mh.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.mooring.mh.R;
import com.mooring.mh.fragment.ControlFragment;
import com.mooring.mh.fragment.ParameterFragment;
import com.mooring.mh.fragment.TimingFragment;
import com.mooring.mh.fragment.WeatherFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView imgView_weather;
    private ImageView imgView_control;
    private ImageView imgView_parameter;
    private ImageView imgView_timing;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private WeatherFragment weatherFragment;
    private ControlFragment controlFragment;
    private ParameterFragment parameterFragment;
    private TimingFragment timingFragment;

    public final int WEATHER = 1;
    public final int CONTROL = 2;
    public final int PARAMETER = 3;
    public final int TIMING = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        fragmentManager = getSupportFragmentManager();

        setTabSelection(WEATHER);

    }

    private void initView() {
        imgView_weather = (ImageView) findViewById(R.id.imgView_weather);
        imgView_control = (ImageView) findViewById(R.id.imgView_control);
        imgView_parameter = (ImageView) findViewById(R.id.imgView_parameter);
        imgView_timing = (ImageView) findViewById(R.id.imgView_timing);

        imgView_weather.setOnClickListener(this);
        imgView_control.setOnClickListener(this);
        imgView_parameter.setOnClickListener(this);
        imgView_timing.setOnClickListener(this);

    }


    private void setTabSelection(int index) {
        fragmentTransaction = fragmentManager.beginTransaction();
        setTabSelectStatu(index);
        hideFragments(fragmentTransaction);
        switch (index) {
            case WEATHER:
                if (weatherFragment == null) {
                    weatherFragment = new WeatherFragment();
                    fragmentTransaction.add(R.id.main_container, weatherFragment, "WeatherFragment");
                } else {
                    fragmentTransaction.show(weatherFragment);
                }
                break;
            case CONTROL:
                if (controlFragment == null) {
                    controlFragment = new ControlFragment();
                    fragmentTransaction.add(R.id.main_container, controlFragment, "ControlFragment");
                } else {
                    fragmentTransaction.show(controlFragment);
                }
                break;
            case PARAMETER:
                if (parameterFragment == null) {
                    parameterFragment = new ParameterFragment();
                    fragmentTransaction.add(R.id.main_container, parameterFragment, "ParameterFragment");
                } else {
                    fragmentTransaction.show(parameterFragment);
                }
                break;
            case TIMING:
                if (timingFragment == null) {
                    timingFragment = new TimingFragment();
                    fragmentTransaction.add(R.id.main_container, timingFragment, "TimingFragment");
                } else {
                    fragmentTransaction.show(timingFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (weatherFragment != null) {
            transaction.hide(weatherFragment);
        }
        if (controlFragment != null) {
            transaction.hide(controlFragment);
        }
        if (parameterFragment != null) {
            transaction.hide(parameterFragment);
        }
        if (timingFragment != null) {
            transaction.hide(timingFragment);
        }
    }

    private void setTabSelectStatu(int index) {

        imgView_weather.setImageResource(index != WEATHER ? R.mipmap.btn_weather_normal : R.mipmap.btn_weather_select);
        imgView_control.setImageResource(index != CONTROL ? R.mipmap.btn_control_normal : R.mipmap.btn_control_select);
        imgView_parameter.setImageResource(index != PARAMETER ? R.mipmap.btn_parameter_normal : R.mipmap.btn_parameter_select);
        imgView_timing.setImageResource(index != TIMING ? R.mipmap.btn_timing_normal : R.mipmap.btn_timing_select);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgView_weather:
                setTabSelection(WEATHER);
                break;
            case R.id.imgView_control:
                setTabSelection(CONTROL);
                break;
            case R.id.imgView_parameter:
                setTabSelection(PARAMETER);
                break;
            case R.id.imgView_timing:
                setTabSelection(TIMING);
                break;
        }
    }


}
