package com.test.testdemo.waitingdots;

import android.animation.ValueAnimator;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Will on 16/6/23.
 */
public class InvalidateViewOnUpdate implements ValueAnimator.AnimatorUpdateListener {
    private final WeakReference<View> viewRef;

    public InvalidateViewOnUpdate(View view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        final View view = viewRef.get();

        if (view == null) {
            return;
        }

        view.invalidate();
    }
}