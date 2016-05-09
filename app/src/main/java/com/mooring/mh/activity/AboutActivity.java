package com.mooring.mh.activity;

import android.view.View;

import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.mooring.mh.R;

/**
 * AboutActivity
 * <p/>
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
    protected void OnClick(View v) {

    }

    @Override
    protected MachtalkSDKListener setSDKListener() {
        return null;
    }

}
