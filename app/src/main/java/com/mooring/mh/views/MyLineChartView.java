package com.mooring.mh.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Will on 16/4/27.
 */
public class MyLineChartView extends LineChartView {
    public MyLineChartView(Context context) {
        super(context);
    }

    public MyLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
