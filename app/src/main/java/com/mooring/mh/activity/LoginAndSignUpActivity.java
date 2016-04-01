package com.mooring.mh.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mooring.mh.R;
import com.mooring.mh.adapter.LoginAndSignUpPagerAdapter;

/**
 * Created by Will on 16/3/30.
 */
public class LoginAndSignUpActivity extends AppCompatActivity {
    private ViewPager login_viewPager;
    private TabLayout login_tabLayout;
    private LoginAndSignUpPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign);

        initView();

        initData();
    }

    private void initView() {

        login_viewPager = (ViewPager) findViewById(R.id.login_viewPager);
        login_tabLayout = (TabLayout) findViewById(R.id.login_tabLayout);

    }

    private void initData() {

        pagerAdapter = new LoginAndSignUpPagerAdapter(getSupportFragmentManager());
        login_viewPager.setAdapter(pagerAdapter);
        login_tabLayout.setupWithViewPager(login_viewPager);

    }

}
