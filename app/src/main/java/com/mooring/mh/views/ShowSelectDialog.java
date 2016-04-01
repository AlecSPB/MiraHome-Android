package com.mooring.mh.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.mooring.mh.R;
import com.mooring.mh.views.WheelPicker.AbstractWheelDecor;
import com.mooring.mh.views.WheelPicker.AbstractWheelPicker;
import com.mooring.mh.views.WheelPicker.IWheelPicker;
import com.mooring.mh.views.WheelPicker.WheelCrossPicker;
import com.mooring.mh.views.WheelPicker.WheelDayPicker;
import com.mooring.mh.views.WheelPicker.WheelMonthPicker;
import com.mooring.mh.views.WheelPicker.WheelStraightPicker;
import com.mooring.mh.views.WheelPicker.WheelYearPicker;

import java.util.List;

/**
 * 公共dialog选择器
 * <p/>
 * Created by Will on 16/4/1.
 */
public class ShowSelectDialog extends LinearLayout implements IWheelPicker {

    private WheelStraightPicker heightPicker;//身高
    private WheelStraightPicker unitPicker;//单位
    protected AbstractWheelPicker.OnWheelChangeListener listener;
    private String height = "";
    private String unit = "";

    public ShowSelectDialog(Context context) {
        super(context);
        init();
    }

    public ShowSelectDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        int padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        int padding2x = padding * 3;


        LayoutParams llParams = new LayoutParams(-2, -2);

        heightPicker = new WheelStraightPicker(getContext());
        unitPicker = new WheelStraightPicker(getContext());
        heightPicker.setPadding(padding2x, 0, padding2x, 0);
        unitPicker.setPadding(padding2x, 0, padding2x, 0);

        addView(heightPicker, llParams);
        addView(unitPicker, llParams);

        initListener(heightPicker, 0);
        initListener(unitPicker, 1);
    }


    private void initListener(final WheelCrossPicker picker, final int type) {
        picker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {
                if (null != listener) listener.onWheelScrolling(deltaX, deltaY);
            }

            @Override
            public void onWheelSelected(View view, int index, String data) {
                if (type == 0) {
                    height = data;
                }
                if (type == 1) {
                    unit = data;
                }
                if (isValidDate()) {
                    if (type == 0 || type == 1)
                        pickerDay.setCurrentYearAndMonth(Integer.valueOf(year),
                                Integer.valueOf(month));
                    if (null != listener)
                        listener.onWheelSelected(view, -1, year + "-" + month + "-" + day);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (type == 0) stateYear = state;
                if (type == 1) stateMonth = state;
                if (null != listener) checkState(listener);
            }
        });
    }

    public void setCurrentHeight(int height, int unit) {
        heightPicker.setCurrentYear(height);
        unitPicker.setCurrentMonth(unit);
    }

    @Override
    public void setData(List<String> data) {

    }

    @Override
    public void setOnWheelChangeListener(AbstractWheelPicker.OnWheelChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void setItemIndex(int index) {

    }

    @Override
    public void setItemSpace(int space) {

    }

    @Override
    public void setItemCount(int count) {

    }

    @Override
    public void setTextColor(int color) {

    }

    @Override
    public void setTextSize(int size) {

    }

    @Override
    public void clearCache() {

    }

    @Override
    public void setCurrentTextColor(int color) {

    }

    @Override
    public void setWheelDecor(boolean ignorePadding, AbstractWheelDecor decor) {

    }
}
