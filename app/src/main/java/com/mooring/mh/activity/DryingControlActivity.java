package com.mooring.mh.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.ReceivedDeviceMessage;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.mooring.mh.views.CircleProgress.DryingCircleView;
import com.mooring.mh.views.CommonDialog;
import com.mooring.mh.views.CustomToggle;

import org.xutils.common.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 烘干Activity
 * <p/>
 * Created by Will on 16/4/12.
 */
public class DryingControlActivity extends BaseActivity {

    private DryingCircleView drying;
    private TextView tv_drying_times;
    private CustomToggle toggle_drying;

    private int time = 0;//倒计时使用
    private Timer timer;
    private TimerTask timerTask;
    private long times = 30 * 60 * 1000;//默认烘干时间30分钟
    private String deviceId;//设备ID
    private MSDKListener msdkListener;//自定义SDK回调监听

    @Override
    protected int getLayoutId() {
        return R.layout.activity_drying_control;
    }

    @Override
    protected String getTitleName() {
        return getResources().getString(R.string.drying_title);
    }

    @Override
    protected void initActivity() {

        deviceId = sp.getString(MConstants.DEVICE_ID, "");
        msdkListener = new MSDKListener();

    }

    @Override
    protected void initView() {
        drying = (DryingCircleView) findViewById(R.id.drying);
        tv_drying_times = (TextView) findViewById(R.id.tv_drying_times);
        toggle_drying = (CustomToggle) findViewById(R.id.toggle_drying);
        if (toggle_drying != null) {
            toggle_drying.setChecked(false);//初始化关闭
        }

        //上一次烘干没结束的情况下,继续烘干
        RestoreDryingTimes();

        toggle_drying.setOnCheckedChange(new CustomToggle.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View v, boolean isChecked) {
                if (isChecked) {
                    MachtalkSDK.getInstance().operateDevice(deviceId,
                            new String[]{MConstants.ATTR_DRYING_SWITCH, MConstants.ATTR_DRYING_TIME},
                            new String[]{"1", String.valueOf(times / 1000)});

                    toggle_drying.setChecked(true);
                    initTimer();
                    timer.schedule(timerTask, 0, 1000);
                    drying.setAnimDuration(times);
                    drying.startAnim();
                    time = 0;

                    editor.putString(MConstants.DRYING_START_TIME,
                            MUtils.getCurrTime("yyyy-MM-dd HH:mm:ss"));
                    editor.putString(MConstants.DRYING_TIMES, String.valueOf(times));
                    editor.putBoolean(MConstants.DRYING_OPEN, true);
                    editor.commit();
                } else {
                    showDialog(); //弹出提示
                }
            }
        });
    }


    /**
     * 恢复当前烘干时间
     */
    private void RestoreDryingTimes() {
        if (!sp.getBoolean(MConstants.DRYING_OPEN, false)) {
            return;
        }
        String startTime = sp.getString(MConstants.DRYING_START_TIME, "");
        if (!TextUtils.isEmpty(startTime)) {
            int drying_times = Integer.parseInt(sp.getString(MConstants.DRYING_TIMES, ""));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                Date start_date = sdf.parse(startTime);
                long curr = System.currentTimeMillis();
                if (curr - start_date.getTime() < drying_times) {
                    toggle_drying.setChecked(true);
                    drying.setAnimDuration(drying_times - curr + start_date.getTime());
                    float scaled = (float) (curr - start_date.getTime()) / drying_times;
                    drying.startAnim(scaled);
                    time = (int) (curr - start_date.getTime()) / 1000;
                    initTimer();
                    timer.schedule(timerTask, 0, 1000);
                } else {//时间超过设定时间了
                    editor.putBoolean(MConstants.DRYING_OPEN, false);
                    editor.commit();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void OnClick(View v) {

    }

    /**
     * 发送消息
     */
    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv_drying_times.setText(calculateTimes(times - time * 1000));
            time++;
            if (time * 1000 >= times) {
                //加载完毕执行重置
                drying.resetView();
                clearTimer();
                time = 0;
            }
        }
    };

    /**
     * 设置烘干时间
     *
     * @param times
     */

    public void setTimes(long times) {
        this.times = times;
    }

    /**
     * 格式化时间显示
     *
     * @param times
     * @return
     */
    private String calculateTimes(long times) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return sdf.format(times);
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
    protected void onDestroy() {
        super.onDestroy();
        clearTimer();
    }


    /**
     * show提示dialog
     */
    private void showDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.stop_drying));
        builder.setLogo(R.drawable.img_close_drying);
        builder.setCanceledOnTouchOtherPlace(false);
        builder.setPositiveButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消烘干
                MachtalkSDK.getInstance().operateDevice(deviceId,
                        new String[]{MConstants.ATTR_DRYING_SWITCH},
                        new String[]{"0"});
                editor.putBoolean(MConstants.DRYING_OPEN, false);
                editor.commit();
                drying.resetView();
                clearTimer();
                tv_drying_times.setText(getResources().getString(R.string.blank_time));
                time = 0;
                toggle_drying.setChecked(false);
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
     * 自定义回调监听
     */
    class MSDKListener extends MachtalkSDKListener {

        @Override
        public void onReceiveDeviceMessage(Result result, ReceivedDeviceMessage rdm) {
            super.onReceiveDeviceMessage(result, rdm);
            LogUtil.w("  " + result.getSuccess() + "  " + (rdm == null));
            if (result != null && result.getSuccess() == Result.SUCCESS && rdm != null) {
                //操作成功
                LogUtil.w("  " + result.getSuccess() + "  " + (rdm.getDvidStatusList().get(0).toString()));
            } else {
                MUtils.showToast(context, getResources().getString(R.string.operate_failed));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(msdkListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(msdkListener);
    }
}
