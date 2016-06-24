package com.mooring.mh.views.WheelPicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.mooring.mh.views.WheelPicker.WheelCurvedPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 基于WheelPicker的月份选择控件
 * MonthPicker base on WheelPicker
 * <p/>
 * Created by Will on 16/3/31.
 */
public class WheelDayPicker extends WheelCurvedPicker {
    private static final HashMap<Integer, List<String>> DAYS = new HashMap<>();

    private static final Calendar C = Calendar.getInstance();

    private List<String> days = new ArrayList<>();

    private int day = C.get(Calendar.DAY_OF_MONTH);
    private int month = C.get(Calendar.MONTH) + 1;
    private int year = C.get(Calendar.YEAR);
    private int maxDay;

    public WheelDayPicker(Context context) {
        super(context);
        init();
    }

    public WheelDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initData();
        setCurrentDay();
    }

    private void initData() {
        int maxDay = C.getActualMaximum(Calendar.DATE);
        if (maxDay == this.maxDay) return;
        this.maxDay = maxDay;
        List<String> days;
        if (DAYS.containsKey(maxDay)) {
            days = DAYS.get(maxDay);
        } else {
            days = new ArrayList<>();
            for (int i = 1; i <= maxDay; i++)
                days.add(String.format(Locale.getDefault(), "%02d", i));
            DAYS.put(maxDay, days);
        }
        this.days = days;
        super.setData(days);
    }

    @Override
    public void setData(List<String> data) {
        throw new RuntimeException("Set data will not allow here!");
    }

    public void setCurrentDay(int day) {
        day = Math.max(day, 1);
        day = Math.min(day, maxDay);
        this.day = day;
        setCurrentDay();
    }

    private void setCurrentDay() {
        setItemIndex(day - 1);
    }

    public void setCurrentMonth(int month) {
        setMonth(month);
        initData();
    }

    private void setMonth(int month) {
        month = Math.max(month, 1);
        month = Math.min(month, 12);
        this.month = month;
        C.set(Calendar.MONTH, month - 1);
    }

    public void setCurrentYear(int year) {
        setYear(year);
        initData();
    }

    private void setYear(int year) {
        year = Math.max(year, 1);
        year = Math.min(year, Integer.MAX_VALUE - 1);
        this.year = year;
        C.set(Calendar.YEAR, year);
    }

    public void setCurrentYearAndMonth(int year, int month) {
        setYear(year);
        setMonth(month);
        initData();
        checkScrollState();
    }
}