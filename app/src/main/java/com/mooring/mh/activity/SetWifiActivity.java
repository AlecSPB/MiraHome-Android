package com.mooring.mh.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.mooring.mh.utils.NetworkUtil;

import org.xutils.common.util.LogUtil;

/**
 * 设置WIFI信息进行广播设备
 * <p>
 * Created by Will on 16/5/12.
 */
public class SetWifiActivity extends BaseActivity {

    private View layout_tip_connect;//设备连接指导
    private View layout_setting_wifi;//设置WIFI密码
    private View layout_no_wifi;//没有在WIFI条件下
    private View layout_scan_wifi;//扫描设备
    private View layout_no_device;//无设备
    private TextView tv_act_skip;//跳过
    /**
     * 导航
     */
    private TextView tv_confirm_guide;//导航提示确认
    /**
     * 设置WIFI密码
     */
    private TextView tv_wifi_name;//wifi名称
    private EditText edit_wifi_psw;//wifi密码
    private TextView tv_error_text;//显示错误提示信息
    private TextView tv_send_wifi;//发送wifi密码,开始检索
    /**
     * 没有WIFI
     */
    private ImageView imgView_retry_connect;//重试,检查是否有wifi
    /**
     * 扫描
     */
    private ImageView imgView_search_mid;//检索动画中间图片
    /**
     * 没有设备层
     */
    private ImageView imgView_retry_search;//重试,搜索设备

