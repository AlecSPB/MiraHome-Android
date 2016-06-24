package com.mooring.mh.views.LoadingDots;

import android.animation.TypeEvaluator;

/**
 * 计算sin值
 * <p>
 * Created by Will on 16/6/23.
 */
public class SinTypeEvaluator implements TypeEvaluator<Number> {
    @Override
    public Number evaluate(float fraction, Number startValue, Number endValue) {
        double value = Math.max(0, Math.sin(fraction * Math.PI * 2));
        return value * (endValue.floatValue() - startValue.floatValue());
    }
}
