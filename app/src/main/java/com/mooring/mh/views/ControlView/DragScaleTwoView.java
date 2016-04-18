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

/**
 * 自定义双边可拖动改变图层控件
 * <p/>
 * Created by Will on 16/4/11.
 */
public class DragScaleTwoView extends View implements View.OnTouchListener {

    private int lastY;
    private int oriLeft;//拖动变量
    private int oriRight;//拖动变量

    private boolean dragLeft = false;//左侧是否符合拖动位置
    private boolean dragRight = false;//右侧是否符合拖动位置

    private Paint colorPaint;//上层颜色画笔
    private Bitmap drop;//拖动图标
    private Paint tickMarkPaint;//刻度线画笔
    private Paint scalePaint;//刻度圆圈
    private Paint maskPaint;//遮罩层

    private int dropW = 0;//拖动小球宽度
    private int dropH = 0;//拖动小球高度

    private int upperBound = 104;//温度上界
    private int lowerBound = 68;//温度下界
    private String bedLeftTemp = "83℉";//左边床的温度
    private String bedRightTemp = "60℉";//右边床的温度
    private String currLeftTemp = "86℉";//当前温度
    private String currRightTemp = "76℉";//当前温度

    /*针对文本居中显示*/
    private Rect targetRect;
    private Paint commonPaint;
    private Paint.FontMetricsInt fontMetrics;

    private int viewW = 0;//当前View的宽度
    private int viewH = 0;//当前View的高度
    private boolean isLeftDropAble = true;//是否可用且可拖动
    private boolean isRightDropAble = true;//是否可用且可拖动

    private OnDropListener listener;//拖动监听

    private int roomY;//室内温度y坐标

    private int roomTemp = 80;//室内温度

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
    private Shader mShader;  //渐变对象


    public DragScaleTwoView(Context context) {
        this(context, null);
    }