    private RotateAnimation rotateAnimation;//旋转动画
    private AlphaAnimation showAnimation;//展示
    private AlphaAnimation hideAnimation;//隐藏
    private MSDKListener msdkListener;
    private View currShowView;//当前操作的show View
    private View currHideView;//当前操作的hide View
    private InputMethodManager imm;//输入

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

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        msdkListener = new MSDKListener();
        self_control_back = true;//设置返回键点击事件自主控制
    }

    @Override
    protected void initView() {

        layout_tip_connect = findViewById(R.id.layout_tip_connect);
        layout_setting_wifi = findViewById(R.id.layout_setting_wifi);
        layout_no_wifi = findViewById(R.id.layout_no_wifi);
        layout_scan_wifi = findViewById(R.id.layout_scan_wifi);
        layout_no_device = findViewById(R.id.layout_no_device);
        tv_act_skip = (TextView) findViewById(R.id.tv_act_skip);

        tv_confirm_guide = (TextView) findViewById(R.id.tv_confirm_guide);
        tv_wifi_name = (TextView) findViewById(R.id.tv_wifi_name);
        edit_wifi_psw = (EditText) findViewById(R.id.edit_wifi_psw);
        tv_error_text = (TextView) findViewById(R.id.tv_error_text);
        tv_send_wifi = (TextView) findViewById(R.id.tv_send_wifi);
        imgView_retry_connect = (ImageView) findViewById(R.id.imgView_retry_connect);
        imgView_search_mid = (ImageView) findViewById(R.id.imgView_search_mid);
        imgView_retry_search = (ImageView) findViewById(R.id.imgView_retry_search);

        edit_wifi_psw.addTextChangedListener(EditTextChangeListener);
        tv_act_skip.setOnClickListener(this);
        tv_confirm_guide.setOnClickListener(this);
        tv_send_wifi.setOnClickListener(this);
        imgView_retry_connect.setOnClickListener(this);
        imgView_retry_search.setOnClickListener(this);
        currShowView = layout_tip_connect;

        initAnimation();
    }

    /**
     * 文本改变监听
     */
    private TextWatcher EditTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (edit_wifi_psw.getText().length() != 0) {
                tv_error_text.setText("");
            }
        }
    };

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

        showAnimation = new AlphaAnimation(0.05f, 1.0f);
        showAnimation.setDuration(600);
        showAnimation.setRepeatCount(0);
        showAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (currShowView == layout_tip_connect) {
                    layout_tip_connect.setVisibility(View.VISIBLE);
                }
                if (currShowView == layout_setting_wifi) {
                    layout_setting_wifi.setVisibility(View.VISIBLE);
                }
                if (currShowView == layout_no_wifi) {
                    layout_no_wifi.setVisibility(View.VISIBLE);
                }
                if (currShowView == layout_scan_wifi) {
                    layout_scan_wifi.setVisibility(View.VISIBLE);
                    imgView_search_mid.startAnimation(rotateAnimation);
                }
                if (currShowView == layout_no_device) {
                    layout_no_device.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        hideAnimation = new AlphaAnimation(1.0f, 0.05f);
        hideAnimation.setDuration(300);
        hideAnimation.setRepeatCount(0);
        hideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (currHideView == layout_tip_connect) {
                    layout_tip_connect.setVisibility(View.INVISIBLE);
                }
                if (currHideView == layout_setting_wifi) {
                    layout_setting_wifi.setVisibility(View.INVISIBLE);
                }
                if (currHideView == layout_no_wifi) {
                    layout_no_wifi.setVisibility(View.INVISIBLE);
                }
                if (currHideView == layout_scan_wifi) {
                    layout_scan_wifi.setVisibility(View.INVISIBLE);
                }
                if (currHideView == layout_no_device) {
                    layout_no_device.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * 判断WIFI是否可用,了用的情况下获取WIFI名称
     *
     * @param flag 1->guide,2->no wifi,3->search back,4->no device
     */
    private void judgeWifiConnected(int flag) {
        switch (flag) {
            case 1:
                hideWithAnimation(layout_tip_connect);
                break;
            case 3:
                hideWithAnimation(layout_scan_wifi);
                break;
            case 4:
                hideWithAnimation(layout_no_device);
                break;
        }

        if (NetworkUtil.isWifiConnected(this)) {
            if (flag == 2) {
                hideWithAnimation(layout_no_wifi);
            }
            showWithAnimation(layout_setting_wifi);
            //----------------测试使用------------------
            edit_wifi_psw.setText("mianmianhome");
            //----------------测试使用------------------

            String wifi_name = NetworkUtil.getSSID(this);
            if (!TextUtils.isEmpty(wifi_name)) {
                tv_wifi_name.setText(wifi_name);
            }
        } else {
            if (flag == 2) {
                MUtils.showToast(context, getString(R.string.network_exception));
                return;
            }
            showWithAnimation(layout_no_wifi);
        }
    }

    /**
     * 展示View,同时添加animation
     *
     * @param v
     */
    private void showWithAnimation(View v) {
        currShowView = v;
        if (v == layout_scan_wifi) {
            imgView_act_back.setVisibility(View.VISIBLE);
        } else {
            imgView_act_back.setVisibility(View.GONE);
        }
        v.startAnimation(showAnimation);
    }

    /**
     * 隐藏View,同时添加animation
     *
     * @param v
     */
    private void hideWithAnimation(View v) {
        currHideView = v;
        if (v == layout_scan_wifi) {
            imgView_search_mid.clearAnimation();
        }
        v.startAnimation(hideAnimation);
    }

    /**
     * 强制隐藏输入法键盘
     */
    private void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_act_skip:
                MachtalkSDK.getInstance().setDeviceWiFiCancle();
                context.finish();
                break;
            case R.id.tv_confirm_guide:
                judgeWifiConnected(1);
                break;
            case R.id.tv_send_wifi:
                if (TextUtils.isEmpty(edit_wifi_psw.getText().toString().trim())) {
                    tv_error_text.setText(getString(R.string.error_no_wifi_psw));
                    return;
                }
                //收起手机键盘
                hideInput(context, edit_wifi_psw);

                hideWithAnimation(layout_setting_wifi);
                showWithAnimation(layout_scan_wifi);
                MachtalkSDK.getInstance().addDevice(tv_wifi_name.getText().toString().trim(),
                        edit_wifi_psw.getText().toString().trim(), MachtalkSDKConstant.NO_SCAN_CODE);
                break;
            case R.id.imgView_retry_connect:
                judgeWifiConnected(2);
                break;
            case R.id.imgView_retry_search:
                judgeWifiConnected(4);
                break;
            case R.id.imgView_act_back:
                MachtalkSDK.getInstance().setDeviceWiFiCancle();
                judgeWifiConnected(3);
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
                LogUtil.w("当前绑定设备ID是:   " + deviceId);
                //添加成功之后,跳转到已有设备列表界面
                startActivityForResult(new Intent(context, ExistingDeviceActivity.class),
                        MConstants.EXISTING_REQUEST);
            } else {
                //添加失败直接跳转错误界面
                hideWithAnimation(layout_scan_wifi);
                showWithAnimation(layout_no_device);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MConstants.EXISTING_REQUEST && resultCode == MConstants.EXISTING_RESULT) {
            hideWithAnimation(layout_scan_wifi);
            showWithAnimation(layout_setting_wifi);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(msdkListener);
        if (currShowView == layout_scan_wifi) {
            imgView_search_mid.startAnimation(rotateAnimation);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(msdkListener);
    }

    @Override
    protected void onDestroy() {
        MachtalkSDK.getInstance().setDeviceWiFiCancle();
        super.onDestroy();
        imgView_search_mid.clearAnimation();
    }
}
