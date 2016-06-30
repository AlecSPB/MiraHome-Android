package com.mooring.mh.activity;

import android.view.View;

import com.mooring.mh.R;
import com.umeng.analytics.MobclickAgent;

/**
 * AboutActivity
 * <p>
 * Created by Will on 16/4/14.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected String getTitleName() {
        return getString(R.string.title_about);
    }

    @Override
    protected void initActivity() {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void OnClick(View v) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("About");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("About");
        MobclickAgent.onPause(this);
    }
}
