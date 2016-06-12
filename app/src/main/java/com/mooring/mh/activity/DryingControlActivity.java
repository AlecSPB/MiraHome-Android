package com.mooring.mh.activity;

import android.content.DialogInterface;
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

/**
 * 烘干Activity
 * <p/>
 * Created by Will on 16/4/12.
 */
public class DryingControlActivity extends BaseActivity implements DryingCircleView.LoadingListener,
        CustomToggle.OnCheckedChangeListener {

    private DryingCircleView drying;
    private TextView tv_drying_times;
    private CustomToggle toggle_drying;

    private long time = 0;//记录上次
    private long times = 30 * 60 * 1000;//默认烘干时间30分钟
    private String deviceId;//设备ID
    private MSDKListener msdkListener;//自定义SDK回调监听
    private boolean isOperate = false;//是否是手动操作
    private boolean isDestroy = false;//是否关闭当前Activity
    private boolean isFingerCancel = false;//是否手动取消当前烘干

    @Override
    protected int getLayoutId() {
        return R.layout.activity_drying_control;
    }

    @Override
    protected String getTitleName() {
        return getResources().getString(R.string.title_drying);
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

        drying.setOnLoadingListener(this);
        toggle_drying.setChecked(false);//初始化关闭
        toggle_drying.setOnCheckedChange(this);

        //上一次烘干没结束的情况下,继续烘干
        RestoreDryingTimes();
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
                time = curr - start_date.getTime();
                if (time < drying_times) {
                    toggle_drying.setChecked(true);
                    drying.setAnimDuration(drying_times - time - 500);
                    drying.startAnim((float) time / drying_times);
                } else {//时间超过设定时间了
                    time = 0;
                    editor.putBoolean(MConstants.DRYING_OPEN, false);
                    editor.apply();
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
        return sdf.format(times + 500);
    }

    /**
     * show提示dialog
     */
    private void showDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.tip_stop_drying));
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
                editor.apply();

                isFingerCancel = true;
                drying.resetView();
                time = 0;
                toggle_drying.setChecked(false);
                tv_drying_times.setText(getString(R.string.blank_time));
                isOperate = true;
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isFingerCancel = false;
                isOperate = false;
            }
        });

        builder.create().show();
    }

    /**
     * 完成烘干
     */
    private void showCompleteDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.tip_drying_success));
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

    @Override
    public void onCheckedChanged(View v, boolean isChecked) {
        isFingerCancel = false;
        if (isChecked) {
            MachtalkSDK.getInstance().operateDevice(deviceId,
                    new String[]{MConstants.ATTR_DRYING_SWITCH, MConstants.ATTR_DRYING_TIME},
                    new String[]{"1", String.valueOf(times / 1000)});

            toggle_drying.setChecked(true);
            drying.setAnimDuration(times - 500);
            drying.startAnim();
            time = 0;

            editor.putString(MConstants.DRYING_START_TIME, MUtils.getCurrTime("yyyy-MM-dd HH:mm:ss"));
            editor.putString(MConstants.DRYING_TIMES, String.valueOf(times));
            editor.putBoolean(MConstants.DRYING_OPEN, true);
            editor.apply();
            isOperate = true;
        } else {
            showDialog(); //弹出提示
        }
    }

    @Override
    public void onLoading(long value) {
        if (!isDestroy && value == -1 && !isFingerCancel) {
            //加载完毕执行重置
            drying.resetView();
            toggle_drying.setChecked(false);
            time = 0;
            tv_drying_times.setText(getString(R.string.blank_time));
            showCompleteDialog();
            return;
        }
        if (time + value <= times) {
            tv_drying_times.setText(calculateTimes(times - time - value));
        }
    }

    /**
     * 自定义回调监听
     */
    class MSDKListener extends MachtalkSDKListener {

        @Override
        public void onReceiveDeviceMessage(Result result, ReceivedDeviceMessage rdm) {
            super.onReceiveDeviceMessage(result, rdm);
            if (!isOperate) {
                return;
            }
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS && rdm != null && deviceId.equals(rdm.getDeviceId())) {
                //操作成功
                LogUtil.e("操作成功+++++++" + rdm.isRespMsg());
            } else {
                MUtils.showToast(context, getResources().getString(R.string.operate_failed));
            }
            isOperate = false;
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

    @Override
    protected void onDestroy() {
        isDestroy = true;
        drying.resetView();
        time = 0;
        super.onDestroy();
    }
}
