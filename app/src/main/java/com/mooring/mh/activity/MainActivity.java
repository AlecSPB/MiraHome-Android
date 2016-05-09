package com.mooring.mh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.mooring.mh.R;
import com.mooring.mh.adapter.UserListAdapter;
import com.mooring.mh.db.DbXUtils;
import com.mooring.mh.db.User;
import com.mooring.mh.fragment.ControlFragment;
import com.mooring.mh.fragment.ParameterFragment;
import com.mooring.mh.fragment.TimingFragment;
import com.mooring.mh.fragment.WeatherFragment;
import com.mooring.mh.model.UserHeadInfo;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.views.CustomImageView.CircleImageView;
import com.mooring.mh.views.CustomImageView.ZoomCircleView;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面MainActivity
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView imgView_weather;//tab-天气
    private ImageView imgView_control;//tab-控制
    private ImageView imgView_parameter;//tab-参数
    private ImageView imgView_timing;//tab-闹钟

    private ImageView imgView_title_menu;//菜单
    private ZoomCircleView circleImg_left;//左边用户
    private ZoomCircleView circleImg_right;//右边用户
    private CircleImageView circleImg_middle;//单个用户
    public ImageView imgView_title_plus;//添加闹钟
    private View layout_two_user;//两个用户的布局
    private View title_layout;//title布局

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private WeatherFragment weatherFragment;  //WeatherFragment
    private ControlFragment controlFragment;  //ControlFragment
    private ParameterFragment parameterFragment;  //ParameterFragment
    private TimingFragment timingFragment;  //TimingFragment

    public final int WEATHER = 1;
    public final int CONTROL = 2;
    public final int PARAMETER = 3;
    public final int TIMING = 4;
    private int currLocation = MConstants.LEFT_USER;//当前用户的位置
    private User currUser;//当前展示User
    private List<User> currentUsers;//存放当前需要展现的用户,一个或者两个
    private DbManager dbManager;
    private OnSwitchUserListener listener;
    /**
     * ---------侧边滑动栏相关--------
     */
    private DrawerLayout mDrawerLayout;//侧边menu可滑动布局
    private View activity_menu;//侧边menu整个布局,帮助消费点击事件
    private RecyclerView menu_recyclerView;//横向滑动view
    private ImageView imgView_switch_user;//切换用户按钮
    private View layout_connect_mooring;//重新连接view
    private ImageView imgView_to_connect;//点击链接图片
    private View layout_connected_device;//一连上设备
    private ZoomCircleView zcView_left; // 左边用户头像
    private ImageView imgView_delete_left;//左边删除用户图标
    private ZoomCircleView zcView_right;//右边用户头像
    private ImageView imgView_delete_right;//右边删除用户图标
    private View layout_device; // device设备连接状态整个布局
    private TextView tv_not_connected;//device设备连接状态
    private TextView tv_connect_health; // 链接Health kit
    private TextView tv_about_text; // 关于我们
    private TextView tv_suggestions_text;// 建议
    private TextView tv_login_out;//退出登陆

    private RecyclerView.LayoutManager layoutManager;//横向滑动用户列表布局
    private List<UserHeadInfo> dataList;//用户list
    private UserListAdapter adapter;//横向滑动适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        initView();

        initData();

        setTabSelection(CONTROL);

        DbManager.DaoConfig dao = DbXUtils.getDaoConfig(this);
        dbManager = x.getDb(dao);

    }

    /**
     * 初始化
     */
    private void initView() {
        imgView_weather = (ImageView) findViewById(R.id.imgView_weather);
        imgView_control = (ImageView) findViewById(R.id.imgView_control);
        imgView_parameter = (ImageView) findViewById(R.id.imgView_parameter);
        imgView_timing = (ImageView) findViewById(R.id.imgView_timing);
        title_layout = findViewById(R.id.title_layout);

        circleImg_left = (ZoomCircleView) findViewById(R.id.circleImg_left);
        circleImg_right = (ZoomCircleView) findViewById(R.id.circleImg_right);
        circleImg_middle = (CircleImageView) findViewById(R.id.circleImg_middle);
        imgView_title_plus = (ImageView) findViewById(R.id.imgView_title_plus);
        layout_two_user = findViewById(R.id.layout_two_user);
        imgView_title_menu = (ImageView) findViewById(R.id.imgView_title_menu);

        imgView_title_menu.setOnClickListener(this);
        circleImg_left.setOnClickListener(this);
        circleImg_right.setOnClickListener(this);
        imgView_title_plus.setOnClickListener(this);

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

        /**
         * -----------侧边menu-------------
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        activity_menu = findViewById(R.id.activity_menu);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//开启关闭禁止模式
        imgView_switch_user = (ImageView) findViewById(R.id.imgView_switch_user);
        layout_connect_mooring = findViewById(R.id.layout_connect_mooring);
        layout_connected_device = findViewById(R.id.layout_connected_device);
        imgView_to_connect = (ImageView) findViewById(R.id.imgView_to_connect);
        zcView_left = (ZoomCircleView) findViewById(R.id.zcView_left);
        imgView_delete_left = (ImageView) findViewById(R.id.imgView_delete_left);
        zcView_right = (ZoomCircleView) findViewById(R.id.zcView_left);
        imgView_delete_right = (ImageView) findViewById(R.id.imgView_delete_right);
        layout_device = findViewById(R.id.layout_device);
        tv_not_connected = (TextView) findViewById(R.id.tv_not_connected);
        tv_connect_health = (TextView) findViewById(R.id.tv_connect_health);
        tv_about_text = (TextView) findViewById(R.id.tv_about_text);
        tv_suggestions_text = (TextView) findViewById(R.id.tv_suggestions_text);
        tv_login_out = (TextView) findViewById(R.id.tv_login_out);

        menu_recyclerView = (RecyclerView) findViewById(R.id.menu_recyclerView);
        menu_recyclerView.setHasFixedSize(true);
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        menu_recyclerView.setLayoutManager(layoutManager);

        imgView_switch_user.setOnClickListener(this);
        imgView_to_connect.setOnClickListener(this);
        imgView_delete_left.setOnClickListener(this);
        imgView_delete_right.setOnClickListener(this);
        tv_connect_health.setOnClickListener(this);
        tv_about_text.setOnClickListener(this);
        tv_suggestions_text.setOnClickListener(this);
        tv_login_out.setOnClickListener(this);
        activity_menu.setOnClickListener(this);
        layout_device.setOnClickListener(this);

    }


    /**
     * 初始化相关数据
     */
    private void initData() {
        dataList = new ArrayList<UserHeadInfo>();
        for (int i = 0; i < 10; i++) {
            UserHeadInfo d = new UserHeadInfo("Alex", Environment.getExternalStorageDirectory().getPath()
                    + "/Download/11223.jpg");
            dataList.add(d);
        }
        adapter = new UserListAdapter(this, dataList);
        adapter.setOnClickListener(new UserListAdapter.OnClickListener<UserHeadInfo>() {
            @Override
            public void onClick(UserHeadInfo data, int position) {
                Intent it = new Intent();
                if (position != dataList.size() - 1) {
                    it.setClass(MainActivity.this, UserInfoActivity.class);
                    startActivityForResult(it, MConstants.USER_INFO_REQUEST);
                } else {
                    //添加用户
                    it.setClass(MainActivity.this, AddUserActivity.class);
                    startActivityForResult(it, MConstants.ADD_USER_REQUEST);
                }
            }
        });
        menu_recyclerView.setAdapter(adapter);
    }

    /**
     * @param index
     */
    private void setTabSelection(int index) {
        fragmentTransaction = fragmentManager.beginTransaction();

        //添加闹钟按钮,只在闹钟界面起作用
        if (index == TIMING) {
            imgView_title_plus.setVisibility(View.VISIBLE);
        } else {
            imgView_title_plus.setVisibility(View.INVISIBLE);
        }
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

        setTabSelectStatus(index);
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
    private void setTabSelectStatus(int index) {
        //恢复状态栏为透明底色
//        title_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        imgView_weather.setImageResource(index != WEATHER ? R.drawable.btn_weather_normal : R.drawable.btn_weather_select);
        imgView_control.setImageResource(index != CONTROL ? R.drawable.btn_control_normal : R.drawable.btn_control_select);
        imgView_parameter.setImageResource(index != PARAMETER ? R.drawable.btn_parameter_normal : R.drawable.btn_parameter_select);
        imgView_timing.setImageResource(index != TIMING ? R.drawable.btn_timing_normal : R.drawable.btn_timing_select);

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
                if (listener != null) {
                    listener.onSwitch(MConstants.LEFT_USER);
                }
                break;
            case MConstants.RIGHT_USER:
                //右边用户
                if (listener != null) {
                    listener.onSwitch(MConstants.RIGHT_USER);
                }
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

        Intent it = new Intent();

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
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.imgView_title_plus:
                it.putExtra("flag", "add");
                it.setClass(MainActivity.this, AlarmEditActivity.class);
                startActivityForResult(it, MConstants.ALARM_EDIT_REQUEST);
                break;
            case R.id.imgView_switch_user:
                Toast.makeText(this, "imgView_switch_user", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgView_to_connect:
                startActivity(new Intent(MainActivity.this, SetWifiActivity.class));
                break;
            case R.id.imgView_delete_left:
                Toast.makeText(this, "imgView_delete_left", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgView_delete_right:
                Toast.makeText(this, "imgView_delete_right", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_connect_health:
                Toast.makeText(this, "tv_connect_health", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_about_text:
                it.setClass(MainActivity.this, AboutActivity.class);
                startActivity(it);
                break;
            case R.id.tv_suggestions_text:
                it.setClass(MainActivity.this, SuggestionsActivity.class);
                startActivity(it);
                break;
            case R.id.tv_login_out:
                Toast.makeText(this, "tv_login_out", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_device:
                Toast.makeText(this, "layout_device", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_menu:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MConstants.ADD_USER_REQUEST && resultCode == MConstants.ADD_USER_RESULT) {
            //此时是添加用户回调

            //执行列表更新
        }

        if (requestCode == MConstants.USER_INFO_REQUEST && resultCode == MConstants.USER_INFO_RESULT) {

            //此时为修改用户完成后的回调

            //修改用户展示头像
        }

        if (requestCode == MConstants.ALARM_EDIT_REQUEST && resultCode == MConstants.ALARM_EDIT_RESULT) {
            Fragment f = fragmentManager.findFragmentByTag("TimingFragment");
            f.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        MachtalkSDK.getInstance().stopSDK();
        super.onDestroy();
    }

    /**
     * 切换用户接口
     */
    public interface OnSwitchUserListener {
        void onSwitch(int position);
    }

    /**
     * 设置切换用户监听--用于四个fragment
     *
     * @param l
     */
    public void setOnSwitchUserListener(OnSwitchUserListener l) {
        this.listener = l;
    }
}
