package com.mooring.mh.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.AidStatus;
import com.machtalk.sdk.domain.ReceivedDeviceMessage;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.utils.ActivityCollector;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.mooring.mh.views.other.CommonDialog;
import com.umeng.message.PushAgent;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 自定义BaseActivity for common
 * <p/>
 * Created by Will on 16/3/30.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected AppCompatActivity context;//上下文
    private MachtalkSDKListener baseListener;//全局监听
    protected SharedPreferences sp;//sp
    protected SharedPreferences.Editor editor;//editor
    protected ImageView imgView_act_back;//Activity返回
    protected TextView tv_act_title;//Activity的title
    protected boolean self_control_back = false;//是否自主控制返回按钮事件
    private boolean noNetwork = false;//是否无网络

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ActivityCollector.add(this);

        if (getLayoutId() == 0) {
            throw new IllegalStateException("Layout files can not be empty");
        }
        setContentView(getLayoutId());

        sp = InitApplicationHelper.sp;
        editor = InitApplicationHelper.sp.edit();
        editor.apply();

        //如果不调用此方法，不仅会导致按照"几天不活跃"条件来推送失效
        PushAgent.getInstance(context).onAppStart();

        baseListener = new BaseListener();

        initActivity();

        //title为null时不设置返回和title,title为空时,只设置返回监听
        if (getTitleName() != null) {
            imgView_act_back = (ImageView) findViewById(R.id.imgView_act_back);
            if (imgView_act_back != null) {
                imgView_act_back.setOnClickListener(this);
            }
            if (!"".equals(getTitleName())) {
                tv_act_title = (TextView) findViewById(R.id.tv_act_title);
                if (tv_act_title != null && !TextUtils.isEmpty(getTitleName())) {
                    tv_act_title.setText(getTitleName());
                }
            }
        }

        initView();
    }

    /**
     * 获取布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 获取title名称
     *
     * @return
     */
    protected abstract String getTitleName();

    /**
     * 初始化Activity相关参数和变量,和view无关
     */
    protected abstract void initActivity();

    /**
     * 初始化View控件
     */
    protected abstract void initView();

    /**
     * 点击事件
     *
     * @param v
     */
    protected abstract void OnClick(View v);

    class BaseListener extends MachtalkSDKListener {
        @Override
        public void onServerConnectStatusChanged(MachtalkSDKConstant.ServerConnStatus scs) {
            super.onServerConnectStatusChanged(scs);
            if (scs == MachtalkSDKConstant.ServerConnStatus.RECONNECTING) {
                //后台重新登录中（关闭自动登录功能后将不会再有通知）
                MUtils.showLoadingDialog(context, "连接中...");
            } else {
                MUtils.hideLoadingDialog();
            }
            if (scs == MachtalkSDKConstant.ServerConnStatus.CONNECT_TIMEOUT ||
                    scs == MachtalkSDKConstant.ServerConnStatus.CONNECT_BREAK) {
                if (!noNetwork) {
                    //网络不可用
                    MUtils.showToast(context, "无法连接到服务器");
                }
                if (scs == MachtalkSDKConstant.ServerConnStatus.CONNECT_BREAK) {
                    //服务中断
                    editor.putBoolean(MConstants.DEVICE_WAN_ONLINE, false);
                    editor.putBoolean(MConstants.DEVICE_LAN_ONLINE, false);
                    editor.apply();
                }
            } else if (scs == MachtalkSDKConstant.ServerConnStatus.NETWORK_UNAVAILABLE) {
                //网络连接不可用（手机断开网络连接时会通知）
                noNetwork = true;
                MUtils.showToast(context, "网络连接不可用");
            } else if (scs == MachtalkSDKConstant.ServerConnStatus.NETWORK_RECOVERY) {
                //网络连接恢复（手机重新连接上了网络）
                noNetwork = false;
                MUtils.showToast(context, "网络已连接");
            }
            //登录被挤掉
            if (scs == MachtalkSDKConstant.ServerConnStatus.LOGOUT_KICKOFF) {
                logoutAndRetryLogin();
            }
        }

        @Override
        public void onReceiveDeviceMessage(Result result, ReceivedDeviceMessage rdm) {
            super.onReceiveDeviceMessage(result, rdm);
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS && rdm != null && rdm.getAidStatusList() != null) {
                List<AidStatus> deviceStatus = rdm.getAidStatusList();
                for (int i = 0; i < deviceStatus.size(); i++) {
                    if (MConstants.ATTR_IS_CONNECTED.equals(deviceStatus.get(i).getValue())) {
                        //下面MConstants.ATTR_IS_CONNECTED担当两个不同角色
                        boolean conn = sp.getBoolean(MConstants.ATTR_IS_CONNECTED, false);
                        //最新的状态值保存下来
                        editor.putBoolean(MConstants.ATTR_IS_CONNECTED,
                                "1".equals(deviceStatus.get(i).getValue())).apply();
                        if (conn && "0".equals(deviceStatus.get(i).getValue())) {
                            showBlanketsOffDialog();
                        } else if (!conn && "1".equals(deviceStatus.get(i).getValue())) {
                            MUtils.showToast(context, "毯子已和控制器连接");
                        }
                    }
                }
            }
        }

        @Override
        public void onDeviceOnOffline(String deviceId, MachtalkSDKConstant.DeviceOnOffline status) {
            super.onDeviceOnOffline(deviceId, status);
            deviceOnOffLine(deviceId, status);
        }
    }

    /**
     * 设备上下线
     *
     * @param deviceId 设备ID
     * @param status
     */
    protected void deviceOnOffLine(String deviceId, MachtalkSDKConstant.DeviceOnOffline status) {
        LogUtil.w("onDeviceOnOffline:" + status + "," + deviceId);
        if (sp.getString(MConstants.DEVICE_ID, "").equals(deviceId)) {
            if (status == MachtalkSDKConstant.DeviceOnOffline.DEVICE_WAN_ONLINE) {//广域网上线
                editor.putBoolean(MConstants.DEVICE_WAN_ONLINE, true).apply();
            } else if (status == MachtalkSDKConstant.DeviceOnOffline.DEVICE_WAN_OFFLINE) {
                editor.putBoolean(MConstants.DEVICE_WAN_ONLINE, false).apply();
            } else if (status == MachtalkSDKConstant.DeviceOnOffline.DEVICE_LAN_ONLINE) {
                editor.putBoolean(MConstants.DEVICE_LAN_ONLINE, true).apply();
            } else if (status == MachtalkSDKConstant.DeviceOnOffline.DEVICE_LAN_OFFLINE) {
                editor.putBoolean(MConstants.DEVICE_LAN_ONLINE, false).apply();
            }
        }
    }

    /**
     * 检测到用户被挤出登录
     */
    protected void logoutAndRetryLogin() {
        finish();
    }

    /**
     * 提示用户毯子断开连接
     */
    private void showBlanketsOffDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.tip_disconnect_blanket));
        builder.setLogo(R.drawable.img_blanket_disconnect);
        builder.setCanceledOnTouchOtherPlace(false);
        builder.setPositiveButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        if (!self_control_back && v.getId() == R.id.imgView_act_back) {
            context.finish();
        }
        OnClick(v);
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
        ActivityCollector.remove(this);
        super.onDestroy();
        System.gc();
    }
}
