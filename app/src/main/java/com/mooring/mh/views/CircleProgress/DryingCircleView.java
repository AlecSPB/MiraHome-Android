package com.mooring.mh.views.CircleProgress;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.mooring.mh.R;

/**
 * 自定义环状线程充满进度条
 * <p/>
 * Created by Will on 16/4/12.
 */
public class DryingCircleView extends View {

    /**
     * 背景画笔
     */
    private Paint bgPaint;
    /**
     * 刻度线画笔
     */
    private Paint ticksPaint;
    /**
     * 圆环画笔
     */
    private Paint ringPaint;
    /**
     * 进度条画笔
     */
    private Paint ratePaint;
    /**
     * view的宽度
     */
    private int viewW;
    /**
     * view的高度
     */
    private int viewH;
    /**
     * 底部圆环的外切矩形
     */
    private RectF ringRect = new RectF();
    /**
     * 进度环的外切矩形
     */
    private RectF rateRect = new RectF();
    /**
     * 中间显示图标的bitmap
     */
    private Bitmap bitmap;
    /**
     * 默认圆环的宽度
     */
    private float delta = 20f;
    /**
     * 当前动画的初始值
     */
    private float mPhase = 0f;
    /**
     * object animator for doing the drawing animations
     */
    private ObjectAnimator mDrawAnimator;
    /**
     * 充满加载过程回调
     */
    private LoadingListener listener;

    public DryingCircleView(Context context) {
        this(context, null);
    }

    public DryingCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DryingCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {

        bgPaint = new Paint();
        bgPaint.setColor(getResources().getColor(R.color.colorMainBg));
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);

        ticksPaint = new Paint();
        ticksPaint.setColor(getResources().getColor(R.color.colorWhite50));
        ticksPaint.setStyle(Paint.Style.STROKE);
        ticksPaint.setStrokeWidth(3f);
        ticksPaint.setAntiAlias(true);

        ringPaint = new Paint();
        ringPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(2 * delta);
        ringPaint.setAntiAlias(true);

        ratePaint = new Paint();
        ratePaint.setColor(getResources().getColor(R.color.colorOrange));
        ratePaint.setStyle(Paint.Style.STROKE);
        ratePaint.setStrokeWidth(2 * delta);
        ratePaint.setAntiAlias(true);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_drying_center);

        mDrawAnimator = ObjectAnimator.ofFloat(this, "phase", mPhase, 1.0f).setDuration(3000);
        mDrawAnimator.setInterpolator(new LinearInterpolator());
        mDrawAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (listener != null) {
                    listener.onLoading(animation.getCurrentPlayTime());
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制背景
        canvas.drawCircle(viewW / 2, viewH / 2, Math.min(viewW / 2, viewH / 2), bgPaint);

        //绘制圆环底色
        ringRect.set(delta, delta, viewW - delta, viewH - delta);
        canvas.drawArc(ringRect, 0f, 360f, false, ringPaint);

        //绘制刻度线
        for (int i = 0; i < 120; i++) {
            canvas.drawLine(viewW / 2, delta / 2, viewW / 2, delta * 3 / 2 - 2, ticksPaint);
            canvas.rotate(3f, viewW / 2, viewH / 2);
        }

        //绘制中间图标
        canvas.drawBitmap(bitmap, (viewW - bitmap.getWidth()) / 2, (viewH - bitmap.getHeight()) / 2, null);

        //绘制进度条
        rateRect.set(delta, delta, viewW - delta, viewH - delta);
        canvas.drawArc(rateRect, 90f, 360f * mPhase, false, ratePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, 200);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 200);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewW = w;
        viewH = h;
    }

    /**
     * 重置当前View
     */
    public void resetView() {
        if (mDrawAnimator != null && mDrawAnimator.isRunning()) {
            mDrawAnimator.cancel();
        }
        mPhase = 0f;
        invalidate();
    }

    /**
     * 开始动画
     *
     * @param phase
     */
    public void startAnim(float phase) {
        this.mPhase = phase;
        mDrawAnimator.setFloatValues(mPhase, 1f);
        mDrawAnimator.start();
    }

    /**
     * 开始动画
     */
    public void startAnim() {
        this.startAnim(0f);
    }

    /**
     * 停止动画
     */
    public void stopAnim() {
        mDrawAnimator.end();
    }

    /**
     * set the duration of the drawing animation in milliseconds
     * 设置动画执行时长,单位 毫秒
     *
     * @param durationmillis
     */
    public void setAnimDuration(long durationmillis) {
        mDrawAnimator.setDuration(durationmillis);
    }


    /**
     * 返回当前View动画的状态值
     * <p/>
     * 默认必须有该方法,方法名get+Phase,会被ObjectAnimation调用
     *
     * @return
     */
    public float getPhase() {
        return mPhase;
    }

    /**
     * 不要使用该方法
     * <p/>
     * 默认必须有该方法,方法名set+Phase,会被ObjectAnimation调用
     *
     * @param phase
     */
    public void setPhase(float phase) {
        mPhase = phase;
        invalidate();
    }

    /**
     * 环形加载回调监听
     */
    public interface LoadingListener {

        /**
         * 动画每执行一次调用一次,不宜有大内存消耗的动作
         *
         * @param value
         */
        void onLoading(long value);
    }

    /**
     * 添加加载监听
     *
     * @param l
     */
    public void setOnLoadingListener(LoadingListener l) {
        this.listener = l;
    }
}
