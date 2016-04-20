package com.mooring.mh.views.WeatherView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.mooring.mh.views.other.WeatherView;

/**
 * Created by Will on 16/3/24.
 */
public class SnowFlake {
    private static final float ANGLE_RANGE = 0.1f;//角度范围
    private static final float HALF_ANGLE_RANGE = ANGLE_RANGE / 2f; //半个角度范围
    private static final float HALF_PI = (float) (Math.PI / 2f);//半个PI
    private static final float ANGLE_SEED = 25f;

    private static final float ANGLE_DIVISOR = 10000f;
    // 下落物的移动速度
    private static final float INCREMENT_LOWER = 2f;
    private static final float INCREMENT_UPPER = 4f;

    // 下落物的大小
    private static final float FLAKE_SIZE_LOWER = 1f;
    private static float FLAKE_SIZE_UPPER = 60f;

    private final RandomGenerator mRandom; // 随机控制器
    private final Point mPosition; // 下落物位置
    private float mAngle; // 角度
    private final float mIncrement; // 下落物的速度
    public final float mFlakeSize; // 下落物的大小
    private Bitmap bitmap;//下落物bitmap
    private int mKind;//碎片类型
    private int t = 0;//加速度

    private SnowFlake(RandomGenerator random, Point position, float angle,
                      float increment, float flakeSize, int kind) {
        mRandom = random;
        mPosition = position;
        mIncrement = increment;
        mFlakeSize = flakeSize;
        mAngle = angle;
        mKind = kind;

    }

    public static SnowFlake create(int width, int height, int kind) {
        RandomGenerator random = new RandomGenerator();
        int x = random.getRandom(width);
        int y = random.getRandom(height);
        Point position = new Point(x, y);
        float angle = random.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGLE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
        float increment;
        float flakeSize;
        if (kind == WeatherView.LIGHT_SNOW || kind == WeatherView.HEAVY_SNOW) {
            increment = random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);
        } else {
            increment = random.getRandom(INCREMENT_LOWER * 10, INCREMENT_UPPER * 10);
        }
        if (kind == WeatherView.HEAVY_SNOW || kind == WeatherView.FREEZING_RAIN || kind == WeatherView.SLEET) {
            flakeSize = random.getSizeRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER);
        } else {
            flakeSize = random.getSizeRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER - 20);
        }
        return new SnowFlake(random, position, angle, increment, flakeSize, kind);
    }

    // 绘制雪花
    public void draw(Canvas canvas, Bitmap bitmap) {
        this.bitmap = bitmap;
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        move(width, height);
//        canvas.drawCircle(mPosition.x, mPosition.y, mFlakeSize, mPaint);
        canvas.drawBitmap(bitmap, mPosition.x, mPosition.y, null);
    }

    // 移动雪花
    private void move(int width, int height) {
        double x, y;
        if (mKind == WeatherView.LIGHT_SNOW || mKind == WeatherView.HEAVY_SNOW) {
            //x水平方向，那么需要晃动，主要设置这个值就可以，现在取消晃动了
            //如果 mPosition.x不加上后面那个值，就不会晃动了
            x = mPosition.x + (mIncrement * Math.cos(mAngle));
            //y是竖直方向，就是下落
            y = mPosition.y + (mIncrement * Math.sin(mAngle));
        } else {

            x = mPosition.x;
            //y是竖直方向，就是下落
            t++;
            y = mPosition.y + mIncrement+0.01*t*t;

        }

        mAngle += mRandom.getRandom(-ANGLE_SEED, ANGLE_SEED) / ANGLE_DIVISOR;

        //这个是设置雪花位置，如果在很短时间内刷新一次，就是连起来的动画效果
        mPosition.set((int) x, (int) y);

        // 移除屏幕, 重新开始
        if (!isInside(width, height)) {
            // 重置雪花
            reset(width);
        }
    }

    // 判断是否在其中
    private boolean isInside(int width, int height) {
        int x = mPosition.x;
        int y = mPosition.y;
        int size = bitmap.getWidth();
        int hei = bitmap.getHeight();
        return x > size / 2 - 5 && x + size <= width && y >= -hei - 1 && y - hei < height;
    }

    // 重置雪花
    private void reset(int width) {
        t= 0;
        mPosition.x = mRandom.getRandom(width);
        mPosition.y = -bitmap.getHeight() - 1; // 最上面
        mAngle = mRandom.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGLE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
    }

}
