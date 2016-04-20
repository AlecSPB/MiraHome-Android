package com.mooring.mh.views.WeatherView;

import android.graphics.Canvas;
import android.graphics.Paint;


/**
 * 雨滴类,当移除屏幕时会重新设置其位置
 * <p/>
 * Created by Will on 16/3/24.
 */
public class RainFlake {

    // 移动速度
    private static final float INCREMENT_LOWER = 6f;
    private static final float INCREMENT_UPPER = 8f;

    // 大小
    private static final float FLAKE_SIZE_LOWER = 2f;
    private static final float FLAKE_SIZE_UPPER = 5f;

    private final float mIncrement; // 雨滴大小
    private final float mFlakeSize; // 下落速度
    private final Paint mPaint; // 雨滴画笔

    private Line mLine; // 雨滴

    private RandomGenerator mRandom;

    private RainFlake(RandomGenerator random, Line line, float increment, float flakeSize, Paint paint) {
        mRandom = random;
        mLine = line;
        mIncrement = increment;
        mFlakeSize = flakeSize;
        mPaint = paint;
    }

    //生成雨滴
    public static RainFlake create(int width, int height, Paint paint) {
        RandomGenerator random = new RandomGenerator();
        int[] nline;
        nline = random.getLine(width, height);

        Line line = new Line(nline[0], nline[1], nline[2], nline[3]);
        float increment = random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);
        float flakeSize = random.getRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER);
        return new RainFlake(random, line, increment, flakeSize, paint);
    }

    // 绘制雨滴
    public void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        drawLine(canvas, width, height);
    }

    /**
     * 画出类似雨滴下落的效果
     *
     * @param canvas
     * @param width
     * @param height
     */
    private void drawLine(Canvas canvas, int width, int height) {
        mPaint.setStrokeWidth(mFlakeSize);
        //y是下落方向
        double y1 = mLine.y1 + (mIncrement * Math.sin(1.5));
        double y2 = mLine.y2 + (mIncrement * Math.sin(1.5));

        //设置雨滴的位置,快速刷新呈现下落效果
        mLine.set(mLine.x1, (int) y1, mLine.x2, (int) y2);

        if (!isInsideLine(height)) {
            resetLine(width, height);
        }

        canvas.drawLine(mLine.x1, mLine.y1, mLine.x2, mLine.y2, mPaint);
    }

    //判断是否在屏幕内
    private boolean isInsideLine(int height) {
        return mLine.y1 < height && mLine.y2 < height;
    }

    //重置雨滴
    private void resetLine(int width, int height) {
        int[] nline;
        nline = mRandom.getLine(width, height);
        mLine.x1 = nline[0];
        mLine.y1 = nline[1];
        mLine.x2 = nline[2];
        mLine.y2 = nline[3];
    }

}
