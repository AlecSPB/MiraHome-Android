package com.mooring.mh.views.WheelPicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.mooring.mh.views.WheelPicker.WheelCurvedPicker;
import com.mooring.mh.views.WheelPicker.WheelStraightPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 基于WheelPicker的年份选择控件
 * YearPicker base on WheelPicker
 * <p/>
 * Created by Will on 16/3/31.
 */
public class WheelYearPicker extends WheelCurvedPicker {
    private static final List<String> YEARS = new ArrayList<>();
    private static final int FROM = 1900, TO = 2100;

    static {
        Calendar cal = Calendar.getInstance();
        for (int i = 1900; i <= cal.get(Calendar.YEAR); i++) {
            YEARS.add(String.valueOf(i));
        }
    }

    private List<String> years = YEARS;

    private int from = FROM, to = TO;
    private int year;

    public WheelYearPicker(Context context) {
        super(context);
        init();
    }

    public WheelYearPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.setData(years);
        setCurrentYear(Calendar.getInstance().get(Calendar.YEAR));
    }

    @Override
    public void setData(List<String> data) {
        throw new RuntimeException("Set data will not allow here!");
    }

    public void setYearRange(int yearFrom, int yearTo) {
        from = yearFrom;
        to = yearTo;
        years.clear();
        for (int i = yearFrom; i <= yearTo; i++) years.add(String.valueOf(i));
        super.setData(years);
    }

    public void setCurrentYear(int year) {
        year = Math.max(year, from);
        year = Math.min(year, to);
        this.year = year;
        int d = year - from;
        setItemIndex(d);
    }
}