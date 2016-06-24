package com.test.testdemo.CircleProgress;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.test.testdemo.R;

/**
 * Created by Will on 16/6/24.
 */
public class CircleProgress extends View {

    private static final int RED = 0xFFE5282C;
    private static final int YELLOW = 0xFF1F909A;
    private static final int BLUE = 0xFFFC9E12;
    private static final int COLOR_NUM = 3;
    private int[] COLORS;

    private TimeInterpolator mInterpolator = new EaseInOutCubicInterpolator();

    private final double DEGREE = Math.PI / 180;
    private Paint mPaint;
    private int mViewSize;
    private int mPointRadius;
    private long mStartTime;
    private long mPlayTime;
    private boolean mStartAnim = false;
    private Point mCenter = new Point();

    private ArcPoint[] mArcPoint;
    private static final int POINT_NUM = 15;
    private static final int DELTA_ANGLE = 360 / POINT_NUM;
    private long mDuration = 800;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgress, defStyle, 0);
        int color1 = a.getColor(R.styleable.CircleProgress_color_1, RED);
        int color2 = a.getColor(R.styleable.CircleProgress_color_2, YELLOW);
        int color3 = a.getColor(R.styleable.CircleProgress_color_3, BLUE);
        a.recycle();

        COLORS = new int[]{color1, color2, color3};

        initView();
    }

    private void initView() {

        mArcPoint = new ArcPoint[POINT_NUM];

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = getResources().getDimensionPixelSize(R.dimen.default_circle_view_size);
        int width = getDefaultSize(defaultSize, widthMeasureSpec);
        int height = getDefaultSize(defaultSize, heightMeasureSpec);
        mViewSize = Math.min(width, height);
        setMeasuredDimension(mViewSize, mViewSize);
        mCenter.set(mViewSize / 2, mViewSize / 2);

        calPoints(1.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenter.x, mCenter.y);

        float factor = getFactor();
        canvas.rotate(200 * factor);
        float x, y;
        for (int i = 0; i < POINT_NUM; ++i) {
            mPaint.setColor(mArcPoint[i].color);
            float itemFactor = getItemFactor(i, factor);
            x = mArcPoint[i].x - 2 * mArcPoint[i].x * itemFactor;
            y = mArcPoint[i].y - 2 * mArcPoint[i].y * itemFactor;
            canvas.drawCircle(x, y, mPointRadius, mPaint);
        }

        canvas.restore();

        if (mStartAnim) {
            postInvalidate();
        }
    }

    private void calPoints(float factor) {
        int radius = (int) (mViewSize / 3 * factor);
        mPointRadius = radius / 12;

        for (int i = 0; i < POINT_NUM; ++i) {
            float x = radius * -(float) Math.sin(DEGREE * DELTA_ANGLE * i);
            float y = radius * -(float) Math.cos(DEGREE * DELTA_ANGLE * i);

            ArcPoint point = new ArcPoint(x, y, COLORS[i % COLOR_NUM]);
            mArcPoint[i] = point;
        }
    }


    private float getFactor() {
        if (mStartAnim) {
            mPlayTime = AnimationUtils.currentAnimationTimeMillis() - mStartTime;
        }
        float factor = mPlayTime / (float) mDuration;
        return factor % 1f;
    }

    private float getItemFactor(int index, float factor) {
        float itemFactor = (factor - 0.66f / POINT_NUM * index) * 3;
        if (itemFactor < 0f) {
            itemFactor = 0f;
        } else if (itemFactor > 1f) {
            itemFactor = 1f;
        }
        return mInterpolator.getInterpolation(itemFactor);
    }

    public void startAnim() {
        mPlayTime = mPlayTime % mDuration;
        mStartTime = AnimationUtils.currentAnimationTimeMillis() - mPlayTime;
        mStartAnim = true;
        postInvalidate();
    }

    public void reset() {
        stopAnim();
        mPlayTime = 0;
        postInvalidate();

    }

    public void stopAnim() {
        mStartAnim = false;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setRadius(float factor) {
        stopAnim();
        calPoints(factor);
        startAnim();
    }

    static class ArcPoint {
        float x;
        float y;
        int color;

        ArcPoint(float x, float y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    public class EaseInOutCubicInterpolator implements TimeInterpolator {

        @Override
        public float getInterpolation(float input) {
            if ((input *= 2) < 1.0f) {
                return 0.5f * input * input * input;
            }
            input -= 2;
            return 0.5f * input * input * input + 1;
        }

    }
}
