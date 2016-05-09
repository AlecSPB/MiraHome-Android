package com.mooring.mh.views.WheelPicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.mooring.mh.views.WheelPicker.WheelCurvedPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 体重选择器
 * <p/>
 * Created by Will on 16/4/6.
 */
public class WheelWeightPicker extends WheelCurvedPicker {

    private static final List<String> WEIGHTS = new ArrayList<String>();

    static {
        for (int i = 0; i < 120; i++) {
            if (i % 2 == 0) {
                WEIGHTS.add(String.valueOf(i / 2 + 40));
            } else {
                WEIGHTS.add(String.valueOf((float) (i * 0.5) + 40));
            }
        }
    }

    private List<String> weights = WEIGHTS;

    public WheelWeightPicker(Context context) {
        super(context);
        init();
    }

    public WheelWeightPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        super.setData(weights);
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
        for (int i = 0; i < weights.size(); i++) {
            if (data.equals(weights.get(i))) {
                setItemIndex(i);
            }
        }
    }

}
