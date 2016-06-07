package com.mooring.mh.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.Device;
import com.machtalk.sdk.domain.DeviceListInfo;
import com.machtalk.sdk.domain.Result;
import com.machtalk.sdk.domain.UnbindDeviceResult;
import com.mooring.mh.R;
import com.mooring.mh.adapter.DeviceListAdapter;
import com.mooring.mh.adapter.OnRecyclerItemClickListener;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.mooring.mh.views.CommonDialog;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 已存在设备列表
 * <p/>
 * Created by Will on 16/5/13.
 */
public class ExistingDeviceActivity extends BaseActivity implements OnRecyclerItemClickListener {
    private TextView tv_act_skip;
    private RecyclerView device_recyclerView;
    private ImageView imgView_retry_connect;
    private LinearLayoutManager layoutManager;
    private DeviceListAdapter adapter;
    private List<Device> deviceList;

    /**
     * 单个设备情况
     */
    private View layout_single_device;
    private ImageView imgView_no_device;
    private ImageView imgView_check_box;
    private View layout_one_device;
    private ImageView imgView_one_device;
    private TextView tv_device_name;

    private MSDKListener msdkListener;
    private Dialog dialog;
    private Device singleDevice;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_existing_device;
    }

    @Override
    protected String getTitleName() {
        return "";
    }

    @Override
    protected void initActivity() {

        msdkListener = new MSDKListener();
    }

    @Override
    protected void initView() {
        tv_act_skip = (TextView) findViewById(R.id.tv_act_skip);
        device_recyclerView = (RecyclerView) findViewById(R.id.device_recyclerView);
        layout_single_device = findViewById(R.id.layout_single_device);
        imgView_no_device = (ImageView) findViewById(R.id.imgView_no_device);
        imgView_check_box = (ImageView) findViewById(R.id.imgView_check_box);
        layout_one_device = findViewById(R.id.layout_one_device);
        imgView_one_device = (ImageView) findViewById(R.id.imgView_one_device);
        tv_device_name = (TextView) findViewById(R.id.tv_device_name);
        imgView_retry_connect = (ImageView) findViewById(R.id.imgView_retry_connect);

        tv_act_skip.setVisibility(View.VISIBLE);
        tv_act_skip.setOnClickListener(this);
        imgView_retry_connect.setOnClickListener(this);
        layout_one_device.setOnClickListener(this);

        MachtalkSDK.getInstance().queryDeviceList();
    }

    /**
     * show提示dialog
     */
    private void showDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.tip_failed_get_list));
        builder.setLogo(R.drawable.img_failed_connect_network);
        builder.setCanceledOnTouchOtherPlace(false);

        builder.setTextDialogListener(
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//取消监听
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //重试监听
                        MachtalkSDK.getInstance().queryDeviceList();

                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    /**
     * 决定对当前选中设备的使用或者忽略
     */
    private void decideDevice(final Device device) {
        dialog = new Dialog(this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_decide_device);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.other_view).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        dialog.findViewById(R.id.dialog_cancel).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        //忽略设备
        dialog.findViewById(R.id.tv_ignore_device).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        MachtalkSDK.getInstance().unbindDevice(device.getId(),
                                MachtalkSDKConstant.UnbindType.UNBIND, null);
                    }
                });
        // 使用设备
        dialog.findViewById(R.id.tv_use_device).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!device.isOnline()) {
                            dialog.dismiss();
                            MUtils.showToast(context, getString(R.string.tip_unavailable_offline));
                            return;
                        }
                        imgView_check_box.setVisibility(View.VISIBLE);
                        //保存当前使用的Device的相关数据
                        editor.putString(MConstants.DEVICE_ID, device.getId());
                        editor.putString(MConstants.DEVICE_NAME, device.getName());
                        editor.putString(MConstants.DEVICE_MODEL, device.getModel());
                        editor.putString(MConstants.DEVICE_TYPE, device.getType());
                        editor.putBoolean(MConstants.DEVICE_ONLINE, device.isOnline());
                        editor.putBoolean(MConstants.DEVICE_LANONLINE, device.isLanOnline());
                        editor.apply();

                        Intent it = new Intent(context, CommonSuccessActivity.class);
                        it.putExtra(MConstants.ENTRANCE_FLAG, MConstants.CONNECTED_SUCCESS);
                        startActivity(it);

                        dialog.dismiss();
                        context.finish();
                    }
                });
        dialog.show();
        MUtils.setDialogFullScreen(this, dialog);
    }

    @Override
    protected void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_act_skip:
                context.finish();
                break;
            case R.id.imgView_retry_connect:
                startActivity(new Intent(context, SetWifiActivity.class));
                context.finish();
                break;
            case R.id.layout_one_device:
                decideDevice(singleDevice);
                break;
        }
    }

    /**
     * 自定义智成云回调监听
     */
    class MSDKListener extends MachtalkSDKListener {
        @Override
        public void onQueryDeviceList(Result result, DeviceListInfo dli) {
            super.onQueryDeviceList(result, dli);
            int success = Result.FAILED;
            String errorMsg = null;
            if (result != null) {
                success = result.getSuccess();
                errorMsg = result.getErrorMessage();
            }
            if (success == Result.SUCCESS && dli != null && dli.getDeviceList() != null) {
                deviceList = dli.getDeviceList();
                if (deviceList.size() == 0) {
                    layout_single_device.setVisibility(View.VISIBLE);
                    imgView_no_device.setVisibility(View.VISIBLE);
                    device_recyclerView.setVisibility(View.GONE);
                    layout_one_device.setVisibility(View.GONE);
                } else if (deviceList.size() == 1) {
                    imgView_no_device.setVisibility(View.GONE);
                    layout_single_device.setVisibility(View.VISIBLE);
                    layout_one_device.setVisibility(View.VISIBLE);
                    imgView_one_device.setVisibility(View.VISIBLE);
                    device_recyclerView.setVisibility(View.GONE);
                    tv_device_name.setText(deviceList.get(0).getName());
                    if (!deviceList.get(0).isOnline()) {
                        imgView_one_device.setAlpha(0.5f);
                    }
                    LogUtil.e("deviceList   " + deviceList.get(0).getId() + "   " + sp.getString(MConstants.DEVICE_ID, ""));
                    if (deviceList.get(0).getId().equals(sp.getString(MConstants.DEVICE_ID, ""))) {
                        imgView_check_box.setVisibility(View.VISIBLE);
                    }
                    singleDevice = deviceList.get(0);
                } else {
                    imgView_no_device.setVisibility(View.GONE);
                    device_recyclerView.setVisibility(View.VISIBLE);
                    layout_single_device.setVisibility(View.GONE);
                    layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    device_recyclerView.setLayoutManager(layoutManager);
                    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                    device_recyclerView.setHasFixedSize(true);
                    adapter = new DeviceListAdapter(deviceList);
                    adapter.setItemClickListener(ExistingDeviceActivity.this);
                    device_recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (errorMsg == null) {
                    errorMsg = getResources().getString(R.string.load_device_list_fail);
                }
                LogUtil.e(errorMsg);

                device_recyclerView.setVisibility(View.GONE);
                layout_single_device.setVisibility(View.VISIBLE);
                imgView_no_device.setVisibility(View.VISIBLE);
                layout_one_device.setVisibility(View.GONE);
                //查无结果,弹出dialog是否重试
                showDialog();
            }
        }

        @Override
        public void onUnbindDevice(Result result, UnbindDeviceResult udr) {
            super.onUnbindDevice(result, udr);
            int success = Result.FAILED;
            String errorMsg = null;
            if (result != null) {
                success = result.getSuccess();
                errorMsg = result.getErrorMessage();
            }
            if (success == Result.SUCCESS) {
                //在解绑设备的回调中,重新请求数据舒心当前页面
                MachtalkSDK.getInstance().queryDeviceList();
                MUtils.showToast(context, getString(R.string.unbind_success));
            } else {
                if (errorMsg == null) {
                    errorMsg = getResources().getString(R.string.unbind_failed);
                }
                MUtils.showToast(context, errorMsg);
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
    protected void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(msdkListener);
    }

    @Override
    public void onItemClick(View view, int position) {
        decideDevice(deviceList.get(position));
    }
}
