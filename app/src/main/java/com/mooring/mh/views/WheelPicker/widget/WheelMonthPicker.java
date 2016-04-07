package com.mooring.mh.views.WheelPicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.mooring.mh.views.WheelPicker.WheelCurvedPicker;
import com.mooring.mh.views.WheelPicker.WheelStraightPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 基于WheelPicker的月份选择控件
 * MonthPicker base on WheelPicker
 * <p/>
 * Created by Will on 16/3/31.
 */
public class WheelMonthPicker extends WheelCurvedPicker {
    private static final List<String> MONTHS = new ArrayList<>();

    static {
        for (int i = 1; i <= 12; i++) MONTHS.add(String.valueOf(i));
    }

    private List<String> months = MONTHS;

    private int month;

    public WheelMonthPicker(Context context) {
        super(context);
        init();
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.setData(months);
        setCurrentMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
    }

    @Override
    public void setData(List<String> data) {
        throw new RuntimeException("Set data will not allow here!");
    }

    public void setCurrentMonth(int month) {
        month = Math.max(month, 1);
        month = Math.min(month, 12);
        this.month = month;
        setItemIndex(month - 1);
    }
}