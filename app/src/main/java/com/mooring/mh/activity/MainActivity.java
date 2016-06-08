package com.mooring.mh.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.DeviceStatus;
import com.machtalk.sdk.domain.DvidStatus;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.BuildConfig;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

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

    /**
     * google fit
     */
    private GoogleApiClient mClient = null;

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

        setTabSelection(WEATHER);

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

        circleImg_left.setOnClickListener(this);
        circleImg_right.setOnClickListener(this);
        imgView_title_plus.setOnClickListener(this);
        imgView_title_menu.setOnClickListener(this);

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
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menu_recyclerView.setHasFixedSize(true);
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
     * <p>
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
//--------------------**********************************----------------------------------------
    /**
     * 连接Google Fit
     */
    private void connectGoogleFit() {

        LogUtil.e("connect google fit");

        if (!checkPermissions()) {
            requestPermissions();
            return;
        }

        buildFitnessClient();
    }

    /**
     * 检查权限是否已赋予
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求位置权限
     */
    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            //弹出Dialog,进行选择
            showDialog(getString(R.string.permission_location));
        } else {
            //直接申请
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MConstants.PERMISSIONS_LOCATION);
        }
    }

    /**
     * 实例化Fitness
     */
    private void buildFitnessClient() {
        if (mClient == null && checkPermissions()) {
            GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
            builder.addApi(Fitness.HISTORY_API);
            builder.addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE));
            builder.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    LogUtil.i("Connected!!!");
                    // Now you can make calls to the Fitness APIs.

//                    new InsertAndVerifyDataTask().execute();

                    MUtils.showToast(MainActivity.this, "绑定成功");
                }

                @Override
                public void onConnectionSuspended(int i) {
                    // If your connection to the sensor gets lost at some point,
                    // you'll be able to determine the reason and react to it here.
                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                        LogUtil.i("Connection lost.  Cause: Network Lost.");
                    } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                        LogUtil.i("Connection lost.  Reason: Service Disconnected");
                    }
                }
            });
            builder.enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult result) {
                    LogUtil.i("Google Play services 连接失败: " + result.toString());
                }
            });
            mClient = builder.build();
        }
    }

    /**
     * 插入数据
     */
    private class InsertAndVerifyDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            // Create a new dataset and insertion request.
            DataSet dataSet = insertFitnessData();

            // [START insert_dataset]
            // Then, invoke the History API to insert the data and await the result, which is
            // possible here because of the {@link AsyncTask}. Always include a timeout when calling
            // await() to prevent hanging that can occur from the service being shutdown because
            // of low memory or other conditions.
            //调用History API去插入数据同时等待结果,增加一个超时等待以防止内存过低或者服务器被关闭或者其他可能
            LogUtil.i("Inserting the dataset in the History API.");
            com.google.android.gms.common.api.Status insertStatus =
                    Fitness.HistoryApi.insertData(mClient, dataSet).await(1, TimeUnit.MINUTES);

            // Before querying the data, check to see if the insertion succeeded.
            //在查询数据之前,检查是否插入成功
            if (!insertStatus.isSuccess()) {
                LogUtil.i("There was a problem inserting the dataset.");
                return null;
            }

            // At this point, the data has been inserted and can be read.
            //此时,数据已经成功插入了
            LogUtil.i("Data insert was successful!");
            // [END insert_dataset]

            // Begin by creating the query.
            //查询数据
            DataReadRequest readRequest = queryFitnessData();

            // [START read_dataset]
            // Invoke the History API to fetch the data with the query and await the result of
            // the read request.
            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);
            // [END read_dataset]

            // For the sake of the sample, we'll print the data so we can see what we just added.
            // In general, logging fitness information should be avoided for privacy reasons.


            printData(dataReadResult);

            return null;
        }
    }

    /**
     * Create and return a {@link DataSet} of heart count data for insertion using the History API.
     * 创建一个心跳数据集
     */
    private DataSet insertFitnessData() {
        LogUtil.i("Creating a new data insert request.");

        // [START build_insert_data_request]
        // Set a start and end time for our data, using a start time of 1 hour before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        long startTime = cal.getTimeInMillis();

        // 创建一个数据源
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_HEART_RATE_BPM)
                .setStreamName("******" + " - step count")
                .setType(DataSource.TYPE_RAW)
                .build();

        // 创建一个数据集
        int heartCountDelta = 90;
        DataSet dataSet = DataSet.create(dataSource);
        // For each data point, specify a start time, end time, and the data value -- in this case,
        // the number of new steps.
        DataPoint dataPoint = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_BPM).setInt(heartCountDelta);
        dataSet.add(dataPoint);
        // [END build_insert_data_request]

        return dataSet;
    }

    /**
     * Return a {@link DataReadRequest} for all heart count changes in the past week.
     * 返回心跳总数集
     */
    public static DataReadRequest queryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateInstance();
        LogUtil.i("Range Start: " + dateFormat.format(startTime));
        LogUtil.i("Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                // The data request can specify multiple data types to return, effectively
                // combining multiple data queries into one call.
                // In this example, it's very unlikely that the request is for several hundred
                // datapoints each consisting of a few steps and a timestamp.  The more likely
                // scenario is wanting to see how many steps were walked per day, for 7 days.
                .aggregate(DataType.TYPE_HEART_RATE_BPM, DataType.AGGREGATE_HEART_RATE_SUMMARY)
                // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                // bucketByTime allows for a time span, whereas bucketBySession would allow
                // bucketing by "sessions", which would need to be defined in code.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        // [END build_read_data_request]

        return readRequest;
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MConstants.PERMISSIONS_LOCATION) {
            if (grantResults.length <= 0) {
                LogUtil.i(getString(R.string.error_permission_failed));
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                buildFitnessClient();
            } else {
                // Permission denied.
                showDialog(getString(R.string.permission_location));
            }
        }
    }

    /**
     * Log a record of the query result. It's possible to get more constrained data sets by
     * specifying a data source or data type, but for demonstrative purposes here's how one would
     * dump all the data. In this sample, logging also prints to the device screen, so we can see
     * what the query returns, but your app should not log fitness information as a privacy
     * consideration. A better option would be to dump the data you receive to a local data
     * directory to avoid exposing it to other applications.
     */
    public static void printData(DataReadResult dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
            LogUtil.i("Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            LogUtil.i("Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    private static void dumpDataSet(DataSet dataSet) {
        LogUtil.i("Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            LogUtil.i("Data point:");
            LogUtil.i("\tType: " + dp.getDataType().getName());
            LogUtil.i("\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            LogUtil.i("\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                LogUtil.i("\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
            }
        }
    }
//--------------------**********************************----------------------------------------
    /**
     * 跳转到设置界面的Dialog提示
     *
     * @param msg
     */
    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(getString(R.string.tip_access_request));
        builder.setPositiveButton(getString(R.string.tip_go_setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getString(R.string.tv_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
