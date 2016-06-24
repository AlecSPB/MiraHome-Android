package com.mooring.mh.views.WheelPicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.mooring.mh.views.WheelPicker.WheelStraightPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 小时选择器
 *
 * Created by Will on 16/5/5.
 */
public class WheelHourPicker extends WheelStraightPicker {

    private static final List<String> HOURS = new ArrayList<String>();

    static {
        for (int i = 0; i < 24; i++) {
            HOURS.add(String.format(Locale.getDefault(), "%02d", i));
        }
    }

    public WheelHourPicker(Context context) {
        this(context, null);
    }

    public WheelHourPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private List<String> hours = HOURS;

    private void initView() {
        super.setData(hours);
        setItemIndex(0);
    }

    @Override
    public void setData(List<String> data) {
        throw new RuntimeException("Set data will not allow here!");
    }

    /**
     * 设置当前item
     *
     * @param data
     */
    public void setCurrentData(String data) {
        for (int i = 0; i < hours.size(); i++) {
            if (data.equals(hours.get(i))) {
                setItemIndex(i);
            }
        }
    }
}
