package com.mooring.mh.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mooring.mh.R;
import com.mooring.mh.fragment.DayFragment;
import com.mooring.mh.fragment.MonthFragment;

/**
 * Created by Will on 16/3/28.
 */
public class DayAndMonthPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public DayAndMonthPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: {
                return new DayFragment();
            }
            case 1: {
                return new MonthFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.tv_day);
            case 1:
                return context.getResources().getString(R.string.tv_month);
        }
        return null;
    }
}
