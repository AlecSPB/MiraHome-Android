package com.mooring.mh.views.ChartView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.utils.MUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义x,y轴图标控件可以横向滑动,配合YAxisView使用
 *
 * @see YAxisView
 * <p/>
 * Created by Will on 16/4/29.
 */
public class MyChartView extends View {

    private Paint linePaint;//线条画笔
    private Paint areaPaint;//覆盖区域画笔
    private Paint pointPaintL;//线上圆点
    private Paint pointPaintB;//背景圆点
    private Paint axisPaint;//x轴画笔
    private Paint dataPaint;//x轴单位画笔
    private Path mPath;//平滑线条path
    private int lineColor = getResources().getColor(R.color.colorPurple);//线条颜色
    private int areaColor = getResources().getColor(R.color.colorPurple50);//填充颜色
    private int commColor = getResources().getColor(R.color.colorWhite50);//通用颜色

    private static final float SMOOTHNESS = 0.35f;  //当前设置为最高平滑度(smoother),该值需要<=0.5

    private int xSpace;//X轴刻度之间间距
    private int ySpace;//Y轴刻度之间距离
    private int circleSize;//线上原点大小

    private List<String> Xdatas;//x轴数据
    private List<String> Ydatas;//y轴数据
    private boolean isArea = false;//是否画覆盖区域
    private boolean isCircle = false;//是都线上画圆圈

    private int xLeft;//left
    private int xBottom;//bottom
    private int xTick;//刻度线长度

    private int viewW;
    private int viewH;

    private int num_x;//x轴单位数量
    private int num_y;//y轴单位数量

    private List<String> mValues;//图标的所有数据--对应y轴,默认x从0开始
    private float mMinY = -1;//目前默认mMinY为0
    private float mMaxY = -1;

    public MyChartView(Context context) {
        this(context, null);
    }

