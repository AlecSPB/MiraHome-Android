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
 * 身高和对应单位选择器
 * <p/>
 * Created by Will on 16/4/1.
 */
public class WheelHeightSelectPicker extends LinearLayout implements IWheelPicker {

    private WheelHeightPicker heightPicker;//身高
    private WheelHeightUnitPicker unitPicker;//单位
    protected AbstractWheelPicker.OnWheelChangeListener listener;
    private String height, unit;
    public static final int HEIGHT = 0;
    public static final int UNIT = 1;

    public WheelHeightSelectPicker(Context context) {
        super(context);
        init();
    }

    public WheelHeightSelectPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        int padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        int padding2x = padding * 5;

        LayoutParams llParams = new LayoutParams(-2, -2);

        heightPicker = new WheelHeightPicker(getContext());
        unitPicker = new WheelHeightUnitPicker(getContext());
        heightPicker.setPadding(padding, 0, padding2x, 0);
        unitPicker.setPadding(padding, 0, padding2x, 0);

        addView(heightPicker, llParams);
        addView(unitPicker, llParams);

        initListener(heightPicker, HEIGHT);
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
                if (type == HEIGHT) {
                    height = data;
                }
                if (type == UNIT) {
                    unit = data;
                }
                if (!TextUtils.isEmpty(height) && !TextUtils.isEmpty(unit)) {
                    listener.onWheelSelected(view, -1, height + " " + unit);
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
     * @param height
     * @param unit
     */
    public void setCurrentData(int height, String unit) {
        heightPicker.setCurrentData(height);
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
        heightPicker.setItemIndex(index);
        unitPicker.setItemIndex(index);
    }

    @Override
    public void setItemSpace(int space) {
        heightPicker.setItemSpace(space);
        unitPicker.setItemSpace(space);
    }

    @Override
    public void setItemCount(int count) {
        heightPicker.setItemCount(count);
        unitPicker.setItemCount(count);
    }

    @Override
    public void setTextColor(int color) {
        heightPicker.setTextColor(color);
        unitPicker.setTextColor(color);
    }

    @Override
    public void setTextSize(int size) {
        heightPicker.setTextSize(size);
        unitPicker.setTextSize(size);
    }

    @Override
    public void clearCache() {
        heightPicker.clearCache();
        unitPicker.clearCache();
    }

    @Override
    public void setCurrentTextColor(int color) {
        heightPicker.setCurrentTextColor(color);
        unitPicker.setCurrentTextColor(color);
    }

    @Override
    public void setWheelDecor(boolean ignorePadding, AbstractWheelDecor decor) {
        heightPicker.setWheelDecor(ignorePadding, decor);
        unitPicker.setWheelDecor(ignorePadding, decor);
    }
}
