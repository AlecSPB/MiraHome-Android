package com.mooring.mh.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.DeviceListInfo;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.utils.DeviceManager;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.mooring.mh.utils.NetworkUtil;

/**
 * 设置WIFI信息进行广播设备
 * <p/>
 * Created by Will on 16/5/12.
 */
public class SetWifiActivity extends BaseActivity {
    private ImageView imgView_act_back;
    private TextView tv_act_skip;
    private View layout_setting_wifi;
    private View layout_no_wifi;
    private View layout_wifi_related;
    private View layout_scan_wifi;
    private TextView tv_wifi_name;
    private EditText edit_wifi_psw;
    private TextView tv_send_wifi;
    private ImageView imgView_retry_connect;
    /**
     * scan
     */
    private TextView tv_scan_skip;
    private ImageView imgView_search_mid;
    private RotateAnimation rotateAnimation;//旋转动画


    private MSDKListener msdkListener;
    private boolean isFirstAddDevice;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_wifi;
    }

    @Override
    protected String getTitleName() {
        return "";
    }

    @Override
    protected void initActivity() {

        isFirstAddDevice = sp.getBoolean(MConstants.FIRST_ADD_DEVICE, true);
        msdkListener = new MSDKListener();

    }

    @Override
    protected void initView() {
        imgView_act_back = (ImageView) findViewById(R.id.imgView_act_back);
        tv_act_skip = (TextView) findViewById(R.id.tv_act_skip);
        layout_setting_wifi = findViewById(R.id.layout_setting_wifi);
        layout_no_wifi = findViewById(R.id.layout_no_wifi);
        layout_wifi_related = findViewById(R.id.layout_wifi_related);
        layout_scan_wifi = findViewById(R.id.layout_scan_wifi);
        tv_wifi_name = (TextView) findViewById(R.id.tv_wifi_name);
        edit_wifi_psw = (EditText) findViewById(R.id.edit_wifi_psw);
        tv_send_wifi = (TextView) findViewById(R.id.tv_send_wifi);
        imgView_retry_connect = (ImageView) findViewById(R.id.imgView_retry_connect);

        tv_scan_skip = (TextView) findViewById(R.id.tv_scan_skip);
        imgView_search_mid = (ImageView) findViewById(R.id.imgView_search_mid);


        judgeWifiConnected();

        tv_act_skip.setVisibility(View.VISIBLE);
        tv_act_skip.setOnClickListener(this);
        tv_send_wifi.setOnClickListener(this);
        imgView_retry_connect.setOnClickListener(this);

        tv_scan_skip.setOnClickListener(this);
        initAnimation();
    }

    /**
     * 初始化旋转动画
     */
    private void initAnimation() {
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
    }

    /**
     * 判断WIFI是否可用,了用的情况下获取WIFI名称
     */
    private void judgeWifiConnected() {
        if (NetworkUtil.isWifiConnected(this)) {
            imgView_act_back.setVisibility(View.GONE);
            layout_setting_wifi.setVisibility(View.VISIBLE);
            layout_no_wifi.setVisibility(View.GONE);
            //测试使用
            edit_wifi_psw.setText("mianmianhome");

            String SSID = NetworkUtil.getSSID(this);
            if (!TextUtils.isEmpty(SSID)) {
                tv_wifi_name.setText(SSID);
            } else {
                MUtils.showToast(this, "请检查WIFI是否已连接");
            }
        } else {
            imgView_act_back.setVisibility(View.VISIBLE);
            layout_setting_wifi.setVisibility(View.GONE);
            layout_no_wifi.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_scan_skip:
            case R.id.tv_act_skip:
                MachtalkSDK.getInstance().setDeviceWiFiCancle();
                SetWifiActivity.this.finish();
                break;
            case R.id.tv_send_wifi:
                layout_wifi_related.setVisibility(View.GONE);
                layout_scan_wifi.setVisibility(View.VISIBLE);
                MachtalkSDK.getInstance().addDevice(tv_wifi_name.getText().toString().trim(),
                        edit_wifi_psw.getText().toString().trim(), MachtalkSDKConstant.NO_SCAN_CODE);
                break;

            case R.id.imgView_retry_connect:
                judgeWifiConnected();
                break;
        }
    }


    class MSDKListener extends MachtalkSDKListener {
        @Override
        public void onAddDevice(Result result, String deviceId) {
            super.onAddDevice(result, deviceId);
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS) {
                //添加成功之后,执行查询已绑定设备列表
                MachtalkSDK.getInstance().queryDeviceList();
                InitApplicationHelper.sp.edit().putString(MConstants.DEVICE_ID, deviceId).commit();
            } else {
                //添加失败直接跳转错误界面
                startActivity(new Intent(SetWifiActivity.this, NotFindDeviceActivity.class));
                SetWifiActivity.this.finish();
            }
        }

        @Override
        public void onQueryDeviceList(Result result, DeviceListInfo dli) {
            super.onQueryDeviceList(result, dli);
            int success = Result.FAILED;
            String errorMsg = null;
            if (result != null) {
                success = result.getSuccess();
                errorMsg = result.getErrorMessage();
            }
            if (dli != null && success == Result.SUCCESS) {
                DeviceManager.getInstance().setDeviceList(dli.getDeviceList());
                Intent it = new Intent(SetWifiActivity.this, CommonSuccessActivity.class);
                it.putExtra(MConstants.ENTRANCE_FLAG, MConstants.CONNECTED_SUCCESS);
                startActivity(it);
            } else {
                if (errorMsg == null) {
                    errorMsg = getResources().getString(R.string.load_device_list_fail);
                }
                MUtils.showToast(SetWifiActivity.this, errorMsg);
                //添加失败直接跳转错误界面
                startActivity(new Intent(SetWifiActivity.this, NotFindDeviceActivity.class));
            }
            SetWifiActivity.this.finish();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(msdkListener);
        imgView_search_mid.startAnimation(rotateAnimation);
    }

    @Override
    public void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(msdkListener);
        imgView_search_mid.clearAnimation();
    }
}
