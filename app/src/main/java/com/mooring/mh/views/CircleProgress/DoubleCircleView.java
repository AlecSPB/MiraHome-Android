package com.mooring.mh.views.CircleProgress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.model.SleepTimeInfo;
import com.mooring.mh.utils.MUtils;

import java.util.List;

/**
 * 自定义双环,不同颜色进度展示View
 * <p/>
 * Created by Will on 16/4/26.
 */
public class DoubleCircleView extends View {

    private Paint ringPaint;//圆环画笔
    private Paint linePaint;//起始刻度画笔
    private Bitmap bgBitmap;//背景

    private int viewW;
    private int viewH;

    private List<SleepTimeInfo> outDatas;//外环数据
    private List<SleepTimeInfo> innDatas;//内环数据

    private int keStart;//刻度起始
    private int keStop;//刻度结束
    private float startAngle = 0.0f;//环形起始角度
    private RectF outerRect = new RectF();
    private float delta = MUtils.dp2px(getContext(), 45);//外环
    private float deltaInn = MUtils.dp2px(getContext(), 70);//内环

    public DoubleCircleView(Context context) {
        this(context, null);
    }

    public DoubleCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_report_score_clock);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(MUtils.dp2px(getContext(), 2));
        linePaint.setColor(Color.WHITE);

        ringPaint = new Paint();
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setAntiAlias(true);
        ringPaint.setStrokeWidth(MUtils.dp2px(getContext(), 6));

        keStart = MUtils.dp2px(getContext(), 35);
        keStop = MUtils.dp2px(getContext(), 55);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            viewH = h;
            viewW = w;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 绘制背景
         */
        Matrix matrix = new Matrix();
        matrix.postScale((float) viewW / bgBitmap.getWidth(), (float) viewH / bgBitmap.getHeight());
        canvas.drawBitmap(bgBitmap, matrix, null);

        /**
         * 绘制大环
         */
        if (outDatas != null && outDatas.size() > 0) {
            outerRect.set(delta, delta, getWidth() - delta, getHeight() - delta);

            int time = outDatas.get(0).getTime();
            startAngle = time % 12 * 30;
            if (time != 0) {
                canvas.rotate(startAngle, viewW / 2, viewH / 2);
                canvas.drawLine(viewW / 2, keStart, viewW / 2, keStop, linePaint);
                canvas.rotate(-startAngle, viewW / 2, viewH / 2);
            } else {
                canvas.drawLine(viewW / 2, keStart, viewW / 2, keStop, linePaint);
            }
            canvas.rotate(-90, viewW / 2, viewH / 2);
            for (int i = 1; i < outDatas.size(); i++) {
                ringPaint.setColor(getResources().getColor(outDatas.get(i).getColor()));
                canvas.drawArc(outerRect, startAngle, outDatas.get(i).getTime() / 2, false, ringPaint);
                startAngle += outDatas.get(i).getTime() / 2;
            }
        }

        /**
         * 绘制小环
         */
        if (innDatas != null && innDatas.size() > 0) {
            outerRect.set(deltaInn, deltaInn, getWidth() - deltaInn, getHeight() - deltaInn);
            startAngle = outDatas.get(0).getTime() % 12 * 30;//起始角度
            for (int i = 0; i < innDatas.size(); i++) {
                ringPaint.setColor(getResources().getColor(innDatas.get(i).getColor()));
                canvas.drawArc(outerRect, startAngle, innDatas.get(i).getTime() / 2, false, ringPaint);
                startAngle += innDatas.get(i).getTime() / 2;
            }
        }

    }

    /**
     * 设置数据
     *
     * @param outDatas
     * @param innDatas
     */

    public void setDatass(List<SleepTimeInfo> outDatas, List<SleepTimeInfo> innDatas) {
        this.outDatas = outDatas;
        this.innDatas = innDatas;
        invalidate();
    }
}
