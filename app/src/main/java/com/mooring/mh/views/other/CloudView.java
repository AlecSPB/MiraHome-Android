package com.mooring.mh.views.other;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Will on 16/3/24.
 */
public class CloudView extends ImageView {

    public CloudView(Context context) {
        super(context);
    }

    public CloudView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CloudView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
