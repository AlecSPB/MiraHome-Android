package com.mooring.mh.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Will on 16/3/25.
 */
public class CommonUtils {

    public static float dp2px(Resources r, float dp) {
        DisplayMetrics metrics = r.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
