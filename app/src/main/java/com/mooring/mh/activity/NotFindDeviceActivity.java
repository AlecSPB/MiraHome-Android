package com.mooring.mh.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;

/**
 * 没有发现设备页面
 * <p/>
 * Created by Will on 16/5/13.
 */
public class NotFindDeviceActivity extends BaseActivity {
    private ImageView imgView_retry_connect;
    private TextView tv_act_skip;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_not_find_device;
    }

    @Override
    protected String getTitleName() {
        return "";
    }

    @Override
    protected void initActivity() {

    }

    @Override
    protected void initView() {
        imgView_retry_connect = (ImageView) findViewById(R.id.imgView_retry_connect);
        tv_act_skip = (TextView) findViewById(R.id.tv_act_skip);
        imgView_retry_connect.setOnClickListener(this);
        tv_act_skip.setOnClickListener(this);
        tv_act_skip.setVisibility(View.VISIBLE);
    }

    @Override
    protected void OnClick(View v) {
        if (v.getId() == R.id.imgView_retry_connect) {
            startActivity(new Intent(context, SetWifiActivity.class));
        } else if (v.getId() == R.id.tv_act_skip) {
        }
        context.finish();
    }
}
