package com.mooring.mh.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 16/4/29.
 */
public class MyChartView extends View {

    private int lineColor;
    private int areaColor;
    private int commColor;

    private Paint linePaint;//线条画笔
    private Paint areaPaint;
    private Paint pointPaint;
    private Paint gridPaint;
    private Paint axisPaint;
    private Paint dataPaint;
    private Path mPath;
    private static final float SMOOTHNESS = 0.35f; // the higher the smoother, but don't go over 0.5

    private int Xspace;//X轴刻度之间间距
    private int Yspace;//Y轴刻度之间距离
    private int circleRadius;//背景远点大小
    private int circleSize;//线上原点大小

    private List<String> Xdatas;
    private List<String> Ydatas;
    private boolean isArea = true;//是否画覆盖区域
    private boolean isCircle = true;//是都线上画圆圈

    public MyChartView(Context context) {
        this(context, null);
    }

    public MyChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        lineColor = getResources().getColor(R.color.colorPurple);
        areaColor = getResources().getColor(R.color.colorPurple50);
        commColor = getResources().getColor(R.color.colorWhite50);
        Xspace = CommonUtils.dp2px(getContext(), 50);
        circleRadius = CommonUtils.dp2px(getContext(), 1);
        circleSize = CommonUtils.dp2px(getContext(), 8);

        mPath = new Path();

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(CommonUtils.dp2px(getContext(), 3));
        linePaint.setStyle(Paint.Style.STROKE);

        areaPaint = new Paint();
        areaPaint.setAntiAlias(true);
        areaPaint.setStyle(Paint.Style.FILL);
        areaPaint.setColor(areaColor);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(commColor);

        gridPaint = new Paint();
        gridPaint.setColor(commColor);
        gridPaint.setAntiAlias(true);
        gridPaint.setStyle(Paint.Style.FILL);

        axisPaint = new Paint();
        axisPaint.setAntiAlias(true);
        axisPaint.setStyle(Paint.Style.STROKE);
        axisPaint.setStrokeWidth(CommonUtils.dp2px(getContext(), 1));
        axisPaint.setColor(commColor);

        dataPaint = new Paint();
        dataPaint.setAntiAlias(true);
        dataPaint.setStyle(Paint.Style.STROKE);
        dataPaint.setColor(commColor);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(2000, sizeHeight);
    }

    int xLeft = CommonUtils.dp2px(getContext(), 5);
    int xBottom = CommonUtils.dp2px(getContext(), 30);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        if (Xdatas == null || Xdatas.size() < 0 || Ydatas == null || Ydatas.size() <= 0) {
            return;
        }
        Xspace = viewW / Xdatas.size();
        Yspace = viewH / Ydatas.size();
        canvas.drawLine(xLeft, viewH - xBottom, viewW - xLeft, viewH - xBottom, axisPaint);
        for (int i = 1; i < Xdatas.size(); i++) {
            canvas.drawLine(xLeft + Xspace * i, viewH - xBottom, xLeft + Xspace * i, viewH - xBottom + xLeft, axisPaint);
            canvas.drawText(Xdatas.get(i), Xspace * i, viewH - xBottom + xLeft * 2, dataPaint);
            for (int j = 1; j < Ydatas.size(); j++) {
                canvas.drawCircle(Xspace * i + xLeft, viewH - xBottom - Yspace * j, circleRadius, axisPaint);
            }
        }

        //划线

        if (mValues == null || mValues.length == 0) {
            return;
        }
        int size = mValues.length;

        final float height = getMeasuredHeight() - 2 * xBottom;
        final float width = getMeasuredWidth() - 2 * xLeft;

        Log.e("onMeasure", "  " + height + "   " + width);

        final float dX = mValues.length > 1 ? mValues.length - 1 : (2);
        final float dY = (mMaxY - mMinY) > 0 ? (mMaxY - mMinY) : (2);

        mPath.reset();

        // calculate point coordinates
        List<PointF> points = new ArrayList<PointF>(size);
        for (int i = 0; i < size; i++) {
            float x = xLeft + i * width / dX;
            float y = xBottom + height - (mValues[i] - mMinY) * height / dY;
            points.add(new PointF(x, y));
        }

        // calculate smooth path
        float lX = 0, lY = 0;
        mPath.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < size; i++) {
            PointF p = points.get(i);    // current point

            // first control point
            PointF p0 = points.get(i - 1);    // previous point
            float x1 = p0.x + lX;
            float y1 = p0.y + lY;

            // second control point
            PointF p1 = points.get(i + 1 < size ? i + 1 : i);    // next point
            lX = (p1.x - p0.x) / 2 * SMOOTHNESS;        // (lX,lY) is the slope of the reference line
            lY = (p1.y - p0.y) / 2 * SMOOTHNESS;
            float x2 = p.x - lX;
            float y2 = p.y - lY;

            // add line
            mPath.cubicTo(x1, y1, x2, y2, p.x, p.y);
        }
        canvas.drawPath(mPath, linePaint);

        // draw area
        if (isArea && size > 0) {
            mPath.lineTo(points.get(size - 1).x, height + xBottom);
            mPath.lineTo(points.get(0).x, height + xBottom);
            mPath.close();
            canvas.drawPath(mPath, areaPaint);
        }

        // draw circles
        if (isCircle) {
            for (PointF point : points) {
                canvas.drawCircle(point.x, point.y, circleSize / 2, pointPaint);
            }
            pointPaint.setStyle(Paint.Style.FILL);
            pointPaint.setColor(Color.WHITE);
            for (PointF point : points) {
                canvas.drawCircle(point.x, point.y, circleSize / 2, pointPaint);
            }
        }

    }

    int viewW;
    int viewH;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("onSizeChanged", "  " + w + "  " + h + "  " + oldw + "  " + oldh);
        if (w != oldw || h != oldh) {
            viewW = getMeasuredWidth() - 2 * xLeft;
            viewH = getMeasuredHeight() - 2 * xBottom;
        }
    }

    private float[] mValues;
    private float mMinY;
    private float mMaxY;

    public void setDatas(List<String> xdata, List<String> ydata, float[] values) {
        this.Xdatas = xdata;
        this.Ydatas = ydata;
        mValues = values;

        if (values != null && values.length > 0) {
            mMaxY = values[0];
            //mMinY = values[0].y;
            for (float y : values) {
                if (y > mMaxY)
                    mMaxY = y;
                /*if (y < mMinY)
                    mMinY = y;*/
            }
        }

        invalidate();
    }
}
