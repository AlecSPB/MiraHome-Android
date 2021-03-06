package com.mooring.mh.views.CircleImgView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.mooring.mh.R;

/**
 * 支持缩放且圆形图片背景的View
 * <p/>
 * Created by Will on 16/4/6.
 */
public class ZoomCircleView extends View {

    private Bitmap mBitmap; // 用于缩放的Bitmap
    private Matrix matrix;//用于缩放,矩阵
    private boolean isZoomIn = false;//用于判定是否缩小
    private boolean isZoomOut = false;//用于判定是否放大
    private boolean isZoom = true; //用于支持缩放
    private float mScale = 0.0f;//图片的缩放比例初始值
    private float targetScale = 0.5f;//目标比例
    public static final int SCALE_BIG = 0X11;//放大
    public static final int SCALE_SMALL = 0X22;//缩小
    private int currMode = SCALE_BIG;
    private int currW, currH, left, top, right, bottom;
    private ObjectAnimator animator;//属性动画

    public ZoomCircleView(Context context) {
        this(context, null);
    }

    public ZoomCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttributes(attrs, defStyleAttr);

        matrix = new Matrix();
        animator = ObjectAnimator.ofFloat(this, "scale", mScale, targetScale);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(300);
    }

    private void obtainStyledAttributes(AttributeSet attrs, int defStyleAttr) {
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.ZoomCircleView, defStyleAttr, 0);

        Boolean zoomOut = mTypedArray.getBoolean(R.styleable.ZoomCircleView_img_zoom_out, false);

        this.isZoomIn = !zoomOut;
        this.isZoomOut = zoomOut;

        Drawable srcDrawable = mTypedArray.getDrawable(R.styleable.ZoomCircleView_img_src);
        if (srcDrawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) srcDrawable;
            this.mBitmap = toRoundBitmap(bd.getBitmap());
        }

        mTypedArray.recycle();
    }

    /**
     * 设置默认缩放模式
     * 不可同时设置true或者false
     *
     * @param in  缩小
     * @param out 放大
     */
    public void setZoomInOrOut(boolean in, boolean out) {
        if (in && out) {
            throw new RuntimeException("Do not set the zoom in and out at the same time");
        }
        this.isZoomIn = in;
        this.isZoomOut = out;
    }

    /**
     * 执行缩放
     *
     * @param mode
     */
    public void executeScale(int mode) {
        if (animator.isRunning()) {
            return;
        }
        isZoom = false;
        this.currMode = mode;

        if (mode == SCALE_BIG && isZoomOut && !isZoomIn) {
            isZoom = true;
            isZoomOut = false;
            isZoomIn = true;
        }
        if (mode == SCALE_SMALL && isZoomIn && !isZoomOut) {
            isZoom = true;
            isZoomIn = false;
            isZoomOut = true;
        }

        left = getLeft();
        top = getTop();
        right = getRight();
        bottom = getBottom();
        currW = getWidth();
        currH = getHeight();

        if (isZoom) {
            mScale = 0.0f;
            animator.start();
        }
    }

    /**
     * 绘制圆形图片
     *
     * @param bitmap
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
        paint.setColor(color);

        // 以下有两种方法画圆,drawRounRect和drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        // 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        // 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint); //以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    /**
     * 设置显示的图片
     *
     * @param mBitmap
     */
    public void setImageBitmap(Bitmap mBitmap) {
        this.mBitmap = toRoundBitmap(mBitmap);
        invalidate();
    }

    /**
     * 设置显示图片
     *
     * @param mResource
     */
    public void setImageResource(int mResource) {
        if (mResource != 0) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), mResource);
            this.mBitmap = toRoundBitmap(b);
            invalidate();
        }
    }

    /**
     * scale对应get方法
     *
     * @return
     */
    public float getScale() {
        return mScale;
    }

    /**
     * scale对应set方法
     *
     * @param scale
     */
    public void setScale(float scale) {
        this.mScale = scale;

        int l, t, r, b;
        if (currMode == SCALE_BIG) {
            l = (int) (left - currW * mScale);
            t = (int) (top - currH * mScale);
            r = (int) (right + currW * mScale);
            b = (int) (bottom + currH * mScale);
        } else {
            l = (int) (left + currW * mScale / 2);
            t = (int) (top + currH * mScale / 2);
            r = (int) (right - currW * mScale / 2);
            b = (int) (bottom - currH * mScale / 2);
        }
        layout(l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, matrix, null);
        }

        if (!isZoomIn) {
            Paint p = new Paint();
            p.setColor(getResources().getColor(R.color.transparent_2));
            p.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth(), getHeight()) / 2, p);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(100, 100);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(100, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 100);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

            /*图片缩放view同等大小*/
        mScale = (float) getWidth() / (float) mBitmap.getWidth();
        matrix.setScale(mScale, mScale);
    }

}