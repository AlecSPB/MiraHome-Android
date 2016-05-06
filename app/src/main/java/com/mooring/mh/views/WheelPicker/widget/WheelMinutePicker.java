package com.mooring.mh.views.WheelPicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.mooring.mh.views.WheelPicker.WheelStraightPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 16/5/5.
 */
public class WheelMinutePicker extends WheelStraightPicker {

    private static final List<String> MINUTES = new ArrayList<String>();

    static {
        for (int i = 0; i < 60; i++) {
            MINUTES.add(String.format("%02d", i));
        }
    }

    private List<String> minutes = MINUTES;

    public WheelMinutePicker(Context context) {
        this(context, null);
    }

    public WheelMinutePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    private void initView() {
        super.setData(minutes);
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
        for (int i = 0; i < minutes.size(); i++) {
            if (data.equals(minutes.get(i))) {
                setItemIndex(i);
            }
        }
    }
}
