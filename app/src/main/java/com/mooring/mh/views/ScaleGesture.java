package com.mooring.mh.views;

import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * Created by Will on 16/3/30.
 */
public class ScaleGesture implements ScaleGestureDetector.OnScaleGestureListener {

    private float beforeFactor;
    private float mPivotX;
    private float mPivotY;
    private View mVSouce;
    private boolean isFillAfter;

    public void setSourceView(View destinyView) {
        mVSouce = destinyView;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (checkIsNull()) {
            return false;
        }
        final float factor = detector.getScaleFactor();
        Animation animation = new ScaleAnimation(beforeFactor, factor,
                beforeFactor, factor, mPivotX, mPivotY);
        animation.setFillAfter(true);
        mVSouce.startAnimation(animation);
        beforeFactor = factor;
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        if (checkIsNull()) {
            return false;
        }
        beforeFactor = 1f;
        mPivotX = detector.getFocusX() - mVSouce.getLeft();
        mPivotY = mVSouce.getTop() + (mVSouce.getHeight() >> 1);
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        if (checkIsNull()) {
            return;
        }
        final float factor = detector.getScaleFactor();
        final int nWidth = (int) (mVSouce.getWidth() * factor);
        final int nHeight = (int) mVSouce.getHeight();
        final int nLeft = (int) (mVSouce.getLeft() - ((nWidth - mVSouce
                .getWidth()) * (mPivotX / mVSouce.getWidth())));
        final int nTop = (int) mVSouce.getTop();
        if (isFillAfter) {
            mVSouce.layout(nLeft, nTop, nLeft + nWidth, nTop + nHeight);
        }
    }

    public boolean checkIsNull() {
        return mVSouce == null ? true : false;
    }

    public void setFillAfter(boolean isFill) {
        isFillAfter = isFill;
    }
}
