package com.mooring.mh.views.ControlView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;

/**
 * 自定义可拖动改变图层控件
 * <p/>
 * Created by Will on 16/4/8.
 */
public class DragScaleView extends View implements View.OnTouchListener {

    private Paint colorPaint;//可改变高度View画笔
    private Bitmap drop;//拖动图标
    private Paint tickMarkPaint;//刻度线画笔
    private Paint scalePaint;//刻度圆圈
    private Paint maskPaint;//遮罩层
    private Shader mShader;  //渐变对象
    /*针对文本居中显示*/
    private Rect targetRect;
    private Paint commonPaint;
    private Paint.FontMetricsInt fontMetrics;

    private int dropW = 0;//拖动小球宽度
    private int dropH = 0;//拖动小球高度
    private int upperBound = 40;//温度上界
    private int lowerBound = 20;//温度下界
    private int bedTemperature = 30;//床的温度
    private int currTemperature = 35;//当前温度
    private int viewW = 0;//当前View的宽度
    private int viewH = 0;//当前View的高度
    private boolean isDropAble = true;//是否可用且可拖动
    private int roomY;//室内温度y坐标
    private int roomTemp = 35;//室内温度
    private String unit = "";//床温,拖动温度,Room温度显示单位
    private String currUnit = "℃";//当前系统设置的温度单位
    private int lastY;
    private int oriTop;//拖动变量
    private boolean dragDirection;//是否符合拖动位置

    private OnDropListener listener;//拖动监听

    private int dropTop = 0;  //拖动图标顶部
    private int lineBottom = 0; //刻度线的底部
    private int tempTop = 0; //温度上界顶部
    private int tempBottom = 0;  //温度下界底部
    private int tvHeight = 0; //文字框的高度
    private int tvWidth = 0;  //文字框的宽度
    private int scaleRadius = 0;  //刻度点的半径
    private int dropTvSize = 0;  //拖动文字的字体大小
    private int tempTvSize = 0;  //温度上下界的文字字体大小
    private int bedTvSize = 0;  //床温文字字体大小

    public DragScaleView(Context context) {
        this(context, null);
    }

