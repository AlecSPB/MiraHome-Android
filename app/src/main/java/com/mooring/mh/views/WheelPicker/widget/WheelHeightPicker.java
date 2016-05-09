package com.mooring.mh.views.WheelPicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.mooring.mh.views.WheelPicker.WheelCurvedPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义data picker
 * <p/>
 * Created by Will on 16/4/1.
 */
public class WheelHeightPicker extends WheelCurvedPicker {
    private static final List<String> HEIGHTS = new ArrayList<String>();

    static {
        for (int i = 150; i < 241; i++) {
            HEIGHTS.add(String.valueOf(i));
        }
    }

    private List<String> heights = HEIGHTS;

    public WheelHeightPicker(Context context) {
        super(context);
        init();
    }

    public WheelHeightPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        super.setData(heights);
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
        for (int i = 0; i < heights.size(); i++) {
            if (data.equals(heights.get(i))) {
                setItemIndex(i);
            }
        }
    }

    /**
     * 设置当前item
     *
     * @param data
     */
    public void setCurrentData(int data) {
        int from = Integer.parseInt(heights.get(0));
        setItemIndex(data - from);
    }

}
