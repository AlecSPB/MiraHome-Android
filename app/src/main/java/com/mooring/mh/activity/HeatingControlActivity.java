package com.mooring.mh.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.DeviceStatus;
import com.machtalk.sdk.domain.DvidStatus;
import com.machtalk.sdk.domain.ReceivedDeviceMessage;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.views.CommonDialog;
import com.mooring.mh.views.ControlView.DragScaleTwoView;
import com.mooring.mh.views.ControlView.DragScaleView;
import com.mooring.mh.views.CustomImageView.CircleImageView;
import com.mooring.mh.views.CustomToggle;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * Heating 控制Activity
 * <p/>
 * Created by Will on 16/4/8.
 */
public class HeatingControlActivity extends AppCompatActivity implements CustomToggle.OnCheckedChangeListener {
    private ImageView imgView_act_back;
    private View layout_two_header;
    private CircleImageView circleImg_left;
    private CircleImageView circleImg_right;

    private View layout_one_user;
    private DragScaleView dragScaleView;
    private CustomToggle toggle_middle;

    private View layout_two_user;
    private DragScaleTwoView dragScaleTwoView;
    private CustomToggle toggle_left;
    private CustomToggle toggle_right;

    /**
     * 当前用户的个数
     */
    private int currUsers = 2;

    private String deviceId = "";
    private BaseListener listener;
    private SharedPreferences.Editor editor;
    private String left_real_temp;//床温
    private String left_target_temp;//可调整温度
    private Boolean left_drop_enable = false;//左边可操控
    private String right_real_temp;//床温
    private String right_target_temp;//可调整温度
    private Boolean right_drop_enable = false;//右边可操控
    private String room_temp = "";//室温

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heating_control);

        editor = InitApplicationHelper.sp.edit();
        deviceId = InitApplicationHelper.sp.getString(MConstants.DEVICE_ID, "");

        listener = new BaseListener();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(listener);

        left_target_temp = InitApplicationHelper.sp.getString(MConstants.LEFT_TARGET_TEMP, "");
        right_target_temp = InitApplicationHelper.sp.getString(MConstants.RIGHT_TARGET_TEMP, "");

        initView();

