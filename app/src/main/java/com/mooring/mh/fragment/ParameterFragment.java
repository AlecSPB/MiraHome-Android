package com.mooring.mh.fragment;

import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.DeviceStatus;
import com.machtalk.sdk.domain.DvidStatus;
import com.machtalk.sdk.domain.ReceivedDeviceMessage;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.activity.MainActivity;
import com.mooring.mh.activity.ParameterDetailActivity;
import com.mooring.mh.activity.SetWifiActivity;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 参数Fragment,实时更新
 * <p/>
 * Created by Will on 16/3/24.
 */
public class ParameterFragment extends BaseFragment implements View.OnClickListener,
        MainActivity.OnSwitchUserListener {

    private View layout_parameter;//有设备页
    private View layout_no_device;//无设备页
    private ImageView imgView_device_connect;

    private View layout_heart_rate;
    private View layout_breathing_rate;
    private View layout_body_movement;
    private View layout_humidity;
    private View layout_temperature;
    private View layout_bed_temperature;
    private View layout_light;
    private View layout_noise;

    private TextView tv_heart_rate;
    private TextView tv_breathing_rate;
    private TextView tv_body_movement;
    private TextView tv_humidity;
    private TextView tv_temperature;
    private TextView tv_bed_temperature;
    private TextView tv_light;
    private TextView tv_noise;
    private MSDKListener msdkListener;

    private int currUsers;
    private String deviceId;
    private AlphaAnimation alphaAnimation;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_parameter;
    }

    @Override
    protected void initFragment() {
        msdkListener = new MSDKListener();

        currUsers = sp.getInt(MConstants.SP_KEY_CURRUSERS, 0);
        deviceId = sp.getString(MConstants.DEVICE_ID, "");
    }

    @Override
    protected void initView() {

        layout_parameter = rootView.findViewById(R.id.layout_parameter);
        layout_no_device = rootView.findViewById(R.id.layout_no_device);
        imgView_device_connect = (ImageView) rootView.findViewById(R.id.imgView_device_connect);

        layout_heart_rate = rootView.findViewById(R.id.layout_heart_rate);
        layout_breathing_rate = rootView.findViewById(R.id.layout_breathing_rate);
        layout_body_movement = rootView.findViewById(R.id.layout_body_movement);
        layout_humidity = rootView.findViewById(R.id.layout_humidity);
        layout_temperature = rootView.findViewById(R.id.layout_temperature);
        layout_bed_temperature = rootView.findViewById(R.id.layout_bed_temperature);
        layout_light = rootView.findViewById(R.id.layout_light);
        layout_noise = rootView.findViewById(R.id.layout_noise);

        layout_heart_rate.setOnClickListener(this);
        layout_breathing_rate.setOnClickListener(this);
        layout_body_movement.setOnClickListener(this);
        layout_humidity.setOnClickListener(this);
        layout_temperature.setOnClickListener(this);
        layout_bed_temperature.setOnClickListener(this);
        layout_light.setOnClickListener(this);
        layout_noise.setOnClickListener(this);
        imgView_device_connect.setOnClickListener(this);

        tv_heart_rate = (TextView) rootView.findViewById(R.id.tv_heart_rate);
        tv_breathing_rate = (TextView) rootView.findViewById(R.id.tv_breathing_rate);
        tv_body_movement = (TextView) rootView.findViewById(R.id.tv_body_movement);
        tv_humidity = (TextView) rootView.findViewById(R.id.tv_humidity);
        tv_temperature = (TextView) rootView.findViewById(R.id.tv_temperature);
        tv_bed_temperature = (TextView) rootView.findViewById(R.id.tv_bed_temperature);
        tv_light = (TextView) rootView.findViewById(R.id.tv_light);
        tv_noise = (TextView) rootView.findViewById(R.id.tv_noise);

        alphaAnimation = new AlphaAnimation(0.05f, 1.0f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setRepeatCount(-1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        tv_heart_rate.setAnimation(alphaAnimation);
        tv_breathing_rate.setAnimation(alphaAnimation);
        tv_body_movement.setAnimation(alphaAnimation);
        tv_humidity.setAnimation(alphaAnimation);
        tv_temperature.setAnimation(alphaAnimation);
        tv_bed_temperature.setAnimation(alphaAnimation);
        tv_light.setAnimation(alphaAnimation);
        tv_noise.setAnimation(alphaAnimation);
        alphaAnimation.start();

//        //获取当前Device的详细信息
//        MachtalkSDK.getInstance().queryDeviceStatus(deviceId);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layout_heart_rate:
                jumpActivity(0, "88", "Heart rate");
                break;
            case R.id.layout_breathing_rate:
                jumpActivity(1, "88", "Breathing rate");
                break;
            case R.id.layout_body_movement:
                jumpActivity(2, "88", "Body movement");
                break;
            case R.id.layout_humidity:
                jumpActivity(3, "88", "Humidity");
                break;
            case R.id.layout_temperature:
                jumpActivity(4, "88", "Temperature");
                break;
            case R.id.layout_bed_temperature:
                jumpActivity(5, "88", "Bed temperature");
                break;
            case R.id.layout_light:
                jumpActivity(6, "88", "Light");
                break;
            case R.id.layout_noise:
                jumpActivity(7, "88", "Noise");
                break;
            case R.id.imgView_device_connect:
                startActivity(new Intent(context, SetWifiActivity.class));
                break;
        }
    }

    /**
     * 跳转到详情页
     *
     * @param id
     * @param param
     * @param title
     */
    private void jumpActivity(int id, String param, String title) {
        Intent it = new Intent();
        it.setClass(context, ParameterDetailActivity.class);
        it.putExtra("number", id);
        it.putExtra("param", param);
        it.putExtra("title", title);
        context.startActivity(it);
    }

    @Override
    public void onSwitch(int position) {

    }

    /**
     * 自定义回调监听
     */
    class MSDKListener extends MachtalkSDKListener {
        @Override
        public void onQueryDeviceStatus(Result result, DeviceStatus deviceStatus) {
            super.onQueryDeviceStatus(result, deviceStatus);
            LogUtil.e("onQueryDeviceStatus  " + result.getSuccess());
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS && deviceStatus != null
                    && deviceId.equals(deviceStatus.getDeviceId())) {
                List<DvidStatus> list = deviceStatus.getDeviceDvidStatuslist();
                parseDvidStatusList(list);
                layout_parameter.setVisibility(View.VISIBLE);
                layout_no_device.setVisibility(View.GONE);
            } else {
                MUtils.showToast(context, getResources().getString(R.string.device_not_online));
                layout_parameter.setVisibility(View.GONE);
                layout_no_device.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceiveDeviceMessage(Result result, ReceivedDeviceMessage rdm) {
            super.onReceiveDeviceMessage(result, rdm);
            LogUtil.e("result  " + result.getSuccess());
            if (rdm != null) {
                LogUtil.e("mDeviceId  " + rdm.getDeviceId() + " 操作回复 " + rdm.isRespMsg());
                if (rdm.isRespMsg()) {
                    List<DvidStatus> list = rdm.getDvidStatusList();
                    parseDvidStatusList(list);
                }
            }
        }
    }

    /**
     * 解析对应属性的值
     *
     * @param list
     */
    private void parseDvidStatusList(List<DvidStatus> list) {
        if (list != null) {
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
            for (int i = 0; i < list.size(); i++) {
                LogUtil.e("  dvid  " + list.get(i).getDvid() + " value  " + list.get(i).getValue());
                DvidStatus ds = list.get(i);
                if (MConstants.ATTR_ENVIR_HUMIDITY.equals(ds.getDvid())) {//环境湿度
                    tv_humidity.setText(list.get(i).getValue());
                }
                if (MConstants.ATTR_ENVIR_TEMPERATURE.equals(ds.getDvid())) {//环境温度----温度
                    tv_temperature.setText(list.get(i).getValue());
                }
                if (MConstants.ATTR_ENVIR_LIGHT.equals(ds.getDvid())) {//环境光照
                    tv_light.setText(list.get(i).getValue());
                }
                if (MConstants.ATTR_ENVIR_NOISE.equals(ds.getDvid())) {//环境噪声
                    tv_noise.setText(list.get(i).getValue());
                }
                if (currUsers == MConstants.LEFT_USER) {
                    if (MConstants.ATTR_LEFT_HEART_RATE.equals(ds.getDvid())) {//左边心率
                        tv_heart_rate.setText(list.get(i).getValue());
                    }
                    if (MConstants.ATTR_LEFT_RESP_RATE.equals(ds.getDvid())) {//左边呼吸频率
                        tv_breathing_rate.setText(list.get(i).getValue());
                    }
                    if (MConstants.ATTR_LEFT_MOVEMENT.equals(ds.getDvid())) {//左边体动
                        tv_body_movement.setText(list.get(i).getValue());
                    }
                    if (MConstants.ATTR_LEFT_ACTUAL_TEMP.equals(ds.getDvid())) {//左边实际温度
                        tv_bed_temperature.setText(list.get(i).getValue());
                    }
                }
                if (currUsers == MConstants.RIGHT_USER) {
                    if (MConstants.ATTR_RIGHT_HEART_RATE.equals(ds.getDvid())) {//右边心率
                        tv_heart_rate.setText(list.get(i).getValue());
                    }
                    if (MConstants.ATTR_RIGHT_RESP_RATE.equals(ds.getDvid())) {//右边呼吸频率
                        tv_breathing_rate.setText(list.get(i).getValue());
                    }
                    if (MConstants.ATTR_RIGHT_MOVEMENT.equals(ds.getDvid())) {//右边体动
                        tv_body_movement.setText(list.get(i).getValue());
                    }
                    if (MConstants.ATTR_RIGHT_ACTUAL_TEMP.equals(ds.getDvid())) {//右边实际温度
                        tv_bed_temperature.setText(list.get(i).getValue());
                    }
                }
            }
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
            MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(context);
        MachtalkSDK.getInstance().setSdkListener(msdkListener);
        //每次跳转回来执行一次请求,防止设备断开
//        MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alphaAnimation != null) {
            alphaAnimation.cancel();
            alphaAnimation = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(msdkListener);
    }
}
