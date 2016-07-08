package com.mooring.mh.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.AidStatus;
import com.machtalk.sdk.domain.DeviceStatus;
import com.machtalk.sdk.domain.ReceivedDeviceMessage;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.mooring.mh.views.CircleProgress.DryingCircleView;
import com.mooring.mh.views.other.CommonDialog;
import com.mooring.mh.views.other.CustomToggle;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class DryingControlActivity extends BaseActivity implements CustomToggle.OnCheckedChangeListener {

    private DryingCircleView dryingCircleView;
    private TextView tv_drying_totalTimes;
    private CustomToggle toggle_drying;

    private Timer timer;
    private TimerTask timerTask;
    private long totalTimes = 30 * 60 * 1000;//默认烘干时间30分钟,保留毫秒为单位为了方便动画加载
    private int clockTime = 0;//倒计时使用
    private String deviceId;//设备ID
    private MSDKListener msdkListener;//自定义SDK回调监听
    private boolean isPassiveOpenDrying = false;//是否被动开启烘干

    @Override
    protected int getLayoutId() {
        return R.layout.activity_drying_control;
    }

    @Override
    protected String getTitleName() {
        return getString(R.string.title_drying);
    }

    @Override
    protected void initActivity() {

        deviceId = sp.getString(MConstants.DEVICE_ID, "");
        msdkListener = new MSDKListener();
    }

    @Override
    protected void initView() {
        dryingCircleView = (DryingCircleView) findViewById(R.id.dryingCircleView);
        toggle_drying = (CustomToggle) findViewById(R.id.toggle_drying);
        tv_drying_totalTimes = (TextView) findViewById(R.id.tv_drying_times);

        toggle_drying.setOnCheckedChange(this);
        toggle_drying.setChecked(false);//初始化关闭

        // 上一次烘干没结束的情况下,继续烘干
        RestoreDryingTimes();
    }

    /**
     * 恢复当前烘干时间
     */
    private void RestoreDryingTimes() {
        if (!sp.getBoolean(MConstants.ATTR_DRYING_SWITCH, false)) {
            MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
            return;
        }
        long startTime = sp.getLong(MConstants.ATTR_DRYING_ON_TIME, 0);
        long dryingTimes = sp.getLong(MConstants.ATTR_DRYING_TIME, 0);
        if (startTime != 0 && dryingTimes != 0) {
            openDryingWithParam(startTime, dryingTimes);
        }
    }

    /**
     * 开启烘干
     *
     * @param startTime
     * @param dryingTime
     */
    private void openDryingWithParam(long startTime, long dryingTime) {
        long curr = System.currentTimeMillis();
        if (curr - startTime >= dryingTime) {
            editor.putBoolean(MConstants.ATTR_DRYING_SWITCH, false).apply();
            return;
        }
        toggle_drying.setChecked(true);
        dryingCircleView.setAnimDuration(dryingTime - (curr - startTime));
        dryingCircleView.startAnim((float) (curr - startTime) / dryingTime);
        clockTime = (int) (curr - startTime) / 1000;
        initTimer();
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 开启烘干
     */
    private void openDrying() {
        toggle_drying.setChecked(true);
        initTimer();
        timer.schedule(timerTask, 0, 1000);
        dryingCircleView.setAnimDuration(totalTimes);
        dryingCircleView.startAnim();
        clockTime = 0;

        editor.putLong(MConstants.ATTR_DRYING_ON_TIME, System.currentTimeMillis());
        editor.putLong(MConstants.ATTR_DRYING_TIME, totalTimes);
        editor.putBoolean(MConstants.ATTR_DRYING_SWITCH, true);
        editor.apply();
    }

    /**
     * 关闭烘干
     */
    private void closeDrying() {
        toggle_drying.setChecked(false);
        clearTimer();
        clockTime = 0;
        dryingCircleView.resetView();
        editor.putBoolean(MConstants.ATTR_DRYING_SWITCH, false).apply();
        tv_drying_totalTimes.setText(getString(R.string.blank_time));
    }

    /**
     * show提示dialog
     */
    private void showDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage(getString(R.string.tip_stop_drying));
        builder.setLogo(R.drawable.img_close_drying);
        builder.setCanceledOnTouchOtherPlace(false);
        builder.setPositiveButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消烘干
                MachtalkSDK.getInstance().operateDevice(deviceId,
                        new String[]{MConstants.ATTR_DRYING_SWITCH},
                        new String[]{"0"});
                MUtils.showLoadingDialog(context);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 完成烘干
     */
    private void showCompleteDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage(getString(R.string.tip_drying_success));
        builder.setLogo(R.drawable.img_drying_complete);
        builder.setCanceledOnTouchOtherPlace(false);
        builder.setPositiveButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 发送消息
     */
    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv_drying_totalTimes.setText(calculateTimes(totalTimes - clockTime * 1000));
            clockTime++;
            if (clockTime * 1000 > totalTimes) {
                //加载完毕执行重置
                dryingCircleView.resetView();
                clearTimer();
                clockTime = 0;
                toggle_drying.setChecked(false);
                editor.putBoolean(MConstants.ATTR_DRYING_SWITCH, false).apply();
                showCompleteDialog();
            }
        }
    };

    /**
     * 格式化时间显示
     *
     * @param totalTimes
     * @return
     */
    private String calculateTimes(long totalTimes) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return sdf.format(totalTimes);
    }

    /**
     * 初始化计时
     */
    private void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                han.sendEmptyMessage(0x01);
            }
        };
    }

    /**
     * 清除计时
     */
    private void clearTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void OnClick(View v) {
    }

    @Override
    public void onCheckedChanged(View v, boolean isChecked) {
        if (isChecked) {
//            MachtalkSDK.getInstance().operateDevice(deviceId,
//                    new String[]{MConstants.ATTR_DRYING_TIME},
//                    new String[]{String.valueOf(totalTimes / 1000)});
            MachtalkSDK.getInstance().operateDevice(deviceId,
                    new String[]{MConstants.ATTR_DRYING_SWITCH},
                    new String[]{"1"});
            MUtils.showLoadingDialog(context);
        } else {
            showDialog(); //弹出提示
        }
    }

    /**
     * 自定义回调监听
     */
    class MSDKListener extends MachtalkSDKListener {

        @Override
        public void onQueryDeviceStatus(Result result, DeviceStatus ds) {
            super.onQueryDeviceStatus(result, ds);
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS && ds != null && deviceId.equals(ds.getDeviceId())) {
                List<AidStatus> dsList = ds.getDeviceAidStatuslist();
                if (dsList == null) return;
                String drySwitch = "";
                long startTime = 0, dryTime = 0;
                for (AidStatus as : dsList) {
                    if (MConstants.ATTR_DRYING_SWITCH.equals(as.getAid())) {
                        drySwitch = as.getValue();
                    }
                    if (MConstants.ATTR_DRYING_ON_TIME.equals(as.getAid())) {
                        startTime = Long.parseLong(as.getValue());
                    }
                    if (MConstants.ATTR_DRYING_TIME.equals(as.getAid())) {
                        dryTime = Long.parseLong(as.getValue());
                    }
                }
                if ("1".equals(drySwitch)) {//设备记录的开启和烘干时间单位:秒
                    openDryingWithParam(startTime * 1000, dryTime * 1000);
                }
            }
        }

        @Override
        public void onReceiveDeviceMessage(Result result, ReceivedDeviceMessage rdm) {
            super.onReceiveDeviceMessage(result, rdm);
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS && rdm != null && deviceId.equals(rdm.getDeviceId())) {
                List<AidStatus> dsList = rdm.getAidStatusList();
                if (dsList == null) return;
                for (AidStatus as : dsList) {
                    if (MConstants.ATTR_DRYING_SWITCH.equals(as.getAid())) {
                        if ("1".equals(as.getValue())) {
                            openDrying();//此处要做手动操作判断,否则会出现重复回调
                            if (rdm.isRespMsg()) MUtils.hideLoadingDialog();
                        } else if ("0".equals(as.getValue())) {
                            closeDrying();
                            if (rdm.isRespMsg()) MUtils.hideLoadingDialog();
                        }
                    }
//                    if (MConstants.ATTR_DRYING_TIME.equals(as.getAid())) {
//                        totalTimes = Long.parseLong(as.getValue()) * 1000;
//                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(msdkListener);
        MobclickAgent.onPageStart("DryingControl");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(msdkListener);
        MobclickAgent.onPageEnd("DryingControl");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        dryingCircleView.resetView();
        clockTime = 0;
        clearTimer();
        super.onDestroy();
    }
}
