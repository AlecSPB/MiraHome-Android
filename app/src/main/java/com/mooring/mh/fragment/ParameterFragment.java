package com.mooring.mh.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.AidStatus;
import com.machtalk.sdk.domain.DeviceStatus;
import com.machtalk.sdk.domain.ReceivedDeviceMessage;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.activity.ParameterDetailActivity;
import com.mooring.mh.activity.SetWifiActivity;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 参数Fragment,实时更新
 * <p/>
 * Created by Will on 16/3/24.
 */
public class ParameterFragment extends BaseFragment implements View.OnClickListener, SwitchUserObserver {

    private View layout_parameter;//有设备页
    private View layout_no_device;//无设备去连接

    private View layout_heart_rate;//心率
    private View layout_breathing_rate;//呼吸频率
    private View layout_body_movement;//体动
    private View layout_humidity;//湿度
    private View layout_temperature;//温度
    private View layout_bed_temperature;//床温
    private View layout_light;//光照值
    private View layout_noise;//噪声值

    private TextView tv_heart_rate;
    private TextView tv_breathing_rate;
    private TextView tv_body_movement;
    private TextView tv_humidity;
    private TextView tv_temperature;
    private TextView tv_unit_temp;
    private TextView tv_bed_temperature;
    private TextView tv_unit_bed_temp;
    private TextView tv_light;
    private TextView tv_noise;
    private MSDKListener msdkListener;

