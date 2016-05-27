package com.mooring.mh.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import com.mooring.mh.R;

/**
 * 自定义ToggleButton,指定关闭之前进行判断
 * <p/>
 * Created by Will on 16/5/9.
 */
public class CustomToggle extends View implements View.OnClickListener {
    private Bitmap toggle_on;//打开
    private Bitmap toggle_off;//关闭
    private Bitmap bgBitmap;//默认
    private int viewW;
    private int viewH;
    private boolean checked = false;

    private OnCheckedChangeListener listener;

    public CustomToggle(Context context) {
        this(context, null);
    }

    public CustomToggle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setOnClickListener(this);
        toggle_on = BitmapFactory.decodeResource(getResources(), R.drawable.btn_toggle_on);
        toggle_off = BitmapFactory.decodeResource(getResources(), R.drawable.btn_toggle_off);
        bgBitmap = toggle_off;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        bgBitmap = checked ? toggle_on : toggle_off;
        Matrix matrix = new Matrix();
        float scale = (float) viewW / bgBitmap.getWidth();
        matrix.postScale(scale, scale);
        matrix.postTranslate(0, (viewH - scale * bgBitmap.getHeight()) / 2);
        canvas.drawBitmap(bgBitmap, matrix, null);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = w;
        viewH = h;
    }

    /**
     * 计算当前view的宽和高
     *
     * @param length
     * @param isWidth
     * @return
     */
    private int getMeasuredLength(int length, boolean isWidth) {

        int specMode = MeasureSpec.getMode(length);
        int specSize = MeasureSpec.getSize(length);
        int size;
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (specMode == MeasureSpec.EXACTLY) {
            size = specSize;
        } else {
            size = isWidth ? padding + bgBitmap.getWidth() : padding + bgBitmap.getHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                size = Math.min(size, specSize);
            }
        }

        return size;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredLength(widthMeasureSpec, true),
                getMeasuredLength(heightMeasureSpec, false));
    }

    /**
     * 添加监听
     *
     * @param listener
     */
    public void setOnCheckedChange(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 设定状态
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
        invalidate();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onCheckedChanged(this, !checked);
        }
    }

    public interface OnCheckedChangeListener {
        /**
         * 当状态改变时调用
         *
         * @param v         view
         * @param isChecked 新状态
         */
        void onCheckedChanged(View v, boolean isChecked);
    }
}