    public DragScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragScaleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
        initScreen();
    }

    /**
     * 初始化
     */
    private void initScreen() {

        colorPaint = new Paint();
        colorPaint.setStyle(Paint.Style.FILL);

        drop = BitmapFactory.decodeResource(getResources(), R.drawable.ic_adjust_temp);
        dropW = drop.getWidth();
        dropH = drop.getHeight();

        tickMarkPaint = new Paint();
        tickMarkPaint.setColor(Color.WHITE);
        tickMarkPaint.setStyle(Paint.Style.STROKE);
        tickMarkPaint.setStrokeWidth(CommonUtils.dp2px(this.getContext(), 1));

        scalePaint = new Paint();
        scalePaint.setColor(Color.WHITE);
        scalePaint.setStyle(Paint.Style.FILL);
        scalePaint.setAntiAlias(true);

        commonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        maskPaint = new Paint();
        maskPaint.setStyle(Paint.Style.FILL);
        maskPaint.setColor(getResources().getColor(R.color.transparent_6));


        dropTop = CommonUtils.dp2px(this.getContext(), 40);
        lineBottom = CommonUtils.dp2px(this.getContext(), 150);
        tempTop = CommonUtils.dp2px(this.getContext(), 20);
        tempBottom = CommonUtils.dp2px(this.getContext(), 130);
        tvHeight = CommonUtils.dp2px(this.getContext(), 40);
        tvWidth = CommonUtils.dp2px(this.getContext(), 50);
        scaleRadius = CommonUtils.dp2px(this.getContext(), 4);
        dropTvSize = CommonUtils.sp2px(this.getContext(), 50);
        tempTvSize = CommonUtils.sp2px(this.getContext(), 22);
        bedTvSize = CommonUtils.sp2px(this.getContext(), 14);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        oriTop = 10;

        viewW = w;
        viewH = h;

        roomY = viewH / 2;

        mShader = new LinearGradient(0, 0, 0, viewH, new int[]{0XFF7d55ff, 0XFF6900ff}, null, Shader.TileMode.REPEAT);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制渐变背景
        colorPaint.setShader(mShader);
        canvas.drawRect(0, 0, viewW, viewH, colorPaint);

        //变动填充层
        colorPaint.setShader(null);
        colorPaint.setColor(getResources().getColor(R.color.colorMainBg));
        canvas.drawRect(0, 0, viewW, dropTop + dropH / 2 + oriTop, colorPaint);

        //绘制室内温度横线
        canvas.drawLine(0, roomY, viewW, roomY, tickMarkPaint);

        //绘制室内温度值
        drawText(canvas, "Room" + "     " + roomTemp + unit, bedTvSize, Color.WHITE, roomY - CommonUtils.
                dp2px(getContext(), 30), viewW / 4 - CommonUtils.dp2px(getContext(), 10));

        //温度text
        drawText(canvas, currTemperature + unit, dropTvSize, 0X7FFFFFFF, oriTop, viewW * 2 / 3 + dropW / 2);

        //拖动图标
        canvas.drawBitmap(drop, viewW * 2 / 3, dropTop + oriTop, null);

        //绘制遮罩层
        if (!isDropAble) {
            canvas.drawRect(0, 0, viewW, viewH, maskPaint);
        }

        //刻度直线
        canvas.drawLine(viewW / 4, dropTop + dropH / 2, viewW / 4, viewH - lineBottom, tickMarkPaint);

        int ss = (viewH - dropTop - lineBottom - dropH / 2) / 10;
        for (int i = 0; i <= 10; i++) {
            //刻度
            canvas.drawCircle(viewW / 4, dropTop + dropH / 2 + ss * i, scaleRadius, scalePaint);
        }

        //温度上界
        drawText(canvas, upperBound + currUnit, tempTvSize, Color.WHITE, tempTop, viewW / 4);

        //温度下界
        drawText(canvas, lowerBound + currUnit, tempTvSize, Color.WHITE, viewH - tempBottom, viewW / 4);

        //bed温度
        drawText(canvas, "Bed " + bedTemperature, bedTvSize, Color.WHITE, viewH - tempBottom, viewW * 3 / 4);

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
    private void drawText(Canvas canvas, String text, int textSize, int textColor, int top, int left) {
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                lastY = (int) event.getRawY();
                dragDirection = getDirection((int) event.getX(),
                        (int) event.getY());

                break;
            case MotionEvent.ACTION_MOVE:
                //可执行拖动的范围

                int dy = (int) event.getRawY() - lastY;

                if (dragDirection && isDropAble) {

                    oriTop += dy;

                    if (oriTop >= 0 && oriTop <= viewH - dropTop - lineBottom - dropH / 2) {
                        currTemperature = computeTemp();
                        invalidate();
                    }
                }

                lastY = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_UP:
                dragDirection = false;
                if (oriTop < 0) {
                    oriTop = 0;

                    currTemperature = computeTemp();
                    invalidate();
                }

                if (oriTop > viewH - dropTop - lineBottom - dropH / 2) {
                    oriTop = viewH - dropTop - lineBottom - dropH / 2;

                    currTemperature = computeTemp();
                    invalidate();
                }

                if (listener != null) {
                    listener.onDrop(currTemperature);
                }
                break;
        }
        return false;
    }

    /**
     * 设置当前的室内温度
     *
     * @param roomTemp 30
     */
    public void setRoomY(int roomTemp) {
        this.roomTemp = roomTemp;
        this.roomY = (upperBound - roomTemp) * (viewH - dropTop - lineBottom - dropH / 2) /
                (upperBound - lowerBound) + dropTop + dropH / 2;

        invalidate();
    }

    /**
     * 设置当前的室内温度
     *
     * @param roomTemp "30"
     */
    public void setRoomY(String roomTemp) {
        int temp = Integer.parseInt(roomTemp);
        setRoomY(temp);
    }

    /**
     * 计算当前温度
     *
     * @return 当前温度
     */
    private int computeTemp() {
        return upperBound - oriTop * (upperBound - lowerBound) / (viewH - dropTop - lineBottom - dropH / 2);
    }

    /**
     * 判定是否拖动小球的位置
     *
     * @param x getX()
     * @param y getY()
     * @return 是否在小球得x, y位置
     */
    protected boolean getDirection(int x, int y) {
        if (y > dropTop + oriTop && y < dropTop + oriTop + dropH) {
            if (x > viewW * 2 / 3 && x < viewW * 2 / 3 + dropW) {
                return true;
            }
        }
        return false;
    }


    /**
     * 设置温度的上下界
     *
     * @param upperBound
     * @param lowerBound
     */
    public void setUpperAndLowerBound(int upperBound, int lowerBound) {
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;

        invalidate();
    }

    /**
     * 设置bed的温度
     *
     * @param bedTemperature "30"
     */
    public void setBedTemperature(String bedTemperature) {
        int temp = Integer.parseInt(bedTemperature);
        this.bedTemperature = temp;

        invalidate();
    }

    /**
     * 设置bed的温度
     *
     * @param bedTemperature 30
     */
    public void setBedTemperature(int bedTemperature) {
        this.bedTemperature = bedTemperature;

        invalidate();
    }

    /**
     * 设置当前的温度
     *
     * @param currTemperature "30"
     */
    public void setCurrTemperature(String currTemperature) {
        int temp = Integer.parseInt(currTemperature);
        setCurrTemperature(temp);
    }

    /**
     * 设置当前的温度
     *
     * @param currTemp 30
     */
    public void setCurrTemperature(int currTemp) {
        this.currTemperature = currTemp;
        oriTop = (upperBound - currTemp) * (viewH - dropTop - lineBottom - dropH / 2) /
                (upperBound - lowerBound);

        invalidate();
    }

    /**
     * 设置是否可拖动
     *
     * @param isDropAble
     */
    public void setIsDropAble(boolean isDropAble) {
        this.isDropAble = isDropAble;

        invalidate();
    }

    public interface OnDropListener {
        void onDrop(int currTemp);
    }

    /**
     * 设置拖动监听,松开手执行
     *
     * @param listener
     */
    public void setOnDropListener(OnDropListener listener) {
        this.listener = listener;
    }

    /**
     * 设置  床温,拖动温度,Room温度显示单位
     *
     * @param unit ℃
     */
    public void setUnit(String unit) {
        this.unit = unit;
        invalidate();
    }

    /**
     * 设置当前系统设定的纬度单位,
     * 一旦改变对应的上下架温度值也跟着改变
     *
     * @param unit ℃  ||  ℉
     * @see MConstants
     */
    public void setCurrUnit(int unit) {
        if (unit == MConstants.DEGREES_C) {
            this.currUnit = getResources().getString(R.string.unit_celsius);
            this.upperBound = 40;
            this.lowerBound = 20;
            roomTemp = (int) CommonUtils.F2C(roomTemp);
            bedTemperature = (int) CommonUtils.F2C(bedTemperature);
            currTemperature = (int) CommonUtils.F2C(currTemperature);
        }
        if (unit == MConstants.DEGREES_F) {
            this.currUnit = getResources().getString(R.string.unit_fahrenheit);
            this.upperBound = 104;
            this.lowerBound = 68;
            roomTemp = (int) CommonUtils.C2F(roomTemp);
            bedTemperature = (int) CommonUtils.C2F(bedTemperature);
            currTemperature = (int) CommonUtils.C2F(currTemperature);
        }
        invalidate();
    }
}
