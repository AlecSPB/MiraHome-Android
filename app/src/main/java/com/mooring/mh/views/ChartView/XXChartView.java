package com.mooring.mh.views.ChartView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.utils.MUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义双X轴图标控件,借助YAxisView作为Y轴
 *
 * @see YAxisView
 * <p/>
 * Created by Will on 16/5/3.
 */
public class XXChartView extends View {
    private Paint bgPaint;//背景网格点
    private Paint currPaint;//当天竖线
    private Paint linePaint;//曲线
    private Paint pointPaint;//线上圆点
    private int xLeft = MUtils.dp2px(getContext(), 20);//left
    private int xSpace = MUtils.dp2px(getContext(), 40);//x间隔
    private int ySpace = MUtils.dp2px(getContext(), 20);//y间隔
    private int topText = MUtils.dp2px(getContext(), 10);//顶部文字距离上部距离
    private int xBottom = MUtils.dp2px(getContext(), 40);//bottom
    private int textHeight = MUtils.dp2px(getContext(), 30);//文字外容器高度
    private int commColor = getResources().getColor(R.color.colorWhite50);//通用颜色
    private int currColor = getResources().getColor(R.color.colorPurple);//当天竖线颜色

    private List<String> sleepValues;//睡眠数据
    private List<String> deepValues;//深睡眠数据
    private List<String> Xdatas;//x轴数据
    private String month;//格式 "2015-10"

    private int num_x = 30;//x轴单位数量
    private int num_y = 12;//y轴单位数量
    private static final float SMOOTHNESS = 0.35f;  //当前设置为最高平滑度(smoother),该值需要<=0.5
    private float mMinY = 0;//目前默认mMinY为0
    private float mMaxY = 12;//Y轴最大
    private Path mPath_deep;//平滑线条path
    private Path mPath_sleep;//平滑线条path
    private int color_deep = getResources().getColor(R.color.colorPurple);//线条颜色
    private int color_sleep = Color.WHITE;//线条颜色
    private int circleSize = MUtils.dp2px(getContext(), 8);//线上原点大小

    private int tvHeight = MUtils.dp2px(this.getContext(), 20);
    private int tvWidth = MUtils.dp2px(this.getContext(), 20);
    private Rect targetRect;
    private Paint commonPaint;
    private Paint.FontMetricsInt fontMetrics;
    private int textSize = MUtils.sp2px(getContext(), 15);//文字大小

    private int viewW;
    private int viewH;

    public XXChartView(Context context) {
        this(context, null);
    }

    public XXChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XXChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(commColor);

        currPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        currPaint.setStrokeWidth(MUtils.dp2px(getContext(), 1));
        currPaint.setColor(currColor);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(MUtils.dp2px(getContext(), 2));
        linePaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setStyle(Paint.Style.FILL);

        commonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPath_deep = new Path();
        mPath_sleep = new Path();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(xLeft * 2 + xSpace * num_x, ySpace * num_y + 2 * xBottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            viewW = getMeasuredWidth();
            viewH = getMeasuredHeight();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Xdatas == null || Xdatas.size() <= 0) {
            return;
        }
        /**
         * 绘制上部和下部x轴,背景点
         */
        for (int i = 0; i < Xdatas.size(); i++) {
            drawText(canvas, Xdatas.get(i), textSize, Color.WHITE, xLeft + xSpace * i, topText);
            drawText(canvas, MUtils.getWeek(month + "-" + String.format("%02d", Integer.parseInt(Xdatas.get(i)))),
                    textSize, Color.WHITE, xLeft + xSpace * i, viewH - textHeight);
            for (int j = 0; j <= num_y; j++) {
                canvas.drawPoint(xSpace * i + xLeft, viewH - xBottom - ySpace * j, bgPaint);
            }
            //如果是当天日期,绘制竖线
            if (MUtils.getCurrDate().equals(month + "-" + String.format("%02d", Integer.parseInt(Xdatas.get(i))))) {
                canvas.drawLine(xLeft + xSpace * i, xBottom, xLeft + xSpace * i, viewH - xBottom, currPaint);
            }
        }

        /**
         * 绘制曲线
         */

        drawLine(canvas, sleepValues, mMaxY, mPath_sleep, color_sleep);

        drawLine(canvas, deepValues, mMaxY, mPath_deep, color_deep);

    }

    /**
     * 绘制曲线
     *
     * @param canvas
     * @param mValues
     * @param mMaxY
     * @param mPath
     * @param lineColor
     */
    private void drawLine(Canvas canvas, List<String> mValues, float mMaxY, Path mPath, int lineColor) {

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
        linePaint.setColor(lineColor);
        canvas.drawPath(mPath, linePaint);

        // 绘制填充区域
        /*if (isArea && size > 0) {
            mPath.lineTo(points.get(size - 1).x, height + xBottom);
            mPath.lineTo(points.get(0).x, height + xBottom);
            mPath.close();
            canvas.drawPath(mPath, areaPaint);
        }*/

        // 绘制线上圆点
        pointPaint.setColor(lineColor);
        for (PointF point : points) {
            canvas.drawCircle(point.x, point.y, circleSize / 2, pointPaint);
        }

    }

    /**
     * 公用绘制文本方法
     *
     * @param canvas    画布
     * @param text      文本
     * @param textSize  文本字体大小
     * @param textColor 文本字体颜色
     * @param top       文本 top 的坐标
     * @param left      文本 left 的坐标
     */
    private void drawText(Canvas canvas, String text, int textSize, int textColor, int left, int top) {
        targetRect = new Rect(left - tvWidth, top, left + tvWidth, top + tvHeight);
        commonPaint.setTextSize(textSize);
        commonPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(targetRect, commonPaint);
        commonPaint.setColor(textColor);
        fontMetrics = commonPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        commonPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, targetRect.centerX(), baseline, commonPaint);
    }

    /**
     * 设置图标参数,改变当前画面
     *
     * @param Xdatas
     * @param month
     * @param sleepValues
     * @param deepValues
     */
    public void setDatas(List<String> Xdatas, String month, List<String> sleepValues,
                         List<String> deepValues) {
        this.Xdatas = Xdatas;
        this.sleepValues = sleepValues;
        this.deepValues = deepValues;
        this.month = month;

        num_x = Xdatas.size() - 1;

        requestLayout();
    }
}
