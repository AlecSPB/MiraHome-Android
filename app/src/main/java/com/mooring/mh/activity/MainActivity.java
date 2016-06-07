package com.mooring.mh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.DeviceStatus;
import com.machtalk.sdk.domain.DvidStatus;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.adapter.OnRecyclerItemClickListener;
import com.mooring.mh.adapter.UserListAdapter;
import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.db.DbXUtils;
import com.mooring.mh.db.User;
import com.mooring.mh.fragment.ControlFragment;
import com.mooring.mh.fragment.ParameterFragment;
import com.mooring.mh.fragment.TimingFragment;
import com.mooring.mh.fragment.WeatherFragment;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.mooring.mh.views.CircleImgView.CircleImageView;
import com.mooring.mh.views.CircleImgView.ZoomCircleView;
import com.mooring.mh.views.CustomToggle;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面MainActivity
 */
public class MainActivity extends SubjectActivity implements View.OnClickListener,
        OnRecyclerItemClickListener, CustomToggle.OnCheckedChangeListener {

    private ImageView imgView_weather;//tab-天气
    private ImageView imgView_control;//tab-控制
    private ImageView imgView_parameter;//tab-参数
    private ImageView imgView_timing;//tab-闹钟

    private ImageView imgView_title_menu;//菜单
    private ZoomCircleView circleImg_left;//左边用户
    private ZoomCircleView circleImg_right;//右边用户
    private CircleImageView circleImg_middle;//单个用户
    public ImageView imgView_title_plus;//添加闹钟
    private View layout_two_user;//两个用户时,头像布局
    private View title_layout;//整个上部分title布局

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private WeatherFragment weatherFragment;  //WeatherFragment
    private ControlFragment controlFragment;  //ControlFragment
    private ParameterFragment parameterFragment;  //ParameterFragment
    private TimingFragment timingFragment;  //TimingFragment
    /**
     * fragment 标签ID
     */
    public final int WEATHER = 1;
    public final int CONTROL = 2;
    public final int PARAMETER = 3;
    public final int TIMING = 4;
    /**
     * ---------侧边滑动栏相关--------
     */
    private DrawerLayout mDrawerLayout;//侧边menu可滑动布局

    private View activity_menu;//侧边menu整个布局,帮助消费点击事件
    private RecyclerView menu_recyclerView;//横向滑动view
    private ImageView imgView_switch_user;//切换用户按钮
    private View layout_connect_mooring;//重新连接view
    private ImageView imgView_to_connect;//点击链接图片
    private View layout_exist_device;//一连上设备
    private TextView tv_fahrenheit;//华氏度
    private CustomToggle toggle_temp;//开关
    private TextView tv_celsius;//设置
    private CircleImageView menu_zcView_left; // 左边用户头像
    private ImageView imgView_delete_left;//左边删除用户图标
    private View layout_user_right;//右边用户
    private CircleImageView menu_zcView_right;//右边用户头像
    private ImageView imgView_delete_right;//右边删除用户图标
    private View layout_device; // device设备连接状态整个布局
    private TextView tv_not_connected;//device设备连接状态
    private TextView tv_connect_health; // 链接Health kit
    private TextView tv_help_text;
    private TextView tv_about_text; // 关于我们
    private TextView tv_suggestions_text;// 建议
    private TextView tv_login_out;//退出登陆

    private RecyclerView.LayoutManager layoutManager;//横向滑动用户列表布局
    private UserListAdapter adapter;//横向滑动适配器
    /**
     * 系统变量相关
     */
    private int currLocation = MConstants.LEFT_USER;//当前用户的位置
    private User currLeftUser;//当前左侧User
    private User currRightUser;//当前右侧User
    private List<User> currentUsers;//存放当前需要展现的用户,一个或者两个
    private DbManager dbManager;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private BaseListener baseListener;
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = InitApplicationHelper.sp;
        editor = sp.edit();
        editor.apply();
        deviceId = sp.getString(MConstants.DEVICE_ID, "");

        fragmentManager = getSupportFragmentManager();

        baseListener = new BaseListener();
        MachtalkSDK.getInstance().setContext(this);

        DbManager.DaoConfig dao = DbXUtils.getDaoConfig(this);
        dbManager = x.getDb(dao);

        initView();

        initData();

        setTabSelection(CONTROL);

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

        /**
         * -----------侧边menu-------------
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);
        activity_menu = findViewById(R.id.activity_menu);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//开启关闭模式
        imgView_switch_user = (ImageView) findViewById(R.id.imgView_switch_user);
        layout_connect_mooring = findViewById(R.id.layout_connect_mooring);
        layout_exist_device = findViewById(R.id.layout_exist_device);
        imgView_to_connect = (ImageView) findViewById(R.id.imgView_to_connect);
        menu_zcView_left = (CircleImageView) findViewById(R.id.menu_zcView_left);
        imgView_delete_left = (ImageView) findViewById(R.id.imgView_delete_left);
        layout_user_right = findViewById(R.id.layout_user_right);
        menu_zcView_right = (CircleImageView) findViewById(R.id.menu_zcView_right);
        imgView_delete_right = (ImageView) findViewById(R.id.imgView_delete_right);
        layout_device = findViewById(R.id.layout_device);
        tv_not_connected = (TextView) findViewById(R.id.tv_not_connected);
        tv_connect_health = (TextView) findViewById(R.id.tv_connect_health);
        tv_fahrenheit = (TextView) findViewById(R.id.tv_fahrenheit);
        toggle_temp = (CustomToggle) findViewById(R.id.toggle_temp);
        tv_celsius = (TextView) findViewById(R.id.tv_celsius);
        tv_help_text = (TextView) findViewById(R.id.tv_help_text);
        tv_about_text = (TextView) findViewById(R.id.tv_about_text);
        tv_suggestions_text = (TextView) findViewById(R.id.tv_suggestions_text);
        tv_login_out = (TextView) findViewById(R.id.tv_login_out);

        menu_recyclerView = (RecyclerView) findViewById(R.id.menu_recyclerView);
        menu_recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menu_recyclerView.setLayoutManager(layoutManager);

        tv_connect_health.setOnClickListener(this);
        tv_help_text.setOnClickListener(this);
        tv_about_text.setOnClickListener(this);
        tv_suggestions_text.setOnClickListener(this);
        tv_login_out.setOnClickListener(this);
        activity_menu.setOnClickListener(this);
        layout_device.setOnClickListener(this);

        toggle_temp.setChecked(true);
        toggle_temp.setOnCheckedChange(this);
    }


    /**
     * 初始化相关数据
     */
    private void initData() {
        /**
         * 以下操作和当前用户数/单双毯没有关联性---------------------
         */
        try {
            currLeftUser = dbManager.selector(User.class).
                    where("_location", "=", MConstants.BED_LEFT).findFirst();
            currRightUser = dbManager.selector(User.class).
                    where("_location", "=", MConstants.BED_RIGHT).findFirst();
            currentUsers = dbManager.selector(User.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (currentUsers == null) {
            currentUsers = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                User user = new User();
                user.set_name("Alex");
                user.set_header(Environment.getExternalStorageDirectory().getPath() + "/Download/11223.jpg");
                currentUsers.add(user);
            }
        }
        /**
         * 添加一个空的User作为添加按钮
         */
        currentUsers.add(new User());
        adapter = new UserListAdapter(currentUsers);
        adapter.setOnClickListener(this);
        menu_recyclerView.setAdapter(adapter);

        MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
    }

    /**
     * 根据设备的类型{单人垫,双人垫},以及现在所拥有的用户个数
     * value  0：单人1：双人
     * <p/>
     * 计算当前用户个数
     */
    public void computeCurrentUsers(String value) {
        int num = Integer.parseInt(value);
        if (num == 1) {//双人毯
            if (currentUsers.size() > 1) {//多个人
                circleImg_middle.setVisibility(View.GONE);
                layout_two_user.setVisibility(View.VISIBLE);
                circleImg_left.setOnClickListener(this);
                circleImg_right.setOnClickListener(this);
                editor.putInt(MConstants.CURR_BLANKET_MODEL, MConstants.DOUBLE_BLANKET_MULTIPLE);
            } else {//单个人
                circleImg_middle.setVisibility(View.VISIBLE);
                layout_two_user.setVisibility(View.GONE);
                editor.putInt(MConstants.CURR_BLANKET_MODEL, MConstants.DOUBLE_BLANKET_SINGLE);
            }
        }
        if (num == 0) {//单人毯
            circleImg_middle.setVisibility(View.VISIBLE);
            layout_two_user.setVisibility(View.GONE);
            editor.putInt(MConstants.CURR_BLANKET_MODEL, MConstants.SINGLE_BLANKET);
        }
        //根绝设备是否在线,决定当前Menu中显示内容
        if (sp.getBoolean(MConstants.DEVICE_ONLINE, false)) {
            layout_exist_device.setVisibility(View.VISIBLE);
            layout_connect_mooring.setVisibility(View.GONE);
            tv_not_connected.setText(sp.getString(MConstants.DEVICE_NAME, ""));

            int curr_blanket_model = sp.getInt(MConstants.CURR_BLANKET_MODEL, -1);
            if (curr_blanket_model == MConstants.SINGLE_BLANKET ||
                    curr_blanket_model == MConstants.DOUBLE_BLANKET_SINGLE) {
                //单人毯切换床的图片
                imgView_switch_user.setEnabled(false);
                imgView_switch_user.setAlpha(0.5f);
                layout_user_right.setVisibility(View.GONE);
                imgView_delete_left.setOnClickListener(this);
                menu_zcView_left.setImageBitmap(BitmapFactory.decodeFile(currLeftUser.get_header()));
            }
            if (curr_blanket_model == MConstants.DOUBLE_BLANKET_MULTIPLE) {
                imgView_switch_user.setEnabled(true);
                imgView_switch_user.setAlpha(1f);
                layout_user_right.setVisibility(View.VISIBLE);
                imgView_switch_user.setOnClickListener(this);
                imgView_delete_left.setOnClickListener(this);
                menu_zcView_left.setImageBitmap(BitmapFactory.decodeFile(currLeftUser.get_header()));
                imgView_delete_right.setOnClickListener(this);
                menu_zcView_right.setImageBitmap(BitmapFactory.decodeFile(currRightUser.get_header()));
            }
        } else {
            layout_exist_device.setVisibility(View.GONE);
            layout_connect_mooring.setVisibility(View.VISIBLE);
            imgView_to_connect.setOnClickListener(this);
            tv_not_connected.setText(getString(R.string.tv_not_connected));
        }
    }

    /**
     * Tab被选中执行
     *
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
        //隐藏当前正在显示的fragment
        if (fragmentManager.getFragments() != null) {
            fragmentTransaction.hide(getVisibleFragment());
        }
        switch (index) {
            case WEATHER:
                if (weatherFragment == null) {
                    weatherFragment = new WeatherFragment();
                    this.attach(weatherFragment);
                    fragmentTransaction.add(R.id.main_container, weatherFragment, "WeatherFragment");
                }
                fragmentTransaction.show(weatherFragment);
                break;
            case CONTROL:
                if (controlFragment == null) {
                    controlFragment = new ControlFragment();
                    this.attach(controlFragment);
                    fragmentTransaction.add(R.id.main_container, controlFragment, "ControlFragment");
                }
                fragmentTransaction.show(controlFragment);
                break;
            case PARAMETER:
                if (parameterFragment == null) {
                    parameterFragment = new ParameterFragment();
                    this.attach(parameterFragment);
                    fragmentTransaction.add(R.id.main_container, parameterFragment, "ParameterFragment");
                }
                fragmentTransaction.show(parameterFragment);
                break;
            case TIMING:
                if (timingFragment == null) {
                    timingFragment = new TimingFragment();
                    this.attach(timingFragment);
                    fragmentTransaction.add(R.id.main_container, timingFragment, "TimingFragment");
                }
                fragmentTransaction.show(timingFragment);
                break;
        }
        fragmentTransaction.commit();

        setTabSelectStatus(index);
    }

    /**
     * 获取可见Fragment
     *
     * @return
     */
    public Fragment getVisibleFragment() {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null) {
            return null;
        }
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    /**
     * 设置选中状态
     *
     * @param index
     */
    private void setTabSelectStatus(int index) {
        imgView_weather.setImageResource(
                index != WEATHER ? R.drawable.btn_weather_normal : R.drawable.btn_weather_select);
        imgView_control.setImageResource(
                index != CONTROL ? R.drawable.btn_control_normal : R.drawable.btn_control_select);
        imgView_parameter.setImageResource(
                index != PARAMETER ? R.drawable.btn_parameter_normal : R.drawable.btn_parameter_select);
        imgView_timing.setImageResource(
                index != TIMING ? R.drawable.btn_timing_normal : R.drawable.btn_timing_select);
    }

    /**
     * 切换用户
     *
     * @param location LEFT_USER,RIGHT_USER
     */
    public void switchUser(int location) {
        currLocation = location;
        switch (location) {
            case MConstants.LEFT_USER:
                //左边用户
                try {
                    currLeftUser = dbManager.selector(User.class).where("_location", "=", "1").findFirst();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case MConstants.RIGHT_USER:
                //右边用户
                try {
                    currLeftUser = dbManager.selector(User.class).where("_location", "=", "2").findFirst();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
        //更改本地的当前位置和当前user的id
        editor.putInt(MConstants.CURR_USER_LOCATION, currLocation);
        editor.putString(MConstants.CURR_USER_ID, currLeftUser.getId() + "");
        editor.apply();
        //状态发生改变，通知各个观察者
        this.notifyObservers(currLeftUser.getId() + "", currLocation, getVisibleFragment().getTag());
    }

    /**
     * 交换用户位置
     */
    private void switchUserLocation() {
        MUtils.showToast(this, "switch User Location");

        //切换成功之后,弹出切换成功Dialog
    }

    /**
     * 删除指定位置的User
     *
     * @param location
     */
    private void deleteLocationUser(int location) {

        switch (location) {
            case MConstants.LEFT_USER:
                MUtils.showToast(this, "delete left user");
                break;
            case MConstants.RIGHT_USER:
                MUtils.showToast(this, "delete right user");
                break;
        }
    }

    /**
     * 连接Google Fit
     */
    private void connectGoogleFit() {
        MUtils.showToast(this, "connect google fit");
    }

    /**
     * 退出登录
     */
    private void logOut() {
        MUtils.showToast(this, "退出登录");
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
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.imgView_title_plus:
                mDrawerLayout.closeDrawer(activity_menu);
                it.putExtra("flag", "add");
                it.setClass(MainActivity.this, AlarmEditActivity.class);
                startActivityForResult(it, MConstants.ALARM_EDIT_REQUEST);
                break;
            case R.id.imgView_switch_user:
                switchUserLocation();
                break;
            case R.id.imgView_to_connect:
                mDrawerLayout.closeDrawer(activity_menu);
                it.setClass(MainActivity.this, SetWifiActivity.class);
                startActivity(it);
                break;
            case R.id.imgView_delete_left:
                deleteLocationUser(MConstants.LEFT_USER);
                break;
            case R.id.imgView_delete_right:
                deleteLocationUser(MConstants.RIGHT_USER);
                break;
            case R.id.tv_connect_health:
                connectGoogleFit();
                break;
            case R.id.tv_help_text:
                mDrawerLayout.closeDrawer(activity_menu);
                it.setClass(MainActivity.this, HelpActivity.class);
                startActivity(it);
                break;
            case R.id.tv_about_text:
                mDrawerLayout.closeDrawer(activity_menu);
                it.setClass(MainActivity.this, AboutActivity.class);
                startActivity(it);
                break;
            case R.id.tv_suggestions_text:
                mDrawerLayout.closeDrawer(activity_menu);
                it.setClass(MainActivity.this, SuggestionsActivity.class);
                startActivity(it);
                break;
            case R.id.tv_login_out:
                logOut();
                break;
            case R.id.layout_device:
                mDrawerLayout.closeDrawer(activity_menu);
                it.setClass(MainActivity.this, ExistingDeviceActivity.class);
                startActivity(it);
                break;
            case R.id.activity_menu:
                //不做任何处理,目的是消费menu上层的点击事件
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        mDrawerLayout.closeDrawer(activity_menu);
        Intent it = new Intent();
        it.setClass(MainActivity.this, UserInfoActivity.class);
        if (position != currentUsers.size() - 1) {
            it.putExtra(MConstants.ENTRANCE_FLAG, "edit");
            startActivityForResult(it, MConstants.USER_INFO_REQUEST);
        } else {
            //添加用户
            it.putExtra(MConstants.ENTRANCE_FLAG, "add");
            startActivityForResult(it, MConstants.ADD_USER_REQUEST);
        }
    }

    @Override
    public void onCheckedChanged(View v, boolean isChecked) {
        toggle_temp.setChecked(isChecked);
        if (isChecked) {
            tv_celsius.setTextColor(getResources().getColor(R.color.colorPurple));
            tv_fahrenheit.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            tv_celsius.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_fahrenheit.setTextColor(getResources().getColor(R.color.colorPurple));
        }
        editor.putBoolean(MConstants.TEMPERATURE_UNIT, isChecked).commit();
        MachtalkSDK.getInstance().operateDevice(
                sp.getString(MConstants.DEVICE_ID, ""),
                new String[]{MConstants.ATTR_LEFT_TARGET_TEMP},
                new String[]{isChecked ? "30" : "80"});
        //上面的30和80还有待于修改,目前待定
        //---希望的方式是,增加标志位,以保证当前温度转换过后的准确性
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

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mDrawerLayout.isDrawerOpen(activity_menu)) {
                mDrawerLayout.closeDrawer(activity_menu);
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    class BaseListener extends MachtalkSDKListener {
        @Override
        public void onServerConnectStatusChanged(MachtalkSDKConstant.ServerConnStatus scs) {
            super.onServerConnectStatusChanged(scs);
            if (scs == MachtalkSDKConstant.ServerConnStatus.LOGOUT_KICKOFF) {
                Intent it = new Intent(MainActivity.this, LoginAndSignUpActivity.class);
                it.putExtra(MConstants.ENTRANCE_FLAG, MConstants.LOGOUT_KICKOFF);
                startActivity(it);
//                MainActivity.this.finish();//用户在其它地方登录，用户被踢掉
                return;
            }
            if (scs == MachtalkSDKConstant.ServerConnStatus.CONNECT_BREAK) {
                LogUtil.e("服务器连接中断,请重试");
                MainActivity.this.finish();//服务器连接中断
                return;
            }
        }

        @Override
        public void onQueryDeviceStatus(Result result, DeviceStatus deviceStatus) {
            super.onQueryDeviceStatus(result, deviceStatus);
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS && deviceStatus != null && deviceId.equals(deviceStatus.getDeviceId())) {
                List<DvidStatus> list = deviceStatus.getDeviceDvidStatuslist();
                if (list == null) {
                    return;
                }
                for (DvidStatus d : list) {
                    if (d.getDvid().equals(MConstants.ATTR_SINGLE_OR_DOUBLE)) {
                        computeCurrentUsers(d.getValue());//得到当前单/双人模式
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(baseListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(baseListener);
    }

    @Override
    protected void onDestroy() {
        MachtalkSDK.getInstance().stopSDK();
        super.onDestroy();
    }
}
