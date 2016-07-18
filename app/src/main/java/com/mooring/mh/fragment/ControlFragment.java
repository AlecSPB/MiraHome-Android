package com.mooring.mh.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewStub;

import com.mooring.mh.R;
import com.mooring.mh.activity.DryingControlActivity;
import com.mooring.mh.activity.HeatingControlActivity;
import com.mooring.mh.activity.SetWifiActivity;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 第二个fragment 负责控制仪器的温度
 * <p/>
 * Created by Will on 16/3/24.
 */
public class ControlFragment extends BaseFragment implements View.OnClickListener, SwitchUserObserver {

    private View layout_control;
    private View layout_no_device;//无设备去连接
    private View layout_heating;
    private View layout_drying;
    private int isDeviceExist = 0;//设备是否存在,0:默认,1:存在,2:不存在

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_control;
    }

    @Override
    protected void initFragment() {
    }

    @Override
    protected void initView() {

        layout_control = rootView.findViewById(R.id.layout_control);
        layout_heating = rootView.findViewById(R.id.layout_heating);
        layout_drying = rootView.findViewById(R.id.layout_drying);

        layout_heating.setOnClickListener(this);
        layout_drying.setOnClickListener(this);
    }

    /**
     * 判断设备是否在线
     */
    private void judgeDeviceIsOnline() {
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
        layout_control.setVisibility(View.GONE);
        if (layout_no_device == null) {
            ViewStub viewStub = (ViewStub) rootView.findViewById(R.id.VStub_no_device);
            layout_no_device = viewStub.inflate();
        } else {
            layout_no_device.setVisibility(View.VISIBLE);
        }
        View view = rootView.findViewById(R.id.no_device_to_conn);
        View imgView_device_connect = view.findViewById(R.id.imgView_device_connect);
        imgView_device_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //连接设备
                context.startActivity(new Intent(context, SetWifiActivity.class));
            }
        });
    }

    /**
     * 隐藏无设备去连接界面
     */
    private void hideNoDeviceView() {
        if (isDeviceExist == 1) return;
        layout_control.setVisibility(View.VISIBLE);
        if (layout_no_device != null) {
            layout_no_device.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent();
        switch (v.getId()) {
            case R.id.layout_heating:
                it.setClass(context, HeatingControlActivity.class);
                context.startActivity(it);
                break;
            case R.id.layout_drying:
                it.setClass(context, DryingControlActivity.class);
                context.startActivity(it);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            judgeDeviceIsOnline();
        }
    }

    @Override
    public void onSwitch(String userId, int location, String fTag) {
        if (!isVisible()) return;
        if (location == MConstants.OBSERVER_DEVICE_STATUS) {//设备上下线
            if (fTag.equals(MConstants.DEVICE_ONLINE + "")) {
                hideNoDeviceView();
                isDeviceExist = 1;
            } else if (fTag.equals(MConstants.DEVICE_OFFLINE + "")) {
                showNoDeviceView();
                isDeviceExist = 2;
            }
        }
    }

    @Override
    protected void OnResume() {
        if (!isVisible()) return;
        //判断设备是否在线
        judgeDeviceIsOnline();

        MobclickAgent.onPageStart("Control");
    }

    @Override
    protected void OnPause() {
        if (!isVisible()) return;
        MobclickAgent.onPageEnd("Control");
    }
}

