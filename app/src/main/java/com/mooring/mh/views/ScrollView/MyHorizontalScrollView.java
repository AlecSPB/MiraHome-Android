package com.mooring.mh.views.ScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 自定义HorizontalScrollView,判断滑动到首尾位置的时候,把触摸事件传递给父控件
 * <p/>
 * Created by Will on 16/6/27.
 */
public class MyHorizontalScrollView extends HorizontalScrollView {

    private View contentView;  //ScrollView包含的子组件

    public MyHorizontalScrollView(Context context) {
        this(context, null);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        contentView = getChildAt(0);
        if (contentView.getMeasuredWidth() <= getScrollX() + getWidth()) {
            getParent().getParent().requestDisallowInterceptTouchEvent(false);
        } else if (getScrollX() == 0) {
            getParent().getParent().requestDisallowInterceptTouchEvent(false);
        } else {
            getParent().getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }
}
