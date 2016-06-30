package com.mooring.mh.activity;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mooring.mh.R;
import com.mooring.mh.adapter.GuideViewPagerAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 帮助Activity,包含App的使用导航
 * <p>
 * Created by Will on 16/6/3.
 */
public class HelpActivity extends BaseActivity {

    private ViewPager help_viewpager;

    // 定义底部小点图片
    private ImageView help_one, help_two, help_three, help_four;

    // 定义各个界面View对象
    private View view1, view2, view3, view4;

    // 定义ViewPager适配器
    private GuideViewPagerAdapter vpAdapter;

    // 定义一个ArrayList来存放View
    private ArrayList<View> views;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected void initActivity() {

    }

    @Override
    protected String getTitleName() {
        return getString(R.string.title_help);
    }

    @Override
    protected void initView() {
        // 实例化各个界面的布局对象
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.help_page_one, null);
        view2 = mLi.inflate(R.layout.help_page_two, null);
        view3 = mLi.inflate(R.layout.help_page_three, null);
        view4 = mLi.inflate(R.layout.help_page_four, null);

        // 实例化ViewPager
        help_viewpager = (ViewPager) findViewById(R.id.help_viewpager);

        // 实例化底部小点图片对象
        help_one = (ImageView) findViewById(R.id.help_one);
        help_two = (ImageView) findViewById(R.id.help_two);
        help_three = (ImageView) findViewById(R.id.help_three);
        help_four = (ImageView) findViewById(R.id.help_four);


        // 设置监听
        help_viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        // 实例化ArrayList对象
        views = new ArrayList<View>();
        // 将要分页显示的View装入数组中
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        // 实例化ViewPager适配器
        vpAdapter = new GuideViewPagerAdapter(views);
        // 设置适配器数据
        help_viewpager.setAdapter(vpAdapter);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    help_one.setImageResource(R.drawable.bg_circle_indicator_select);
                    help_two.setImageResource(R.drawable.bg_circle_indicator_normal);
                    break;
                case 1:
                    help_two.setImageResource(R.drawable.bg_circle_indicator_select);
                    help_one.setImageResource(R.drawable.bg_circle_indicator_normal);
                    help_three.setImageResource(R.drawable.bg_circle_indicator_normal);
                    break;
                case 2:
                    help_three.setImageResource(R.drawable.bg_circle_indicator_select);
                    help_two.setImageResource(R.drawable.bg_circle_indicator_normal);
                    help_four.setImageResource(R.drawable.bg_circle_indicator_normal);
                    break;
                case 3:
                    help_four.setImageResource(R.drawable.bg_circle_indicator_select);
                    help_three.setImageResource(R.drawable.bg_circle_indicator_normal);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    @Override
    protected void OnClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Help");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Help");
        MobclickAgent.onPause(this);
    }
}
