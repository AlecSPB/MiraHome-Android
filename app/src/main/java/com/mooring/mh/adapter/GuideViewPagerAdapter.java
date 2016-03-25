package com.mooring.mh.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Will on 16/3/24.
 */
public class GuideViewPagerAdapter extends PagerAdapter {
    // 界面列表
    private ArrayList<View> views;

    public GuideViewPagerAdapter(ArrayList<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    /**
     * 初始化position位置的界面
     */
    @Override
    public Object instantiateItem(View view, int position) {

        ((ViewPager) view).addView(views.get(position), 0);

        return views.get(position);
    }

    @Override
    public void destroyItem(View view, int position, Object object) {
        ((ViewPager) view).removeView(views.get(position));
    }
}
