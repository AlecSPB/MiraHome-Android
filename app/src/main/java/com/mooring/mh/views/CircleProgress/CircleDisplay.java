package com.mooring.mh.views.CircleProgress;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.views.other.Utils;

import java.text.DecimalFormat;

/**
 * 自定义圆形进度条
 * <p/>
 * Created by Will on 16/3/24.
 */
public class CircleDisplay extends View {

    /**
     * the unit that is represented by the circle-display
     */
    private String mUnit = "%";
    /**
     * startangle of the view
     */
    private float mStartAngle = 90f;
    /**
     * The width of the ring
     */
    private float mStrokeWidth = 10f;
    /**
     * angle that represents the displayed value
     */
    private float mAngle = 0f;
    /**
     * current state of the animation
     */
    private float mPhase = 0f;
    /**
     * the displayed value
     */
    private float mValue = 0f;
    /**
     * the maximum displayable value, depends on the set value
     */
    private float mMaxValue = 0f;
    /**
     * if enabled, the center text is drawn
     */
    private boolean mDrawText = true;
    /**
     * the decimalformat responsible for formatting the values in the view
     */
    private DecimalFormat mFormatValue = new DecimalFormat("###,###,###,##0.0");
    /**
     * array that contains values for the custom-text
     */
    private Paint mInnerCirclePaint;
    /**
     * draw the text
     */
    private Paint mTextPaint;
    /**
     * the paint of progress
     */
    private Paint finishedPaint;
    /**
     * the paint of blank
     */
    private Paint unfinishedPaint;
    /**
     * the RectF of progress
     */
    private RectF finishedOuterRect = new RectF();
    /**
     * the RectF of blank
     */
    private RectF unfinishedOuterRect = new RectF();
    /**
     * object animator for doing the drawing animations
     */
    private ObjectAnimator mDrawAnimator;

    private int finishedStrokeColor = Color.rgb(125, 85, 255);
    private int unfinishedStrokeColor = Color.TRANSPARENT;
    private int textColor = Color.WHITE;

    public CircleDisplay(Context context) {
        this(context, null);
    }

    public CircleDisplay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleDisplay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AutoProgress, defStyleAttr, 0);
        textColor = attributes.getColor(R.styleable.AutoProgress_pre_textColor, Color.WHITE);
        finishedStrokeColor = attributes.getColor(R.styleable.AutoProgress_pre_finishColor, Color.rgb(125, 85, 255));
        unfinishedStrokeColor = attributes.getColor(R.styleable.AutoProgress_pre_unFinishColor, Color.TRANSPARENT);

        attributes.recycle();
        initPaint();
    }


    private void initPaint() {

        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setColor(Color.TRANSPARENT);
        mInnerCirclePaint.setAntiAlias(true);

        finishedPaint = new Paint();
        finishedPaint.setColor(finishedStrokeColor);
        finishedPaint.setStyle(Paint.Style.STROKE);
        finishedPaint.setAntiAlias(true);
        finishedPaint.setStrokeWidth(mStrokeWidth);

        unfinishedPaint = new Paint();
        unfinishedPaint.setColor(unfinishedStrokeColor);
        unfinishedPaint.setStyle(Paint.Style.STROKE);
        unfinishedPaint.setAntiAlias(true);
        unfinishedPaint.setStrokeWidth(mStrokeWidth);

        mTextPaint = new TextPaint();
        mTextPaint.setStyle(Style.STROKE);
        mTextPaint.setTextAlign(Align.CENTER);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(Utils.sp2px(getResources(), 10f));

        mDrawAnimator = ObjectAnimator.ofFloat(this, "phase", mPhase, 1.0f).setDuration(3000);
        mDrawAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float delta = mStrokeWidth;
        finishedOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);
        unfinishedOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);

        float innerCircleRadius = (getWidth() - mStrokeWidth) / 2f;
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, mInnerCirclePaint);
        canvas.drawArc(finishedOuterRect, mStartAngle, getProgressAngle(), false, finishedPaint);
        canvas.drawArc(unfinishedOuterRect, mStartAngle + getProgressAngle(), 360 - getProgressAngle(), false, unfinishedPaint);


        if (mDrawText) {
            drawText(canvas);
        }

        if (mListener != null) {
            mListener.onValueUpdate(getValue() * mPhase, mMaxValue);
        }
    }

    /**
     * Get the current progress
     *
     * @return
     */
    private float getProgressAngle() {
        Log.e("getProgressAngle", "getProgressAngle  " + mAngle + "    " + mPhase + "   " + mDrawAnimator.getAnimatedValue());
        return mAngle * mPhase;
    }

    /**
     * draws the text in the center of the view
     *
     * @param c
     */
    private void drawText(Canvas c) {
        c.drawText(mFormatValue.format(mValue * mPhase) + " " + mUnit, getWidth() / 2,
                getHeight() / 2 + mTextPaint.descent(), mTextPaint);
    }

    /**
     * shows the given value in the circle view
     *
     * @param toShow
     * @param total
     * @param animated
     */
    public void showValue(float toShow, float total, boolean animated) {

        mAngle = toShow / total * 360f;
        mValue = toShow;
        mMaxValue = total;

        if (animated)
            startAnim();
        else {
            mPhase = 1f;
            invalidate();
        }
    }

    /**
     * Sets the unit that is displayed next to the value in the center of the
     * view. Default "%". Could be "€" or "$" or left blank or whatever it is
     * you display.
     *
     * @param unit
     */
    public void setUnit(String unit) {
        mUnit = unit;
    }

    /**
     * Returns the currently displayed value from the view. Depending on the
     * used method to show the value, this value can be percent or actual value.
     *
     * @return
     */
    public float getValue() {
        return mValue;
    }

    public void startAnim() {
        mPhase = 0f;
        mDrawAnimator.start();
    }

    /**
     * set the duration of the drawing animation in milliseconds
     *
     * @param durationmillis
     */
    public void setAnimDuration(int durationmillis) {
        mDrawAnimator.setDuration(durationmillis);
    }

    /**
     * returns the current animation status of the view
     *
     * @return
     */
    public float getPhase() {
        return mPhase;
    }

    /**
     * DONT USE THIS METHOD
     *
     * @param phase
     */
    public void setPhase(float phase) {
        mPhase = phase;
        invalidate();
    }

    /**
     * set the drawing of the center text to be enabled or not
     *
     * @param enabled
     */
    public void setDrawText(boolean enabled) {
        mDrawText = enabled;
    }

    /**
     * returns true if drawing the text in the center is enabled
     *
     * @return
     */
    public boolean isDrawTextEnabled() {
        return mDrawText;
    }


    /**
     * set the size of the center text in dp
     *
     * @param size
     */
    public void setTextSize(float size) {
        mTextPaint.setTextSize(CommonUtils.dp2px(this.getContext(), size));
    }

    /**
     * sets the number of digits used to format values
     *
     * @param digits
     */
    public void setFormatDigits(int digits) {

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        mFormatValue = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    /**
     * set a selection listener for the circle-display that is called whenever a
     * value is selected onTouch()
     *
     * @param l
     */
    public void setSelectionListener(SelectionListener l) {
        mListener = l;
    }

    /**
     * listener called when a value has been selected on touch
     */
    private SelectionListener mListener;

    /**
     * listener for callbacks when selecting values ontouch
     *
     * @author Philipp Jahoda
     */
    public interface SelectionListener {

        /**
         * called everytime the user moves the finger on the circle-display
         *
         * @param value
         * @param maxValue
         */
        public void onValueUpdate(float value, float maxValue);
    }

}