    private int currLocation;//当前用户
    private AlphaAnimation alphaAnimation;//显示动画
    private boolean isRefresh = false;//是否自动刷新数据
    private int isDeviceExist = 0;//设备是否存在,0:默认,1:存在,2:不存在

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_parameter;
    }

    @Override
    protected void initFragment() {

        msdkListener = new MSDKListener();
        currLocation = MUtils.getCurrUserLocation();
    }

    @Override
    protected void initView() {

        layout_parameter = rootView.findViewById(R.id.layout_parameter);
        layout_heart_rate = rootView.findViewById(R.id.layout_heart_rate);
        layout_breathing_rate = rootView.findViewById(R.id.layout_breathing_rate);
        layout_body_movement = rootView.findViewById(R.id.layout_body_movement);
        layout_humidity = rootView.findViewById(R.id.layout_humidity);
        layout_temperature = rootView.findViewById(R.id.layout_temperature);
        layout_bed_temperature = rootView.findViewById(R.id.layout_bed_temperature);
        layout_light = rootView.findViewById(R.id.layout_light);
        layout_noise = rootView.findViewById(R.id.layout_noise);

        tv_heart_rate = (TextView) rootView.findViewById(R.id.tv_heart_rate);
        tv_breathing_rate = (TextView) rootView.findViewById(R.id.tv_breathing_rate);
        tv_body_movement = (TextView) rootView.findViewById(R.id.tv_body_movement);
        tv_humidity = (TextView) rootView.findViewById(R.id.tv_humidity);
        tv_temperature = (TextView) rootView.findViewById(R.id.tv_temperature);
        tv_unit_temp = (TextView) rootView.findViewById(R.id.tv_unit_temp);
        tv_bed_temperature = (TextView) rootView.findViewById(R.id.tv_bed_temperature);
        tv_unit_bed_temp = (TextView) rootView.findViewById(R.id.tv_unit_bed_temp);
        tv_light = (TextView) rootView.findViewById(R.id.tv_light);
        tv_noise = (TextView) rootView.findViewById(R.id.tv_noise);

        layout_heart_rate.setOnClickListener(this);
        layout_breathing_rate.setOnClickListener(this);
        layout_body_movement.setOnClickListener(this);
        layout_humidity.setOnClickListener(this);
        layout_temperature.setOnClickListener(this);
        layout_bed_temperature.setOnClickListener(this);
        layout_light.setOnClickListener(this);
        layout_noise.setOnClickListener(this);

        //初始化温度单位,防止在当前fragment没有attach上观察者之前,更改温度单位
        if (sp.getBoolean(MConstants.TEMPERATURE_UNIT, true)) {
            tv_unit_temp.setText(getString(R.string.unit_celsius));
            tv_unit_bed_temp.setText(getString(R.string.unit_celsius));
        } else {
            tv_unit_temp.setText(getString(R.string.unit_fahrenheit));
            tv_unit_bed_temp.setText(getString(R.string.unit_fahrenheit));
        }

    }

    /**
     * 判断设备是否在线
     */
    private void judgeDeviceIsOnline() {
        LogUtil.e(sp.getBoolean(MConstants.DEVICE_LAN_ONLINE, false) + "—LAN————ONLINE—" +
                sp.getBoolean(MConstants.DEVICE_WAN_ONLINE, false));

        if (MUtils.isCurrDeviceOnline()) {
            hideNoDeviceView();
            isDeviceExist = 1;
        } else {
            showNoDeviceView();
            isDeviceExist = 2;
        }
    }

    /**
     * 显示无设备去连接界面
     */
    private void showNoDeviceView() {
        if (isDeviceExist == 2) return;
        layout_parameter.setVisibility(View.GONE);
        if (layout_no_device == null) {
            ViewStub viewStub = (ViewStub) rootView.findViewById(R.id.VStub_no_device);
            layout_no_device = viewStub.inflate();
        } else {
            layout_no_device.setVisibility(View.VISIBLE);
        }
        stopAnimation();
        View view = rootView.findViewById(R.id.no_device_to_conn);
        View imgView_device_connect = view.findViewById(R.id.imgView_device_connect);
        imgView_device_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //连接设备
                startActivity(new Intent(context, SetWifiActivity.class));
            }
        });
    }

    /**
     * 隐藏无设备去连接界面
     */
    private void hideNoDeviceView() {
        MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
        if (isDeviceExist == 1) return;
        layout_parameter.setVisibility(View.VISIBLE);
        if (layout_no_device != null) {
            layout_no_device.setVisibility(View.GONE);
        }
        startAnimation();//执行动画
    }

    /**
     * 开始动画加载
     */
    private void startAnimation() {
        isRefresh = false;//首先停止刷新
        if (alphaAnimation == null) {
            alphaAnimation = new AlphaAnimation(0.05f, 1.0f);
            alphaAnimation.setDuration(1500);
            alphaAnimation.setRepeatCount(-1);
            alphaAnimation.setRepeatMode(Animation.REVERSE);
        }
        tv_heart_rate.setText(getString(R.string.wait_loading));
        tv_breathing_rate.setText(getString(R.string.wait_loading));
        tv_body_movement.setText(getString(R.string.wait_loading));
        tv_humidity.setText(getString(R.string.wait_loading));
        tv_temperature.setText(getString(R.string.wait_loading));
        tv_bed_temperature.setText(getString(R.string.wait_loading));
        tv_light.setText(getString(R.string.wait_loading));
        tv_noise.setText(getString(R.string.wait_loading));
        //添加动画
        tv_heart_rate.setAnimation(alphaAnimation);
        tv_breathing_rate.setAnimation(alphaAnimation);
        tv_body_movement.setAnimation(alphaAnimation);
        tv_humidity.setAnimation(alphaAnimation);
        tv_temperature.setAnimation(alphaAnimation);
        tv_bed_temperature.setAnimation(alphaAnimation);
        tv_light.setAnimation(alphaAnimation);
        tv_noise.setAnimation(alphaAnimation);
        alphaAnimation.start();
    }

    /**
     * 停止动画加载
     */
    private void stopAnimation() {
        if (alphaAnimation != null) {
            tv_heart_rate.clearAnimation();
            tv_breathing_rate.clearAnimation();
            tv_body_movement.clearAnimation();
            tv_humidity.clearAnimation();
            tv_temperature.clearAnimation();
            tv_bed_temperature.clearAnimation();
            tv_light.clearAnimation();
            tv_noise.clearAnimation();
            alphaAnimation.cancel();
            alphaAnimation = null;
        }
    }

    /**
     * 跳转到详情页
     *
     * @param id
     * @param param
     * @param title
     */
    private void jumpActivity(int id, TextView param, String title) {
        Intent it = new Intent();
        it.setClass(context, ParameterDetailActivity.class);
        it.putExtra("number", id);
        it.putExtra("param", param.getText().toString());
        it.putExtra("title", title);
        context.startActivity(it);
    }

    /**
     * 解析对应属性的值
     *
     * @param list
     */
    private void parseAidStatusList(List<AidStatus> list) {
        if (list == null) return;
        stopAnimation();//停止动画加载
        for (int i = 0; i < list.size(); i++) {
            AidStatus ds = list.get(i);
            if (MConstants.ATTR_ENVIR_HUMIDITY.equals(ds.getAid())) {//环境湿度
                tv_humidity.setText(list.get(i).getValue());
            }
            if (MConstants.ATTR_ENVIR_TEMPERATURE.equals(ds.getAid())) {//环境温度----温度
                tv_temperature.setText(list.get(i).getValue());
            }
            if (MConstants.ATTR_ENVIR_LIGHT.equals(ds.getAid())) {//环境光照
                tv_light.setText(list.get(i).getValue());
            }
            if (MConstants.ATTR_ENVIR_NOISE.equals(ds.getAid())) {//环境噪声
                tv_noise.setText(list.get(i).getValue());
            }
            if (currLocation == MConstants.LEFT_USER) {
                if (MConstants.ATTR_LEFT_HEART_RATE.equals(ds.getAid())) {//左边心率
                    tv_heart_rate.setText(list.get(i).getValue());
                }
                if (MConstants.ATTR_LEFT_RESP_RATE.equals(ds.getAid())) {//左边呼吸频率
                    tv_breathing_rate.setText(list.get(i).getValue());
                }
                if (MConstants.ATTR_LEFT_MOVEMENT.equals(ds.getAid())) {//左边体动
                    tv_body_movement.setText(list.get(i).getValue());
                }
                if (MConstants.ATTR_LEFT_ACTUAL_TEMP.equals(ds.getAid())) {//左边实际温度
                    tv_bed_temperature.setText(list.get(i).getValue());
                }
            }
            if (currLocation == MConstants.RIGHT_USER) {
                if (MConstants.ATTR_RIGHT_HEART_RATE.equals(ds.getAid())) {//右边心率
                    tv_heart_rate.setText(list.get(i).getValue());
                }
                if (MConstants.ATTR_RIGHT_RESP_RATE.equals(ds.getAid())) {//右边呼吸频率
                    tv_breathing_rate.setText(list.get(i).getValue());
                }
                if (MConstants.ATTR_RIGHT_MOVEMENT.equals(ds.getAid())) {//右边体动
                    tv_body_movement.setText(list.get(i).getValue());
                }
                if (MConstants.ATTR_RIGHT_ACTUAL_TEMP.equals(ds.getAid())) {//右边实际温度
                    tv_bed_temperature.setText(list.get(i).getValue());
                }
            }
        }
    }

    /**
     * 自定义回调监听
     */
    class MSDKListener extends MachtalkSDKListener {

        @Override
        public void onQueryDeviceStatus(Result result, DeviceStatus deviceStatus) {
            super.onQueryDeviceStatus(result, deviceStatus);
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS && deviceStatus != null
                    && deviceId.equals(deviceStatus.getDeviceId())) {
                parseAidStatusList(deviceStatus.getDeviceAidStatuslist());
                isRefresh = true;
            } else {
                MUtils.showToast(context, getString(R.string.check_device_online));
            }
        }

        @Override
        public void onReceiveDeviceMessage(Result result, ReceivedDeviceMessage rdm) {
            super.onReceiveDeviceMessage(result, rdm);
            if (!isRefresh) return;
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS && rdm != null && deviceId.equals(rdm.getDeviceId())) {
                parseAidStatusList(rdm.getAidStatusList());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_heart_rate:
                jumpActivity(0, tv_heart_rate, getString(R.string.tv_heart_rate));
                break;
            case R.id.layout_breathing_rate:
                jumpActivity(1, tv_breathing_rate, getString(R.string.tv_breathing_rate));
                break;
            case R.id.layout_body_movement:
                jumpActivity(2, tv_body_movement, getString(R.string.tv_body_movement));
                break;
            case R.id.layout_humidity:
                jumpActivity(3, tv_humidity, getString(R.string.tv_humidity));
                break;
            case R.id.layout_temperature:
                jumpActivity(4, tv_temperature, getString(R.string.tv_temperature));
                break;
            case R.id.layout_bed_temperature:
                jumpActivity(5, tv_bed_temperature, getString(R.string.tv_bed_temperature));
                break;
            case R.id.layout_light:
                jumpActivity(6, tv_light, getString(R.string.tv_light));
                break;
            case R.id.layout_noise:
                jumpActivity(7, tv_noise, getString(R.string.tv_noise));
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MachtalkSDK.getInstance().removeSdkListener(msdkListener);
        } else {
            MachtalkSDK.getInstance().setContext(context);
            MachtalkSDK.getInstance().setSdkListener(msdkListener);

            //执行判断设备在线状态
            judgeDeviceIsOnline();

            //获取当前的User,如果没有改变,则不做切换
            if (currLocation != MUtils.getCurrUserLocation()) {
                currLocation = MUtils.getCurrUserLocation();
                if (!MUtils.isCurrDeviceOnline()) return;//设备不在线直接跳出
                startAnimation();
                MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
            }
        }
    }

    @Override
    public void onSwitch(String userId, int location, String fTag) {
        if (MConstants.OBSERVER_TEMP_UNIT == location) {
            if (fTag.equals(MConstants.DEGREES_C + "")) {
                tv_unit_temp.setText(getString(R.string.unit_celsius));
                tv_unit_bed_temp.setText(getString(R.string.unit_celsius));
            } else if (fTag.equals(MConstants.DEGREES_F + "")) {
                tv_unit_temp.setText(getString(R.string.unit_fahrenheit));
                tv_unit_bed_temp.setText(getString(R.string.unit_fahrenheit));
            }
        }
        if (!isVisible()) return;
        if (!TextUtils.isEmpty(userId)) {//切换头像时
            if (!MUtils.isCurrDeviceOnline()) return;
            currLocation = location;
            startAnimation();
            MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
        } else {
            switch (location) {
                case MConstants.OBSERVER_DEVICE_STATUS:
                    if (fTag.equals(MConstants.DEVICE_ONLINE + "")) {
                        hideNoDeviceView();
                        isDeviceExist = 1;
                    } else if (fTag.equals(MConstants.DEVICE_OFFLINE + "")) {
                        showNoDeviceView();
                        isDeviceExist = 2;
                    }
                    break;
            }
        }
    }

    @Override
    protected void OnResume() {
        //执行判断设备在线状态
        judgeDeviceIsOnline();

        MachtalkSDK.getInstance().setContext(context);
        MachtalkSDK.getInstance().setSdkListener(msdkListener);
        MobclickAgent.onPageStart("Parameter");
    }

    @Override
    protected void OnPause() {
        MachtalkSDK.getInstance().removeSdkListener(msdkListener);
        MobclickAgent.onPageEnd("Parameter");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAnimation();
    }
}