    public MyChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyChartView, defStyleAttr, 0);
        num_x = a.getInteger(R.styleable.MyChartView_chart_x, 0);
        num_y = a.getInteger(R.styleable.MyChartView_chart_y, 0);
        isArea = a.getBoolean(R.styleable.MyChartView_chart_area, false);
        isCircle = a.getBoolean(R.styleable.MyChartView_chart_circle, false);
        lineColor = a.getColor(R.styleable.MyChartView_line_color, Color.GREEN);
        areaColor = a.getColor(R.styleable.MyChartView_area_color, Color.GREEN);
        a.recycle();

        initView();
    }

    private void initView() {

        xSpace = MUtils.dp2px(getContext(), 30);
        ySpace = MUtils.dp2px(getContext(), 20);
        circleSize = MUtils.dp2px(getContext(), 8);
        xLeft = MUtils.dp2px(getContext(), 10);
        xBottom = MUtils.dp2px(getContext(), 10);
        xTick = MUtils.dp2px(getContext(), 5);

        mPath = new Path();

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(MUtils.dp2px(getContext(), 2));
        linePaint.setStyle(Paint.Style.STROKE);

        areaPaint = new Paint();
        areaPaint.setAntiAlias(true);
        areaPaint.setStyle(Paint.Style.FILL);
        areaPaint.setColor(areaColor);

        pointPaintL = new Paint();
        pointPaintL.setAntiAlias(true);
        pointPaintL.setStyle(Paint.Style.FILL);
        pointPaintL.setColor(commColor);

        pointPaintB = new Paint();
        pointPaintB.setAntiAlias(true);
        pointPaintB.setStyle(Paint.Style.FILL);
        pointPaintB.setColor(commColor);

        axisPaint = new Paint();
        axisPaint.setAntiAlias(true);
        axisPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        axisPaint.setStrokeWidth(MUtils.dp2px(getContext(), 1));
        axisPaint.setColor(commColor);

        dataPaint = new TextPaint();
        dataPaint.setTextSize(20);
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
        setMeasuredDimension(num_x * xSpace + xLeft * 2, num_y * ySpace + xBottom * 2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Xdatas == null || Xdatas.size() <= 0 || Ydatas == null || Ydatas.size() <= 0) {
            return;
        }
        /**
         * 绘制x轴,坐标单位,背景点
         */
        canvas.drawLine(xLeft, viewH - xBottom, viewW - xLeft, viewH - xBottom, axisPaint);
        for (int i = 0; i < Xdatas.size(); i++) {
            canvas.drawLine(xLeft + xSpace * i, viewH - xBottom, xLeft + xSpace * i,
                    viewH - xBottom + xTick, axisPaint);
            canvas.drawText(Xdatas.get(i), xSpace * i, viewH - xBottom + xTick * 2, dataPaint);
            for (int j = 0; j < Ydatas.size(); j++) {
                canvas.drawPoint(xSpace * i + xLeft, viewH - xBottom - ySpace * (j + 1), pointPaintL);
            }
        }

        /**
         * 绘制线条,填充区域,线上圆点
         */
        if (mValues == null || mValues.size() == 0) {
            return;
        }
        int size = mValues.size();
        final float height = getMeasuredHeight() - 2 * xBottom;
        final float width = getMeasuredWidth() - 2 * xLeft;

        final float dX = mValues.size() > 1 ? mValues.size() - 1 : 2;
        final float dY = (mMaxY - mMinY) > 0 ? (mMaxY - mMinY) : 2;

        mPath.reset();

        // 计算每个点的坐标
        List<PointF> points = new ArrayList<PointF>(size);
        for (int i = 0; i < size; i++) {
            float x = xLeft + i * width / dX;
            float y = xBottom + height - (Float.parseFloat(mValues.get(i)) - mMinY) * height / dY;
            points.add(new PointF(x, y));
        }

        // 计算平滑路径(贝塞尔曲线)
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

        // 绘制填充区域
        if (isArea && size > 0) {
            mPath.lineTo(points.get(size - 1).x, height + xBottom);
            mPath.lineTo(points.get(0).x, height + xBottom);
            mPath.close();
            canvas.drawPath(mPath, areaPaint);
        }

        // 绘制线上圆点
        if (isCircle) {
            for (PointF point : points) {
                canvas.drawCircle(point.x, point.y, circleSize / 2, pointPaintL);
            }
            pointPaintL.setStyle(Paint.Style.FILL);
            pointPaintL.setColor(Color.WHITE);
            for (PointF point : points) {
                canvas.drawCircle(point.x, point.y, circleSize / 2, pointPaintL);
            }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            viewW = getMeasuredWidth();
            viewH = getMeasuredHeight();
        }
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    public void setMax(float max) {
        this.setMaxAndMin(max, 0);
    }

    /**
     * 设置最大值和最小值
     *
     * @param max
     * @param min
     */
    public void setMaxAndMin(float max, float min) {
        this.mMaxY = max;
        this.mMinY = min;
    }

    /**
     * 为当前View添加数据
     *
     * @param xdata
     * @param ydata
     * @param values
     */
    public void setDatas(List<String> xdata, List<String> ydata, List<String> values) {
        this.Xdatas = xdata;
        this.Ydatas = ydata;
        mValues = values;

        //如果最大值没有设定,从给定数据中选取最大值
        if (mMaxY == -1 && values != null && values.size() > 0) {
            mMaxY = mMinY = Float.parseFloat(values.get(0));
            for (String y : values) {
                if (Float.parseFloat(y) > mMaxY)
                    mMaxY = Float.parseFloat(y);
                if (Float.parseFloat(y) < mMinY)
                    mMinY = Float.parseFloat(y);
            }
        }
        invalidate();
    }

    /**
     * 设置是否绘制填充区域
     *
     * @param isArea
     */
    public void setIsArea(boolean isArea) {
        this.isArea = isArea;
    }

    /**
     * 设置是否绘制线上圆点
     *
     * @param isCircle
     */
    public void setIsCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }
}
