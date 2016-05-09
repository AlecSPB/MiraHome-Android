package com.mooring.mh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.mooring.mh.R;

/**
 * 自定义BaseActivty for common
 * <p/>
 * Created by Will on 16/3/30.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if (getLayoutId() == 0) {
            throw new NullPointerException();
        }
        setContentView(getLayoutId());
        ImageView imgView_act_back = (ImageView) findViewById(R.id.imgView_act_back);
        if (imgView_act_back != null) {
            imgView_act_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.this.finish();
                }
            });
        }
        TextView tv_act_title = (TextView) findViewById(R.id.tv_act_title);
        if (tv_act_title != null) {
            tv_act_title.setText(getTitleName());
        }

        initActivity();

        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(setSDKListener());
    }

    @Override
    public void onClick(View v) {
        OnClick(v);
    }

    protected abstract int getLayoutId();

    protected abstract String getTitleName();

    protected abstract void initActivity();

    protected abstract void OnClick(View v);

    protected abstract MachtalkSDKListener setSDKListener();

    @Override
    public void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(setSDKListener());
    }

    @Override
    public void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(setSDKListener());
    }
}
