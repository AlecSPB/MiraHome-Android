package com.mooring.mh.views.WheelPicker;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by Will on 16/4/1.
 */
public class WheelDataPicker extends WheelStraightPicker {
    private List<String> datas;

    public WheelDataPicker(Context context) {
        super(context);
        init();
    }

    public WheelDataPicker(Context context, List<String> datas) {
        super(context);
        this.datas = datas;
        init();
    }

    public WheelDataPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        super.setData(datas);
        setItemIndex(0);
    }

    /**
     * 设置当前item
     *
     * @param data
     */
    public void setCurrentData(String data) {
        for (int i = 0; i < datas.size(); i++) {
            if (data.equals(datas.get(i))) {
                setItemIndex(i);
            }
        }
    }

}
