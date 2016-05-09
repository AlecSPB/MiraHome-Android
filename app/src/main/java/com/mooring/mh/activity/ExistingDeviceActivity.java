package com.mooring.mh.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.Device;
import com.mooring.mh.R;
import com.mooring.mh.adapter.DeviceListAdapter;
import com.mooring.mh.adapter.OnRecyclerItemClickListener;
import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.utils.DeviceManager;
import com.mooring.mh.utils.MConstants;

import java.util.List;

/**
 * 已存在设备列表
 * <p>
 * Created by Will on 16/5/13.
 */
public class ExistingDeviceActivity extends BaseActivity implements OnRecyclerItemClickListener {
    private TextView tv_act_skip;
    private ImageView imgView_retry_connect;
    private RecyclerView device_recyclerView;
    private LinearLayoutManager manager;
    private DeviceListAdapter adapter;
    private List<Device> deviceList;

    /**
     * 单个设备情况
     */
    private View layout_single_device;
    private ImageView imgView_check_box;
    private ImageView imgView_device_img;
    private TextView tv_device_name;


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

        initView();

    }

    private void initView() {
        tv_act_skip = (TextView) findViewById(R.id.tv_act_skip);
        imgView_retry_connect = (ImageView) findViewById(R.id.imgView_retry_connect);
        tv_act_skip.setVisibility(View.VISIBLE);
        tv_act_skip.setOnClickListener(this);
        imgView_retry_connect.setOnClickListener(this);
        device_recyclerView = (RecyclerView) findViewById(R.id.device_recyclerView);
        layout_single_device = findViewById(R.id.layout_single_device);

        deviceList = DeviceManager.getInstance().getDeviceList();
        if (deviceList != null) {
            if (deviceList.size() > 1) {
                device_recyclerView.setVisibility(View.VISIBLE);
                layout_single_device.setVisibility(View.GONE);

                manager = new LinearLayoutManager(this);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                device_recyclerView.setLayoutManager(manager);
                //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                device_recyclerView.setHasFixedSize(true);
                adapter = new DeviceListAdapter(deviceList);
                adapter.setItemClickListener(this);
                device_recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else if (deviceList.size() == 1) {
                device_recyclerView.setVisibility(View.GONE);
                layout_single_device.setVisibility(View.VISIBLE);
                layout_single_device.setOnClickListener(this);
                imgView_check_box = (ImageView) findViewById(R.id.imgView_check_box);
                imgView_device_img = (ImageView) findViewById(R.id.imgView_device_img);
                tv_device_name = (TextView) findViewById(R.id.tv_device_name);
                tv_device_name.setText(deviceList.get(0).getName());
            } else {
            }
        }
    }

    @Override
    protected void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_act_skip:
            case R.id.imgView_retry_connect:
                ExistingDeviceActivity.this.finish();
                break;
            case R.id.layout_single_device:
                imgView_check_box.setVisibility(View.VISIBLE);
                InitApplicationHelper.sp.edit().putString(MConstants.DEVICE_ID,
                        deviceList.get(0).getId()).commit();
                break;
        }
    }

    @Override
    protected MachtalkSDKListener setSDKListener() {
        return null;
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
