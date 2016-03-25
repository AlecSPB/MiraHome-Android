package com.mooring.mh.views.WeatherView;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.mooring.mh.R;

/**
 * 雨滴视图,间隔DELAY时间来绘制NUM_SNOWFLAKES个雨滴
 */
public class RainView extends View {

    private static final int NUM_SNOWFLAKES = 150; //雨滴数量
    private static final int DELAY = 5; // 间隔时间
    private RainFlake[] mSnowFlakes; // 全部雨滴

    public RainView(Context context) {
        super(context);
    }

    public RainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            initSnow(w, h);
        }
    }

    private void initSnow(int width, int height) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // 抗锯齿
        paint.setColor(getResources().getColor(R.color.colorOrange)); // 设置雨滴的颜色
        paint.setStyle(Paint.Style.FILL); // 填充
        mSnowFlakes = new RainFlake[NUM_SNOWFLAKES];
        //mSnowFlakes所有雨滴的生成都在这个集合里
        for (int i = 0; i < NUM_SNOWFLAKES; ++i) {
            mSnowFlakes[i] = RainFlake.create(width, height, paint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (RainFlake s : mSnowFlakes) {
            //绘制
            s.draw(canvas);
        }
        // 间隔DELAY重新绘制
        getHandler().postDelayed(runnable, DELAY);
    }

    //重新绘制线程
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //自动刷新
            invalidate();
        }
    };
}
