package com.mooring.mh.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.mooring.mh.R;
import com.mooring.mh.app.InitApplicationHelper;

/**
 * 自定义BaseActivity for common
 * <p/>
 * Created by Will on 16/3/30.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected AppCompatActivity context;
    private MachtalkSDKListener baseListener;
    protected SharedPreferences sp;
    protected SharedPreferences.Editor editor;
    protected ImageView imgView_act_back;
    protected TextView tv_act_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        sp = InitApplicationHelper.sp;
        editor = sp.edit();
        editor.apply();

        MachtalkSDK.getInstance().startSDK(this, null);
        MachtalkSDK.getInstance().setContext(this);
        baseListener = new BaseListener();

        initActivity();

        if (getLayoutId() == 0) {
            throw new NullPointerException("Layout files can not be empty");
        }

        setContentView(getLayoutId());

        imgView_act_back = (ImageView) findViewById(R.id.imgView_act_back);
        if (imgView_act_back != null) {
            imgView_act_back.setOnClickListener(this);
        }
        tv_act_title = (TextView) findViewById(R.id.tv_act_title);
        if (tv_act_title != null && !TextUtils.isEmpty(getTitleName())) {
            tv_act_title.setText(getTitleName());
        }

        initView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgView_act_back) {
            context.finish();
        }
        OnClick(v);
    }


    /**
     * 初始化Activity相关参数和变量,和view无关
     */
    protected abstract void initActivity();

    /**
     * 获取布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 获取title名称
     *
     * @return
     */
    protected abstract String getTitleName();

    /**
     * 初始化View控件
     */
    protected abstract void initView();

    /**
     * 点击事件
     *
     * @param v
     */
    protected abstract void OnClick(View v);

    @Override
    protected void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(baseListener);
    }

    @Override
    protected void onDestroy() {
        MachtalkSDK.getInstance().removeSdkListener(baseListener);
        super.onDestroy();
        System.gc();
    }

    class BaseListener extends MachtalkSDKListener {
        @Override
        public void onServerConnectStatusChanged(MachtalkSDKConstant.ServerConnStatus scs) {
            super.onServerConnectStatusChanged(scs);
            if (scs == MachtalkSDKConstant.ServerConnStatus.LOGOUT_KICKOFF) {
                logoutAndRelogin();
                return;
            }
        }
    }

    protected void logoutAndRelogin() {

        //加入其它操作

        BaseActivity.this.finish();
    }
}
