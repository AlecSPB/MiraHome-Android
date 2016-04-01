package com.mooring.mh.views.WheelPicker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 抽象表层覆盖装饰层
 * <p/>
 * Created by Will on 16/3/31.
 */
public abstract class AbstractWheelDecor {

    /**
     * 绘制覆盖层
     *
     * @param canvas
     * @param rectLast
     * @param rectNext
     * @param paint
     */
    public abstract void drawDecor(Canvas canvas, Rect rectLast, Rect rectNext, Paint paint);
}