package com.mooring.mh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.mooring.mh.R;
import com.mooring.mh.db.DbXUtils;
import com.mooring.mh.db.User;
import com.mooring.mh.fragment.ControlFragment;
import com.mooring.mh.fragment.ParameterFragment;
import com.mooring.mh.fragment.TimingFragment;
import com.mooring.mh.fragment.WeatherFragment;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.views.CustomImageView.CircleImageView;
import com.mooring.mh.views.CustomImageView.ZoomCircleView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

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
    private CircleImageView circleImg_middle;
    private View layout_two_user;
    private View title_layout;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private WeatherFragment weatherFragment;  //fragment 1
    private ControlFragment controlFragment;  //fragment 2
    private ParameterFragment parameterFragment;  //fragment 3
    private TimingFragment timingFragment;  //fragment 4

    public final int WEATHER = 1;
    public final int CONTROL = 2;
    public final int PARAMETER = 3;
    public final int TIMING = 4;
    private int currLocation = MConstants.LEFT_USER;//当前用户的位置
    private User currUser;//当前展示User
    private List<User> currentUsers;//存放当前需要展现的用户,一个或者两个
    private DbManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        fragmentManager = getSupportFragmentManager();

        setTabSelection(CONTROL);

        DbManager.DaoConfig dao = DbXUtils.getDaoConfig(this);
        dbManager = x.getDb(dao);

    }

    private void initView() {
        imgView_weather = (ImageView) findViewById(R.id.imgView_weather);
        imgView_control = (ImageView) findViewById(R.id.imgView_control);
        imgView_parameter = (ImageView) findViewById(R.id.imgView_parameter);
        imgView_timing = (ImageView) findViewById(R.id.imgView_timing);
        title_layout = findViewById(R.id.title_layout);

        circleImg_left = (ZoomCircleView) findViewById(R.id.circleImg_left);
        circleImg_right = (ZoomCircleView) findViewById(R.id.circleImg_right);
        circleImg_middle = (CircleImageView) findViewById(R.id.circleImg_middle);
        layout_two_user = findViewById(R.id.layout_two_user);
        imgView_title_menu = (ImageView) findViewById(R.id.imgView_title_menu);

        imgView_title_menu.setOnClickListener(this);
        circleImg_left.setOnClickListener(this);
        circleImg_right.setOnClickListener(this);

        imgView_weather.setOnClickListener(this);
        imgView_control.setOnClickListener(this);
        imgView_parameter.setOnClickListener(this);
        imgView_timing.setOnClickListener(this);

        if (computeCurrentUsers() == 2) {
            circleImg_middle.setVisibility(View.GONE);
            layout_two_user.setVisibility(View.VISIBLE);
            circleImg_left.setOnClickListener(this);
            circleImg_right.setOnClickListener(this);
        } else {
            circleImg_middle.setVisibility(View.VISIBLE);
            layout_two_user.setVisibility(View.GONE);
        }

    }

    /**
     * @param index
     */
    private void setTabSelection(int index) {
        fragmentTransaction = fragmentManager.beginTransaction();

        hideFragments(fragmentTransaction);
        switch (index) {
            case WEATHER:
                if (weatherFragment == null) {
                    weatherFragment = new WeatherFragment();
                    fragmentTransaction.add(R.id.main_container, weatherFragment, "WeatherFragment");
                }
                fragmentTransaction.show(weatherFragment);
                break;
            case CONTROL:
                if (controlFragment == null) {
                    controlFragment = new ControlFragment();
                    fragmentTransaction.add(R.id.main_container, controlFragment, "ControlFragment");
                }
                fragmentTransaction.show(controlFragment);
                break;
            case PARAMETER:
                if (parameterFragment == null) {
                    parameterFragment = new ParameterFragment();
                    fragmentTransaction.add(R.id.main_container, parameterFragment, "ParameterFragment");
                }
                fragmentTransaction.show(parameterFragment);
                break;
            case TIMING:
                if (timingFragment == null) {
                    timingFragment = new TimingFragment();
                    fragmentTransaction.add(R.id.main_container, timingFragment, "TimingFragment");
                }
                fragmentTransaction.show(timingFragment);
                break;
        }
        fragmentTransaction.commit();

        setTabSelectStatu(index);
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

    /**
     * 根据设备的类型{单人垫,双人垫},以及现在所拥有的用户个数
     * <p/>
     * 计算当前用户个数
     */
    public int computeCurrentUsers() {

//        try {
//            currentUsers = dbManager.selector(User.class).where("_location", "in", new int[]{1, 2, 3}).findAll();
//        } catch (DbException e) {
//            e.printStackTrace();
//        }

        //单人垫默认发送一个人

        //双人垫发送当前支持的用户

        return 2;
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public User getCurrentUser() {
        return this.currUser;
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
                if (currLocation == MConstants.RIGHT_USER) {
                    switchUser(MConstants.LEFT_USER);
                }
                break;
            case R.id.circleImg_right:
                circleImg_right.executeScale(2f);
                circleImg_left.executeScale(0.5f);
                if (currLocation == MConstants.LEFT_USER) {
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