    public DragScaleTwoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragScaleTwoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        initScreen();
    }


    /**
     * 初始化
     */
    private void initScreen() {

        colorPaint = new Paint();
        colorPaint.setStyle(Paint.Style.FILL);

        drop = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_adjust_temp);
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

        viewW = w;
        viewH = h;

        oriLeft = 10;
        oriRight = 10;

        roomY = viewH / 2;

        mShader = new LinearGradient(0, 0, 0, viewH, new int[]{0XFF7d55ff, 0XFF6900ff}, null, Shader.TileMode.REPEAT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制渐变背景
        colorPaint.setShader(mShader);
        canvas.drawRect(0, 0, viewW, viewH, colorPaint);

        //重新 设置colorPaint
        colorPaint.setShader(null);
        colorPaint.setColor(getResources().getColor(R.color.colorMainBg));
         /*-------- 绘制左侧 -------*/

        //左侧颜色变动填充层
        canvas.drawRect(0, 0, viewW / 2, dropTop + dropH / 2 + oriLeft, colorPaint);

        //右侧颜色变动填充层
        canvas.drawRect(viewW / 2, 0, viewW, dropTop + dropH / 2 + oriRight, colorPaint);

        //绘制室内温度横线
        canvas.drawLine(0, roomY, viewW, roomY, tickMarkPaint);

        //绘制室内温度值
        drawText(canvas, "Room" + "     " + roomTemp + "℉", bedTvSize, Color.WHITE, roomY - CommonUtils.
                dp2px(getContext(), 30), viewW / 2 - CommonUtils.dp2px(getContext(), 5));

        //温度text
        drawText(canvas, currLeftTemp, dropTvSize, (0X7FFFFFFF), oriLeft, viewW / 4);

        //拖动图标
        canvas.drawBitmap(drop, viewW / 4 - dropW / 2, dropTop + oriLeft, null);

        //绘制遮罩层
        if (!isLeftDropAble) {
            canvas.drawRect(0, 0, viewW / 2, viewH, maskPaint);
        }

        //bed温度
        drawText(canvas, "Bed " + bedLeftTemp, bedTvSize, Color.WHITE, viewH - tempBottom, viewW / 4);

        /*-------- 绘制右侧 -------*/

        //温度text
        drawText(canvas, currRightTemp, dropTvSize, (0X7FFFFFFF), oriRight, viewW * 3 / 4);

        //拖动图标
        canvas.drawBitmap(drop, viewW * 3 / 4 - dropW / 2, dropTop + oriRight, null);

        //绘制遮罩层
        if (!isRightDropAble) {
            canvas.drawRect(viewW / 2, 0, viewW, viewH, maskPaint);
        }

        //bed温度
        drawText(canvas, "Bed " + bedRightTemp, bedTvSize, Color.WHITE, viewH - lineBottom, viewW * 3 / 4);

         /*-------- 绘制公共部分 -------*/
        //刻度直线
        canvas.drawLine(viewW / 2, dropTop + dropH / 2, viewW / 2, viewH - lineBottom, tickMarkPaint);

        int ss = (viewH - dropTop - lineBottom - dropH / 2) / 10;
        for (int i = 0; i <= 10; i++) {
            //刻度
            canvas.drawCircle(viewW / 2, dropTop + dropH / 2 + ss * i, scaleRadius, scalePaint);
        }

        //温度上界
        drawText(canvas, upperBound + "℉", tempTvSize, Color.WHITE, tempTop, viewW / 2);

        //温度下界
        drawText(canvas, lowerBound + "℉", tempTvSize, Color.WHITE, viewH - tempBottom, viewW / 2);

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
                getDirection((int) event.getX(),
                        (int) event.getY());

                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) event.getRawY() - lastY;

                if (dragLeft && isLeftDropAble) {

                    oriLeft += dy;
                    if (oriLeft >= 0 && oriLeft <= viewH - dropTop - lineBottom - dropH / 2) {
                        currLeftTemp = computeTemp(oriLeft) + "℉";
                        invalidate();
//                        listener.onDrop(currLeftTemp, 0);
                    }
                }

                if (dragRight && isRightDropAble) {
                    oriRight += dy;
                    if (oriRight >= 0 && oriRight <= viewH - dropTop - lineBottom - dropH / 2) {
                        currRightTemp = computeTemp(oriRight) + "℉";
                        invalidate();
//                        listener.onDrop(currRightTemp, 1);
                    }
                }

                lastY = (int) event.getRawY();

                break;

            case MotionEvent.ACTION_UP:
                /*---Left---*/
                dragLeft = false;
                if (oriLeft < 0) {
                    oriLeft = 0;
                    currLeftTemp = computeTemp(oriLeft) + "℉";
                    invalidate();
                }
                if (oriLeft > viewH - dropTop - lineBottom - dropH / 2) {
                    oriLeft = viewH - dropTop - lineBottom - dropH / 2;
                    currLeftTemp = computeTemp(oriLeft) + "℉";
                    invalidate();
                }

                if (listener != null) {
                    listener.onDrop(currLeftTemp, 0);
                }
                /*---Right---*/
                dragRight = false;
                if (oriRight < 0) {
                    oriRight = 0;
                    currRightTemp = computeTemp(oriRight) + "℉";
                    invalidate();
                }
                if (oriRight > viewH - dropTop - lineBottom - dropH / 2) {
                    oriRight = viewH - dropTop - lineBottom - dropH / 2;
                    currRightTemp = computeTemp(oriRight) + "℉";
                    invalidate();
                }

                if (listener != null) {
                    listener.onDrop(currRightTemp, 1);
                }
                break;
        }

        return false;
    }

    /**
     * 设置当前的室内温度
     *
     * @param roomTemp
     */
    public void setRoomY(int roomTemp) {

        this.roomTemp = roomTemp;

        this.roomY = (upperBound - roomTemp) * (viewH - dropTop - lineBottom - dropH / 2) /
                (upperBound - lowerBound) + dropTop + dropH / 2;

        invalidate();

    }

    /**
     * 计算当前温度
     *
     * @return 当前温度
     */
    private int computeTemp(int currDrop) {
        return upperBound - currDrop * (upperBound - lowerBound) / (viewH - dropTop - lineBottom - dropH / 2);
    }

    /**
     * 判定拖动小球的位置
     *
     * @param x getX()
     * @param y getY()
     * @return 是否在小球得x, y位置
     */
    protected void getDirection(int x, int y) {

        if (y > dropTop + oriLeft && y < dropTop + oriLeft + dropH) {
            if (x > viewW / 4 - dropW / 2 && x < viewW / 4 + dropW / 2) {
                dragLeft = true;
            }
        }

        if (y > dropTop + oriRight && y < dropTop + oriRight + dropH) {
            if (x > viewW * 3 / 4 - dropW / 2 && x < viewW * 3 / 4 + dropW / 2) {
                dragRight = true;
            }
        }
    }


    public interface OnDropListener {
        //location 0:left  1:right
        void onDrop(String currTemp, int location);
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
     * 设置左边bed温度
     *
     * @param bedLeftTemp
     */
    public void setBedLeftTemp(String bedLeftTemp) {
        this.bedLeftTemp = bedLeftTemp;
        invalidate();
    }

    /**
     * 设置右边bed温度
     *
     * @param bedRightTemp
     */
    public void setBedRightTemp(String bedRightTemp) {
        this.bedRightTemp = bedRightTemp;
        invalidate();
    }

    /**
     * 设置当前左边的温度
     *
     * @param currLeftTemp 70℉
     */
    public void setCurrLeftTemp(String currLeftTemp) {
        this.currLeftTemp = currLeftTemp;
        int temp = Integer.parseInt(currLeftTemp.substring(0, currLeftTemp.length() - 1));
        oriLeft = (upperBound - temp) * (viewH - dropTop - lineBottom - dropH / 2) /
                (upperBound - lowerBound);

        invalidate();
    }

    /**
     * 设置当前左边的温度
     *
     * @param currLeftTemp 70
     */
    public void setCurrLeftTemp(int currLeftTemp) {
        this.currLeftTemp = currLeftTemp + "℉";
        oriLeft = (upperBound - currLeftTemp) * (viewH - dropTop - lineBottom - dropH / 2) /
                (upperBound - lowerBound);
        invalidate();
    }

    /**
     * 设置当前右边温度
     *
     * @param currRightTemp 70℉
     */
    public void setCurrRightTemp(String currRightTemp) {
        this.currRightTemp = currRightTemp;
        int temp = Integer.parseInt(currRightTemp.substring(0, currRightTemp.length() - 1));
        oriRight = (upperBound - temp) * (viewH - dropTop - lineBottom - dropH / 2) /
                (upperBound - lowerBound);
        invalidate();
    }

    /**
     * 设置当前右边温度
     *
     * @param currRightTemp 70
     */
    public void setCurrRightTemp(int currRightTemp) {
        this.currRightTemp = currRightTemp + "℉";
        oriRight = (upperBound - currRightTemp) * (viewH - dropTop - lineBottom - dropH / 2) /
                (upperBound - lowerBound);
        invalidate();
    }

    /**
     * 设置左边是否可拖动
     *
     * @param isLeftDropAble
     */
    public void setIsLeftDropAble(boolean isLeftDropAble) {
        this.isLeftDropAble = isLeftDropAble;
        invalidate();
    }

    /**
     * 设置右边是否可拖动
     *
     * @param isRightDropAble
     */
    public void setIsRightDropAble(boolean isRightDropAble) {
        this.isRightDropAble = isRightDropAble;
        invalidate();
    }

}
