package com.mooring.mh.views.WheelPicker.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.views.WheelPicker.AbstractWheelDecor;
import com.mooring.mh.views.WheelPicker.AbstractWheelPicker;
import com.mooring.mh.views.WheelPicker.IWheelPicker;
import com.mooring.mh.views.WheelPicker.WheelCrossPicker;

import java.util.List;

/**
 * 自定义小时和分钟选择滚动控件
 * <p/>
 * Created by Will on 16/5/5.
 */
public class WheelTimePicker extends LinearLayout implements IWheelPicker {

    private WheelHourPicker wheelHourPicker;
    private WheelMinutePicker wheelMinutePicker;
    protected AbstractWheelPicker.OnWheelChangeListener listener;
    private String hour, minute;
    public static final int HOUR = 8;
    public static final int MINUTE = 30;
    private int labelColor;

    public WheelTimePicker(Context context) {
        this(context, null);
    }

    public WheelTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        labelColor = getResources().getColor(R.color.colorPurple);
        int padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        int padding2x = padding * 3;

        LayoutParams llParams = new LayoutParams(-2, -2);

        wheelHourPicker = new WheelHourPicker(getContext());
        wheelMinutePicker = new WheelMinutePicker(getContext());


        TextView textView = new TextView(getContext());
        textView.setText(":");
        textView.setTextSize(CommonUtils.sp2px(getContext(), 12));
        textView.setTextColor(labelColor);
        textView.setPadding(padding2x, 0, padding2x, CommonUtils.dp2px(getContext(), 14));

        addView(wheelHourPicker, llParams);
        addView(textView, llParams);
        addView(wheelMinutePicker, llParams);

        initListener(wheelHourPicker, HOUR);
        initListener(wheelMinutePicker, MINUTE);
    }

    /**
     * 初始化监听
     *
     * @param picker
     * @param type
     */
    private void initListener(final WheelCrossPicker picker, final int type) {
        picker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {
                if (null != listener) listener.onWheelScrolling(deltaX, deltaY);
            }

            @Override
            public void onWheelSelected(View view, int index, String data) {
                if (type == HOUR) {
                    hour = data;
                }
                if (type == MINUTE) {
                    minute = data;
                }
                if (null != listener && !TextUtils.isEmpty(hour) && !TextUtils.isEmpty(minute)) {
                    listener.onWheelSelected(view, -1, hour + minute);
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
     * @param hour
     * @param minute
     */
    public void setCurrentData(String hour, String minute) {
        wheelHourPicker.setCurrentData(hour);
        wheelMinutePicker.setCurrentData(minute);
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
        wheelHourPicker.setItemIndex(index);
        wheelMinutePicker.setItemIndex(index);
    }

    @Override
    public void setItemSpace(int space) {
        wheelHourPicker.setItemSpace(space);
        wheelMinutePicker.setItemSpace(space);
    }

    @Override
    public void setItemCount(int count) {
        wheelHourPicker.setItemCount(count);
        wheelMinutePicker.setItemCount(count);
    }

    @Override
    public void setTextColor(int color) {
        wheelHourPicker.setTextColor(color);
        wheelMinutePicker.setTextColor(color);
    }

    @Override
    public void setTextSize(int size) {
        wheelHourPicker.setTextSize(size);
        wheelMinutePicker.setTextSize(size);
    }

    @Override
    public void clearCache() {
        wheelHourPicker.clearCache();
        wheelMinutePicker.clearCache();
    }

    @Override
    public void setCurrentTextColor(int color) {
        wheelHourPicker.setCurrentTextColor(color);
        wheelMinutePicker.setCurrentTextColor(color);
    }

    @Override
    public void setWheelDecor(boolean ignorePadding, AbstractWheelDecor decor) {
        wheelHourPicker.setWheelDecor(ignorePadding, decor);
        wheelMinutePicker.setWheelDecor(ignorePadding, decor);
    }
}
