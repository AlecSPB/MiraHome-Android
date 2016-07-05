package com.mooring.mh.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.adapter.LoginAndSignUpPagerAdapter;
import com.mooring.mh.utils.MConstants;
import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;
import java.util.Locale;

/**
 * 登录和注册Activity
 * <p/>
 * Created by Will on 16/3/30.
 */
public class LoginAndSignUpActivity extends BaseActivity {
    private ViewPager login_viewPager;
    private TabLayout login_tabLayout;
    private LoginAndSignUpPagerAdapter pagerAdapter;
    private String flag;
    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_sign;
    }

    @Override
    protected String getTitleName() {
        return null;
    }

    @Override
    protected void initActivity() {
        Intent it = getIntent();
        flag = it.getStringExtra(MConstants.ENTRANCE_FLAG);
    }

    @Override
    protected void initView() {
        login_viewPager = (ViewPager) findViewById(R.id.login_viewPager);
        login_tabLayout = (TabLayout) findViewById(R.id.login_tabLayout);

        pagerAdapter = new LoginAndSignUpPagerAdapter(this, getSupportFragmentManager());
        login_viewPager.setAdapter(pagerAdapter);
        login_tabLayout.setupWithViewPager(login_viewPager);//在设定Adapter之后才可执行
        login_tabLayout.setTabMode(TabLayout.MODE_FIXED);

        /**
         * 若是用户登录被挤掉,弹出被迫下线dialog
         */
        if (MConstants.LOGOUT_KICKOFF.equals(flag)) {
            dialog = new Dialog(this, R.style.LoadingDialogStyle);
            dialog.setContentView(R.layout.dialog_logout_kickoff);
            dialog.setCanceledOnTouchOutside(false);
            Calendar cal = Calendar.getInstance();
            ((TextView) dialog.findViewById(R.id.tv_tip_time)).setText(
                    String.format(getString(R.string.tip_kicked_out),
                            String.format(Locale.getDefault(), "%02d", cal.get(Calendar.HOUR_OF_DAY))
                                    + ":" + String.format(Locale.getDefault(), "%02d", cal.get(Calendar.MINUTE))));
            dialog.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            dialog.findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //申请去重新登录

                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }

    @Override
    protected void OnClick(View v) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
