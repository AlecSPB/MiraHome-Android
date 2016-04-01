package com.mooring.mh.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.mooring.mh.R;
import com.mooring.mh.adapter.DayAndMonthPagerAdapter;

/**
 * Created by Will on 16/3/28.
 */
public class MoreActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager main_viewPager;
    private TabLayout main_tabLayout;
    private ImageView imgView_back;
    private DayAndMonthPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);


        initView();

        initData();
    }

    private void initView() {

        main_viewPager = (ViewPager) findViewById(R.id.more_viewPager);
        main_tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        imgView_back = (ImageView) findViewById(R.id.imgView_back);


    }

    private void initData() {

        imgView_back.setOnClickListener(this);
        pagerAdapter = new DayAndMonthPagerAdapter(getSupportFragmentManager());
        main_viewPager.setAdapter(pagerAdapter);
        main_tabLayout.setupWithViewPager(main_viewPager);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgView_back:
                this.finish();
                break;
        }
    }
}
