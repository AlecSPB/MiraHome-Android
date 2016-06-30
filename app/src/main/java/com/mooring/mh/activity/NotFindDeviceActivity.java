package com.mooring.mh.activity;

import android.content.Intent;
import android.view.View;

import com.mooring.mh.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 没有发现设备页面
 * <p/>
 * Created by Will on 16/5/13.
 */
public class NotFindDeviceActivity extends BaseActivity {

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
        findViewById(R.id.imgView_retry_connect).setOnClickListener(this);
        findViewById(R.id.tv_act_skip).setOnClickListener(this);
        findViewById(R.id.tv_act_skip).setVisibility(View.VISIBLE);
    }

    @Override
    protected void OnClick(View v) {
        if (v.getId() == R.id.imgView_retry_connect) {
            startActivity(new Intent(context, SetWifiActivity.class));
        }
        context.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NotFindDevice");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NotFindDevice");
        MobclickAgent.onPause(this);
    }
}
