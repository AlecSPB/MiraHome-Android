package com.mooring.mh.activity;

import android.content.Intent;
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
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.views.CustomImageView.ZoomCircleView;

/**
 * 主界面MainActivity
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView imgView_weather;
    private ImageView imgView_control;
    private ImageView imgView_parameter;
    private ImageView imgView_timing;

    private ImageView imgView_title_menu;
    private ZoomCircleView circleImg_left;
    private ZoomCircleView circleImg_right;
    private View title_layout;

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
    public int currentUser = MConstants.LEFT_USER;


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
        title_layout = findViewById(R.id.title_layout);

        circleImg_left = (ZoomCircleView) findViewById(R.id.circleImg_left);
        circleImg_right = (ZoomCircleView) findViewById(R.id.circleImg_right);
        imgView_title_menu = (ImageView) findViewById(R.id.imgView_title_menu);

        imgView_title_menu.setOnClickListener(this);
        circleImg_left.setOnClickListener(this);
        circleImg_right.setOnClickListener(this);

        imgView_weather.setOnClickListener(this);
        imgView_control.setOnClickListener(this);
        imgView_parameter.setOnClickListener(this);
        imgView_timing.setOnClickListener(this);

    }


    /**
     * @param index
     */
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

    /**
     * 隐藏所有fragment
     *
     * @param transaction
     */
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

    /**
     * 设置选中状态
     *
     * @param index
     */
    private void setTabSelectStatu(int index) {
        //恢复状态栏为透明底色
        title_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        imgView_weather.setImageResource(index != WEATHER ? R.mipmap.btn_weather_normal : R.mipmap.btn_weather_select);
        imgView_control.setImageResource(index != CONTROL ? R.mipmap.btn_control_normal : R.mipmap.btn_control_select);
        imgView_parameter.setImageResource(index != PARAMETER ? R.mipmap.btn_parameter_normal : R.mipmap.btn_parameter_select);
        imgView_timing.setImageResource(index != TIMING ? R.mipmap.btn_timing_normal : R.mipmap.btn_timing_select);

    }


    /**
     * 切换用户
     *
     * @param location LEFT_USER,RIGHT_USER
     */
    public void switchUser(int location) {
        switch (location) {
            case MConstants.LEFT_USER:
                //左边用户
                break;
            case MConstants.RIGHT_USER:
                //右边用户
                break;
        }
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
            case R.id.circleImg_left:
                circleImg_left.executeScale(2f);
                circleImg_right.executeScale(0.5f);
                if (currentUser == MConstants.RIGHT_USER) {
                    switchUser(MConstants.LEFT_USER);
                }
                break;
            case R.id.circleImg_right:
                circleImg_right.executeScale(2f);
                circleImg_left.executeScale(0.5f);
                if (currentUser == MConstants.LEFT_USER) {
                    switchUser(MConstants.RIGHT_USER);
                }
                break;

            case R.id.imgView_title_menu:
                Intent it = new Intent();
                it.setClass(MainActivity.this, MenuActivity.class);
                startActivityForResult(it, MConstants.MENU_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MConstants.MENU_REQUEST && resultCode == MConstants.MENU_RESULT) {
            //修改当前左右两边用户

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
