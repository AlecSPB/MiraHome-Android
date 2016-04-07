package com.mooring.mh.views.WheelPicker.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.mooring.mh.R;
import com.mooring.mh.views.WheelPicker.AbstractWheelDecor;
import com.mooring.mh.views.WheelPicker.AbstractWheelPicker;
import com.mooring.mh.views.WheelPicker.IWheelPicker;
import com.mooring.mh.views.WheelPicker.WheelCrossPicker;

import java.util.List;

/**
 * 体重和对应单位选择器
 * <p/>
 * Created by Will on 16/4/6.
 */
public class WheelWeightSelectPicker extends LinearLayout implements IWheelPicker {

    private WheelWeightPicker weightPicker;//身高
    private WheelWeightUnitPicker unitPicker;//单位
    protected AbstractWheelPicker.OnWheelChangeListener listener;
    private String weight, unit;
    public static final int WEIGHT = 0;
    public static final int UNIT = 1;

    public WheelWeightSelectPicker(Context context) {
        super(context);
        init();
    }

    public WheelWeightSelectPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        int padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        int padding2x = padding * 5;

        LayoutParams llParams = new LayoutParams(-2, -2);

        weightPicker = new WheelWeightPicker(getContext());
        unitPicker = new WheelWeightUnitPicker(getContext());
        weightPicker.setPadding(padding, 0, padding2x, 0);
        unitPicker.setPadding(padding, 0, padding2x, 0);

        addView(weightPicker, llParams);
        addView(unitPicker, llParams);

        initListener(weightPicker, WEIGHT);
        initListener(unitPicker, UNIT);
    }


    private void initListener(final WheelCrossPicker picker, final int type) {
        picker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {
                if (null != listener) listener.onWheelScrolling(deltaX, deltaY);
            }

            @Override
            public void onWheelSelected(View view, int index, String data) {
                if (type == WEIGHT) {
                    weight = data;
                }
                if (type == UNIT) {
                    unit = data;
                }
                if (!TextUtils.isEmpty(weight) && !TextUtils.isEmpty(unit)) {
                    listener.onWheelSelected(view, -1, weight + " " + unit);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (null != listener) {
                    listener.onWheelScrollStateChanged(state);
                }
            }
        });
    }

    /**
     * 设置当前身高数据
     *
     * @param weight
     * @param unit
     */
    public void setCurrentData(String weight, String unit) {
        weightPicker.setCurrentData(weight);
        unitPicker.setCurrentUnit(unit);
    }

    @Override
    public void setData(List<String> data) {
        throw new RuntimeException("Set data will not allow here!");
    }

    @Override
    public void setOnWheelChangeListener(AbstractWheelPicker.OnWheelChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void setItemIndex(int index) {
        weightPicker.setItemIndex(index);
        unitPicker.setItemIndex(index);
    }

    @Override
    public void setItemSpace(int space) {
        weightPicker.setItemSpace(space);
        unitPicker.setItemSpace(space);
    }

    @Override
    public void setItemCount(int count) {
        weightPicker.setItemCount(count);
        unitPicker.setItemCount(count);
    }

    @Override
    public void setTextColor(int color) {
        weightPicker.setTextColor(color);
        unitPicker.setTextColor(color);
    }

    @Override
    public void setTextSize(int size) {
        weightPicker.setTextSize(size);
        unitPicker.setTextSize(size);
    }

    @Override
    public void clearCache() {
        weightPicker.clearCache();
        unitPicker.clearCache();
    }

    @Override
    public void setCurrentTextColor(int color) {
        weightPicker.setCurrentTextColor(color);
        unitPicker.setCurrentTextColor(color);
    }

    @Override
    public void setWheelDecor(boolean ignorePadding, AbstractWheelDecor decor) {
        weightPicker.setWheelDecor(ignorePadding, decor);
        unitPicker.setWheelDecor(ignorePadding, decor);
    }
}