//        judgeUser();

        MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
    }

    private void initView() {

        imgView_act_back = (ImageView) findViewById(R.id.imgView_act_back);
        layout_two_header = findViewById(R.id.layout_two_header);
        circleImg_left = (CircleImageView) findViewById(R.id.circleImg_left);
        circleImg_right = (CircleImageView) findViewById(R.id.circleImg_right);

        layout_one_user = findViewById(R.id.layout_one_user);
        dragScaleView = (DragScaleView) findViewById(R.id.dragScaleView);
        toggle_middle = (CustomToggle) findViewById(R.id.toggle_middle);

        layout_two_user = findViewById(R.id.layout_two_user);
        dragScaleTwoView = (DragScaleTwoView) findViewById(R.id.dragScaleTwoView);
        toggle_left = (CustomToggle) findViewById(R.id.toggle_left);
        toggle_right = (CustomToggle) findViewById(R.id.toggle_right);

        //判断当前使用户的个数
        imgView_act_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeatingControlActivity.this.finish();
            }
        });
    }

    private void judgeUser() {
        if (currUsers == 1) {
            layout_two_user.setVisibility(View.GONE);
            layout_two_header.setVisibility(View.GONE);
            layout_one_user.setVisibility(View.VISIBLE);

            toggle_middle.setOnCheckedChange(this);
            dragScaleView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                    OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!TextUtils.isEmpty(left_target_temp)) {
                        dragScaleView.setCurrTemperature(Integer.parseInt(left_target_temp));
                    }
                    if (!TextUtils.isEmpty(left_real_temp)) {
                        dragScaleView.setBedTemperature(left_real_temp);
                    }
                    toggle_middle.setChecked(left_drop_enable);
                    dragScaleView.setRoomY(room_temp);
                    dragScaleView.setIsDropAble(left_drop_enable);
                    dragScaleView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });

            dragScaleView.setOnDropListener(new DragScaleView.OnDropListener() {
                @Override
                public void onDrop(int currTemp) {
                    MachtalkSDK.getInstance().operateDevice(deviceId,
                            new String[]{MConstants.ATTR_LEFT_TARGET_TEMP},
                            new String[]{currTemp + ""});
                    editor.putString(MConstants.LEFT_TARGET_TEMP, currTemp + "");
                    editor.commit();
                }
            });
        } else {
            layout_one_user.setVisibility(View.GONE);
            layout_two_user.setVisibility(View.VISIBLE);
            layout_two_header.setVisibility(View.VISIBLE);

            toggle_left.setOnCheckedChange(this);
            toggle_right.setOnCheckedChange(this);

            dragScaleTwoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                    OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!TextUtils.isEmpty(left_target_temp)) {
                        dragScaleTwoView.setCurrLeftTemp(Integer.parseInt(left_target_temp));
                    }
                    if (!TextUtils.isEmpty(right_target_temp)) {
                        dragScaleTwoView.setCurrRightTemp(Integer.parseInt(right_target_temp));
                    }
                    if (!TextUtils.isEmpty(left_real_temp)) {
                        dragScaleTwoView.setBedLeftTemp(left_real_temp);
                    }
                    if (!TextUtils.isEmpty(right_real_temp)) {
                        dragScaleTwoView.setBedRightTemp(right_real_temp);
                    }
                    toggle_left.setChecked(left_drop_enable);
                    toggle_right.setChecked(right_drop_enable);
                    dragScaleTwoView.setIsLeftDropAble(left_drop_enable);
                    dragScaleTwoView.setIsRightDropAble(right_drop_enable);
                    dragScaleTwoView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
            dragScaleTwoView.setOnDropListener(new DragScaleTwoView.OnDropListener() {
                @Override
                public void onDrop(int currTemp, int location) {
                    if (location == 0) {
                        MachtalkSDK.getInstance().operateDevice(deviceId,
                                new String[]{MConstants.ATTR_LEFT_TARGET_TEMP},
                                new String[]{currTemp + ""});
                        editor.putString(MConstants.LEFT_TARGET_TEMP, currTemp + "");
                        editor.commit();
                    }
                    if (location == 1) {
                        MachtalkSDK.getInstance().operateDevice(deviceId,
                                new String[]{MConstants.ATTR_RIGHT_TARGET_TEMP},
                                new String[]{currTemp + ""});
                        editor.putString(MConstants.RIGHT_TARGET_TEMP, currTemp + "");
                        editor.commit();
                    }
                }
            });
        }
    }

    /**
     * show提示dialog
     */
    private void showDialog(final CustomToggle toggle) {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.stop_heating));
        builder.setLogo(R.drawable.img_close_heating);
        builder.setCanceledOnTouchOtherPlace(false);
        builder.setPositiveButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (toggle == toggle_middle) {
                    MachtalkSDK.getInstance().operateDevice(deviceId,
                            new String[]{MConstants.ATTR_LEFT_TARGET_TEMP_SWITCH},
                            new String[]{"0"});
                    toggle_middle.setChecked(false);
                    dragScaleView.setIsDropAble(false);
                }
                if (toggle == toggle_left) {
                    MachtalkSDK.getInstance().operateDevice(deviceId,
                            new String[]{MConstants.ATTR_LEFT_TARGET_TEMP_SWITCH},
                            new String[]{"0"});
                    toggle_left.setChecked(false);
                    dragScaleTwoView.setIsLeftDropAble(false);
                }
                if (toggle == toggle_right) {
                    MachtalkSDK.getInstance().operateDevice(deviceId,
                            new String[]{MConstants.ATTR_RIGHT_TARGET_TEMP_SWITCH},
                            new String[]{"0"});
                    toggle_right.setChecked(false);
                    dragScaleTwoView.setIsRightDropAble(false);
                }
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


    @Override
    public void onCheckedChanged(View v, boolean isChecked) {
        switch (v.getId()) {
            case R.id.toggle_middle:
                if (isChecked) {
                    MachtalkSDK.getInstance().operateDevice(deviceId,
                            new String[]{MConstants.ATTR_LEFT_TARGET_TEMP_SWITCH},
                            new String[]{"1"});
                    toggle_middle.setChecked(true);
                    dragScaleView.setIsDropAble(true);
                } else {
                    showDialog(toggle_middle);
                }
                break;

            case R.id.toggle_left:
                if (isChecked) {
                    MachtalkSDK.getInstance().operateDevice(deviceId,
                            new String[]{MConstants.ATTR_LEFT_TARGET_TEMP_SWITCH},
                            new String[]{"1"});
                    toggle_left.setChecked(true);
                    dragScaleTwoView.setIsLeftDropAble(true);
                } else {
                    showDialog(toggle_left);
                }
                break;

            case R.id.toggle_right:
                if (isChecked) {
                    MachtalkSDK.getInstance().operateDevice(deviceId,
                            new String[]{MConstants.ATTR_RIGHT_TARGET_TEMP_SWITCH},
                            new String[]{"1"});
                    toggle_right.setChecked(true);
                    dragScaleTwoView.setIsRightDropAble(true);
                } else {
                    showDialog(toggle_right);
                }
                break;
        }
    }

    /**
     * 自定义回调监听
     */
    class BaseListener extends MachtalkSDKListener {
        @Override
        public void onServerConnectStatusChanged(MachtalkSDKConstant.ServerConnStatus serverConnStatus) {
            super.onServerConnectStatusChanged(serverConnStatus);
            if (serverConnStatus == MachtalkSDKConstant.ServerConnStatus.LOGOUT_KICKOFF) {
                HeatingControlActivity.this.finish();
                return;
            }
        }

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
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        LogUtil.e("dvid  " + list.get(i).getDvid() + " value  "
                                + list.get(i).getValue());
                        if (MConstants.ATTR_SINGLE_OR_DOUBLE.equals(list.get(i).getDvid())) {
                            currUsers = Integer.parseInt(list.get(i).getValue()) + 1;
                        }
                        if (MConstants.ATTR_LEFT_TARGET_TEMP_SWITCH.equals(list.get(i).getDvid())) {
                            left_drop_enable = Integer.parseInt(list.get(i).getValue()) == 1;
                        }

                        if (MConstants.ATTR_LEFT_ACTUAL_TEMP.equals(list.get(i).getDvid())) {
                            left_real_temp = list.get(i).getValue();
                        }
                        if (MConstants.ATTR_LEFT_TARGET_TEMP.equals(list.get(i).getDvid())) {
                            left_target_temp = list.get(i).getValue();
                        }
                        if (MConstants.ATTR_ENVIR_TEMPERATURE.equals(list.get(i).getDvid())) {
                            room_temp = list.get(i).getValue();
                        }
                        if (currUsers == 2) {
                            if (MConstants.ATTR_RIGHT_ACTUAL_TEMP.equals(list.get(i).getDvid())) {
                                right_real_temp = list.get(i).getValue();
                            }
                            if (MConstants.ATTR_RIGHT_TARGET_TEMP.equals(list.get(i).getDvid())) {
                                right_target_temp = list.get(i).getValue();
                            }
                            if (MConstants.ATTR_RIGHT_TARGET_TEMP_SWITCH.equals(list.get(i).getDvid())) {
                                right_drop_enable = Integer.parseInt(list.get(i).getValue()) == 1;
                            }
                        }
                    }
                    judgeUser();
                }
            } else {
                CommonUtils.showToast(HeatingControlActivity.this,
                        getResources().getString(R.string.device_not_online));
            }
        }

        @Override
        public void onReceiveDeviceMessage(Result result, ReceivedDeviceMessage rdm) {
            super.onReceiveDeviceMessage(result, rdm);

            LogUtil.e("result  " + result.getSuccess());
            LogUtil.e("mDeviceId  " + rdm.getDeviceId() + " 操作回复 " + rdm.isRespMsg());

            if (rdm.isRespMsg()) {
                List<DvidStatus> list = rdm.getDvidStatusList();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {

                        LogUtil.e("  dvid  " + list.get(i).getDvid() + " value  " + list.get(i).getValue());

                        if (MConstants.ATTR_ENVIR_TEMPERATURE.equals(list.get(i).getDvid())) {
                            room_temp = list.get(i).getValue();
                            if (currUsers == 1) {
                                dragScaleView.setRoomY(Integer.parseInt(room_temp));
                            } else {
                                dragScaleTwoView.setRoomY(Integer.parseInt(room_temp));
                            }
                        }
                        if (MConstants.ATTR_LEFT_ACTUAL_TEMP.equals(list.get(i).getDvid())) {
                            left_real_temp = list.get(i).getValue();
                            if (currUsers == 1) {
                                dragScaleView.setBedTemperature(left_real_temp);
                            } else {
                                dragScaleTwoView.setBedLeftTemp(left_real_temp);
                            }
                        }
                        if (currUsers == 2 && MConstants.ATTR_RIGHT_ACTUAL_TEMP.
                                equals(list.get(i).getDvid())) {
                            right_real_temp = list.get(i).getValue();
                            dragScaleTwoView.setBedRightTemp(right_real_temp);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(listener);
    }
}
