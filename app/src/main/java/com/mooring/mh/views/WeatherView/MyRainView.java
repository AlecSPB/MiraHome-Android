package com.mooring.mh.views.WeatherView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.mooring.mh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 16/4/18.
 */
public class MyRainView extends View {

    private static final int NUM_SNOWFLAKES = 150; //雨滴数量
    private static final int DELAY = 5; // 间隔时间
    private List<Bitmap> rainList = null;


    public MyRainView(Context context) {
        super(context);
    }

    public MyRainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int hei = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < 10; i++) {
            canvas.drawBitmap(rainList.get(getRandom(4)), i * getWidth() / 10, hei, null);
        }
        // 间隔DELAY重新绘制
        getHandler().postDelayed(runnable, DELAY);
    }

    //重新绘制线程
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            hei = hei+10;
            if(hei>getHeight()){
                hei = 0;
            }
            //自动刷新
            invalidate();
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            initSnow(w, h);
        }
    }

    private void initSnow(int width, int height) {
        rainList = new ArrayList<>();
        rainList.add(getBitmap(R.mipmap.ic_rain_1));
        rainList.add(getBitmap(R.mipmap.ic_rain_2));
        rainList.add(getBitmap(R.mipmap.ic_rain_3));
        rainList.add(getBitmap(R.mipmap.ic_rain_4));

    }

    private Bitmap getBitmap(int id) {
        return BitmapFactory.decodeResource(getResources(), id);
    }

    private static final java.util.Random RANDOM = new java.util.Random();

    private int getRandom(int num) {
        return (int) (RANDOM.nextFloat() * num);
    }
}
