package com.test.testdemo.waitingdots;

import android.animation.TypeEvaluator;

/**
 * Created by Will on 16/6/23.
 */
public class SinTypeEvaluator implements TypeEvaluator<Number> {
    @Override
    public Number evaluate(float fraction, Number from, Number to) {
        return Math.max(0, Math.sin(fraction * Math.PI * 2)) * (to.floatValue() - from.floatValue());
    }
}
