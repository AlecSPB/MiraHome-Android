package com.mooring.mh.views.ChartView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.utils.MUtils;

import java.util.List;

/**
 * 自定义Y轴,配合MyChartView和XXChartView使用
 *
 * @see MyChartView
 * @see XXChartView
 * <p/>
 * Created by Will on 16/4/29.
 */
public class YAxisView extends View {

    private Paint linePaint;//刻度线画笔
    private Paint textPaint;//y轴单位画笔
    private int viewW;
    private int viewH;
    private int xBottom;//bottom
    private int ySpace;//Y轴刻度之间距离
    private int lineSize;//短刻度线的长度
    private int yRight;//right
    private int yLeft;
    private List<String> yDatas;//y轴数据
    private int num_y;//y轴单位数量
    private int y_location;//0:右边  1:左边
    private int commColor;
    private int tvHeight;
    private int tvWidth;
    private Rect targetRect;
    private Paint commonPaint;
    private Paint.FontMetricsInt fontMetrics;


    public YAxisView(Context context) {
        this(context, null);
    }

    public YAxisView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YAxisView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyChartView, defStyleAttr, 0);
        num_y = a.getInteger(R.styleable.MyChartView_chart_y, 0);
        y_location = a.getInteger(R.styleable.MyChartView_y_location, 0);
        a.recycle();


        initView();
    }

    private void initView() {
        xBottom = MUtils.dp2px(getContext(), 10);
        ySpace = MUtils.dp2px(getContext(), 20);
        yRight = MUtils.dp2px(getContext(), 50);
        yLeft = MUtils.dp2px(getContext(), 30);
        lineSize = MUtils.dp2px(getContext(), 10);
        tvHeight = MUtils.dp2px(this.getContext(), 10);
        tvWidth = MUtils.dp2px(this.getContext(), 10);

        commColor = getResources().getColor(R.color.colorWhite50);


        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(MUtils.dp2px(getContext(), 1));
        linePaint.setColor(commColor);

        textPaint = new TextPaint();
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(commColor);

        commonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, num_y * ySpace + xBottom * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (yDatas == null || yDatas.size() <= 0) {
            return;
        }

        for (int i = 0; i < yDatas.size(); i++) {
            if (y_location == 0) {
                canvas.drawLine(viewW - yRight, viewH - xBottom - ySpace * (i + 1), viewW - yRight +
                        lineSize, viewH - xBottom - ySpace * (i + 1), linePaint);
                canvas.drawText(yDatas.get(i), viewW - yRight + lineSize * 2, viewH - xBottom -
                        ySpace * (i + 1) + 10, textPaint);
            } else {
                if (i % 2 == 0)
                    drawText(canvas, yDatas.get(i), yLeft, viewH - xBottom - ySpace * i);

            }
        }
    }

    /**
     * 绘制居中显示的坐标单位
     *
     * @param canvas
     * @param text
     * @param left
     * @param top
     */
    private void drawText(Canvas canvas, String text, int left, int top) {
        targetRect = new Rect(left, top - tvHeight, left + tvWidth, top + tvHeight);
        commonPaint.setTextSize(MUtils.sp2px(getContext(), 10));
        commonPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(targetRect, commonPaint);
        commonPaint.setColor(commColor);
        fontMetrics = commonPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        commonPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, targetRect.centerX(), baseline, commonPaint);
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
     * 设置参数
     *
     * @param ydatas
     */
    public void setYdatas(List<String> ydatas) {
        yDatas = ydatas;
        invalidate();
    }
}
