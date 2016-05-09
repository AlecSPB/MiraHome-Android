package com.mooring.mh.views.WheelPicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.mooring.mh.views.WheelPicker.WheelCurvedPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 身高单位选择器
 * <p/>
 * Created by Will on 16/4/6.
 */
public class WheelHeightUnitPicker extends WheelCurvedPicker {
    private static final List<String> UNITS = new ArrayList<String>();

    static {
        UNITS.add("cm");
        UNITS.add("inch");
    }

    private List<String> units = UNITS;

    public WheelHeightUnitPicker(Context context) {
        super(context);
        init();
    }

    public WheelHeightUnitPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        super.setData(units);
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
    public void setCurrentUnit(String data) {
        for (int i = 0; i < units.size(); i++) {
            if (data.equals(units.get(i))) {
                setItemIndex(i);
            }
        }
    }

}
