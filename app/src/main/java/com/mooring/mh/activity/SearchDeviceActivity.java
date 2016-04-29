package com.mooring.mh.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.machtalk.sdk.domain.Device;
import com.machtalk.sdk.domain.DeviceListInfo;
import com.machtalk.sdk.domain.ModuleVersionInfo;
import com.machtalk.sdk.domain.Result;
import com.machtalk.sdk.domain.SearchedLanDevice;
import com.machtalk.sdk.domain.UnbindDeviceResult;
import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.NetworkUtil;

import java.util.List;

/**
 * 搜索设备的Activity
 * <p/>
 * Created by Will on 16/4/25.
 */
public class SearchDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = SearchDeviceActivity.class.getSimpleName();
    private TextView tv_act_skip;
    private ImageView imgView_search_up;
    private ImageView imgView_search_mid;
    private ImageView imgView_search_bot;
    private RotateAnimation rotateAnimation;//旋转动画

    private View layout_setting_wifi;
    private View layout_scan_wifi;
    private TextView tv_wifi_name;
    private EditText edit_wifi_password;
    private TextView tv_send_wifi;
    private TextView tv_confirm_btn;

    /**
     * 扫描Wifi
     */
    private MachtalkSDKListener mSdkListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_device);

        initView();

        initData();
    }

    private void initView() {
        //---------setting wifi---------------
        layout_setting_wifi = findViewById(R.id.layout_setting_wifi);
        tv_wifi_name = (TextView) findViewById(R.id.tv_wifi_name);
        edit_wifi_password = (EditText) findViewById(R.id.edit_wifi_password);
        tv_send_wifi = (TextView) findViewById(R.id.tv_send_wifi);
        tv_confirm_btn = (TextView) findViewById(R.id.tv_confirm_btn);
        //---------scan wifi---------------
        layout_scan_wifi = findViewById(R.id.layout_scan_wifi);
        tv_act_skip = (TextView) findViewById(R.id.tv_act_skip);
        imgView_search_up = (ImageView) findViewById(R.id.imgView_search_up);
        imgView_search_mid = (ImageView) findViewById(R.id.imgView_search_mid);
        imgView_search_bot = (ImageView) findViewById(R.id.imgView_search_bot);

        tv_send_wifi.setOnClickListener(this);
        tv_confirm_btn.setOnClickListener(this);
        tv_act_skip.setOnClickListener(this);

    }


    private void initData() {

        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        edit_wifi_password.setText("mianmianhome");

        mSdkListener = new MySDKListener();

        //执行检索WIFI
        MachtalkSDK.getInstance().startSDK(this, null);
        MachtalkSDK.getInstance().setSdkListener(mSdkListener);

        String SSID = NetworkUtil.getSSID(this);
        if (SSID != null && !"".equals(SSID)) {
            tv_wifi_name.setText(SSID);
        } else {
            CommonUtils.showToast(this, "请检查WIFI是否已连接");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        imgView_search_mid.startAnimation(rotateAnimation);
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(mSdkListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        imgView_search_mid.clearAnimation();
        MachtalkSDK.getInstance().removeSdkListener(mSdkListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgView_search_mid.clearAnimation();
        MachtalkSDK.getInstance().stopSDK();
        MachtalkSDK.getInstance().removeSdkListener(mSdkListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_act_skip:

                //停止检索WIFI


                //跳转到已有设备处


                //结束当前Activity
                SearchDeviceActivity.this.finish();
                break;
            case R.id.tv_send_wifi:

                MachtalkSDK.getInstance().unbindDevice(deviceId, MachtalkSDKConstant.
                        UnbindType.UNBIND, null);


                Log.e("tv_send_wifi", "  " + tv_wifi_name.getText().toString().trim() +
                        "  " + edit_wifi_password.getText().toString().trim());

//                MachtalkSDK.getInstance().addDevice(tv_wifi_name.getText().toString().trim(),
//                        edit_wifi_password.getText().toString().trim(), MachtalkSDKConstant.NO_SCAN_CODE);

                break;
            case R.id.tv_confirm_btn:

//                MachtalkSDK.getInstance().queryDeviceList();

                MachtalkSDK.getInstance().unbindDevice(deviceId, MachtalkSDKConstant.
                        UnbindType.UNBIND, null);

                MachtalkSDK.getInstance().restoreDeviceFactorySettings(deviceId, devicePin, String.valueOf(System.currentTimeMillis() / 1000));


                break;

        }
    }

    private String deviceId = "110000001000028122";
    private String devicePin = "";

    class MySDKListener extends MachtalkSDKListener {

        @Override
        public void onUnbindDevice(Result result, UnbindDeviceResult unbindDeviceResult) {
            if (result.getSuccess() == Result.SUCCESS) {
                Log.i(TAG, "onUnbindDevice  success  " + unbindDeviceResult.getDeviceId() + "  " +
                        unbindDeviceResult.getUid() + "   " + unbindDeviceResult.getUnbindType());


            } else {
                Log.i(TAG, "onUnbindDevice  error error");
            }

            MachtalkSDK.getInstance().addDevice(tv_wifi_name.getText().toString().trim(),
                    edit_wifi_password.getText().toString().trim(), MachtalkSDKConstant.NO_SCAN_CODE);

//            MachtalkSDK.getInstance().setDeviceWiFi(tv_wifi_name.getText().toString().trim(),
//                    edit_wifi_password.getText().toString().trim(), MachtalkSDKConstant.NO_SCAN_CODE);
        }

        @Override
        public void onAddDevice(Result result, String deviceId) {
            if (result.getSuccess() == Result.SUCCESS) {
                Log.i(TAG, "onAddDevice  success");
                MachtalkSDK.getInstance().queryDeviceModuleVersionInfo(deviceId);
                MachtalkSDK.getInstance().queryDeviceList();
                MachtalkSDK.getInstance().modifyDeviceName(deviceId, "测试名称");
            } else {
                Log.i(TAG, "onAddDevice  error error");
            }
        }

        @Override
        public void onSetDeviceWiFi(Result result, SearchedLanDevice info) {

            if (result.getSuccess() == Result.SUCCESS) {
                Log.i(TAG, "update wifi success");
                deviceId = info.getDeviceId();
                devicePin = info.getDevicePin();

                MachtalkSDK.getInstance().bindDevice(info);
            } else {
                Log.i(TAG, "update wifi error error");
            }

        }

        @Override
        public void onBindDevice(Result result, String deviceId) {
            if (result.getSuccess() == Result.SUCCESS) {
                Log.i(TAG, "bind device success");
                MachtalkSDK.getInstance().queryDeviceModuleVersionInfo(deviceId);
                finish();
            } else {
                Log.i(TAG, "bind device error error");
            }
        }


        @Override
        public void onQueryDeviceModuleVersion(Result result, ModuleVersionInfo info) {
            Log.i(TAG, "result " + result.getErrorCode() + "  getDeviceId  " + info.getDeviceId()
                    + " NewVersion " + info.getNewVersion() + " TimeStr " + info.getTimeStr() +
                    " Type " + info.getType() + " Version " + info.getVersion() + " VersionInfo "
                    + info.getVersionInfo() + " VersionTime " + info.getVersionTime());
        }

        @Override
        public void onQueryDeviceList(Result result, DeviceListInfo info) {
            List<Device> ds = info.getDeviceList();
            for (int i = 0; i < ds.size(); i++) {
                Log.i(TAG, "  " + ds.get(i).getId() + "  " + ds.get(i).getVersion() + "  " + ds.get(i).getType() +
                                "  " + ds.get(i).getBindTime() + "  " + ds.get(i).getFirm() + "  " + ds.get(i).getModel() +
                                "  " + ds.get(i).getName() + "  " + ds.get(i).getPid() + "  " + ds.get(i).getProduct() +
                                "  " + ds.get(i).getResourceVersion() + "  " + ds.get(i).getSymbol() +
                                "  " + ds.get(i).getType() + "  " + ds.get(i).getVersion() + "  " + ds.get(i).getOrderNo()
                );
            }
        }

        @Override
        public void onModifyDeviceName(Result result, String deviceId) {
            Log.i(TAG, "onModifyDeviceName " + result.getErrorCode() + " " + deviceId);

            MachtalkSDK.getInstance().queryDeviceList();
        }
    }
}
