package com.mooring.mh.views.WeatherView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.TextView;

/**
 * Created by Will on 16/6/13.
 */
public class CloudView extends SurfaceView {

    TextView tvTV;

    public CloudView(Context context) {
        this(context, null);
    }

    public CloudView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloudView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
