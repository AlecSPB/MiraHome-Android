package com.mooring.mh.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.adapter.DayAndMonthPagerAdapter;

/**
 * 睡眠数据详情Activity 页面  展现Day或Month的所有数据
 *
 * @see com.mooring.mh.fragment.DayFragment
 * @see com.mooring.mh.fragment.MonthFragment
 * <p/>
 * Created by Will on 16/3/28.
 */
/*public class SleepDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager more_viewPager;
    private TabLayout more_tabLayout;
    private ImageView imgView_back;
    private DayAndMonthPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_detail);

        initView();

        initData();
    }

    private void initView() {

        more_viewPager = (ViewPager) findViewById(R.id.more_viewPager);
        more_tabLayout = (TabLayout) findViewById(R.id.more_tabLayout);
        imgView_back = (ImageView) findViewById(R.id.imgView_back);

    }

    private void initData() {

        imgView_back.setOnClickListener(this);
        pagerAdapter = new DayAndMonthPagerAdapter(this, getSupportFragmentManager());
        more_viewPager.setAdapter(pagerAdapter);
        more_tabLayout.setupWithViewPager(more_viewPager);
        more_tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgView_back:
                this.finish();
                break;
        }
    }
}*/

public class SleepDetailActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager more_viewPager;
    private TabLayout more_tabLayout;
    private DayAndMonthPagerAdapter pagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sleep_detail;
    }

    @Override
    protected String getTitleName() {
        return null;
    }

    @Override
    protected void initActivity() {
    }

    @Override
    protected void initView() {
        more_viewPager = (ViewPager) findViewById(R.id.more_viewPager);
        more_tabLayout = (TabLayout) findViewById(R.id.more_tabLayout);

        pagerAdapter = new DayAndMonthPagerAdapter(this, getSupportFragmentManager());
        more_viewPager.setAdapter(pagerAdapter);
        more_tabLayout.setupWithViewPager(more_viewPager);
        more_tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void OnClick(View v) {
    }
}
