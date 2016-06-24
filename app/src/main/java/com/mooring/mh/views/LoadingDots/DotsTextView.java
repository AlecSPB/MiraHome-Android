package com.mooring.mh.views.LoadingDots;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mooring.mh.R;

import java.lang.ref.WeakReference;

/**
 * 自定义加载文本View
 * <p>
 * Created by Will on 16/6/23.
 */
public class DotsTextView extends TextView {

    private JumpingSpan dotOne;//第一个文本
    private JumpingSpan dotTwo;//第二个文本
    private JumpingSpan dotThree;//第三个文本

    private int showSpeed = 700;//展开和收起的时间
    private int jumpHeight;//跳动的高度
    private boolean autoPlay;//自动加载
    private boolean isPlaying;//正在加载
    private boolean isHide;//是否收起
    private int period;//每次加载时间长度

    private AnimatorSet mAnimatorSet;
    private float textWidth;

    public DotsTextView(Context context) {
        this(context, null);
    }

    public DotsTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaitingDots);
            period = typedArray.getInt(R.styleable.WaitingDots_period, 6000);
            jumpHeight = typedArray.getInt(
                    R.styleable.WaitingDots_jumpHeight, (int) (getTextSize() / 4));
            autoPlay = typedArray.getBoolean(R.styleable.WaitingDots_autoPlay, true);
            typedArray.recycle();
        }

        initView();
    }

    private void initView() {

        dotOne = new JumpingSpan();
        dotTwo = new JumpingSpan();
        dotThree = new JumpingSpan();

        SpannableString span = new SpannableString("...\u200B");
        span.setSpan(dotOne, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(dotTwo, 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(dotThree, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(span);

        textWidth = getPaint().measureText(".", 0, 1);

        ObjectAnimator oneAnimation = createDotJumpAnimator(dotOne, 0);
        oneAnimation.addUpdateListener(new InvalidateViewOnUpdate(this));
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(oneAnimation, createDotJumpAnimator(dotTwo, period / 6)
                , createDotJumpAnimator(dotThree, period * 2 / 6));

        isPlaying = autoPlay;
        if (autoPlay && !isInEditMode()) {
            start();
        }
    }

    /**
     * 创建跳动动画
     *
     * @param jumpingSpan
     * @param delay
     * @return
     */
    private ObjectAnimator createDotJumpAnimator(JumpingSpan jumpingSpan, long delay) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(jumpingSpan, "translationY", 0, -jumpHeight);

        objectAnimator.setEvaluator(new SinTypeEvaluator());
        objectAnimator.setDuration(period);
        objectAnimator.setStartDelay(delay);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        return objectAnimator;
    }

    /**
     * 开始
     */
    public void start() {
        isPlaying = true;
        setAllAnimationsRepeatCount(ValueAnimator.INFINITE);
        mAnimatorSet.start();
    }

    /**
     * 停止
     */
    public void stop() {
        isPlaying = false;
        setAllAnimationsRepeatCount(0);
    }

    /**
     * 显示
     */
    public void show() {

        ObjectAnimator dotThreeMoveLeftToRight =
                ObjectAnimator.ofFloat(dotThree, "translationX", -textWidth * 2, 0);
        dotThreeMoveLeftToRight.setDuration(showSpeed);
        dotThreeMoveLeftToRight.start();

        ObjectAnimator dotTwoMoveLeftToRight =
                ObjectAnimator.ofFloat(dotTwo, "translationX", -textWidth * 1, 0);
        dotTwoMoveLeftToRight.setDuration(showSpeed);
        dotTwoMoveLeftToRight.addUpdateListener(new InvalidateViewOnUpdate(this));
        dotTwoMoveLeftToRight.start();

        isHide = false;
    }

    /**
     * 隐藏
     */
    public void hide() {

        ObjectAnimator dotThreeMoveRightToLeft =
                ObjectAnimator.ofFloat(dotThree, "translationX", 0, -textWidth * 2);
        dotThreeMoveRightToLeft.setDuration(showSpeed);
        dotThreeMoveRightToLeft.start();

        ObjectAnimator dotTwoMoveRightToLeft =
                ObjectAnimator.ofFloat(dotTwo, "translationX", 0, -textWidth * 1);
        dotTwoMoveRightToLeft.setDuration(showSpeed);
        dotTwoMoveRightToLeft.addUpdateListener(new InvalidateViewOnUpdate(this));
        dotTwoMoveRightToLeft.start();

        isHide = true;
    }

    /**
     * 显示且开始
     */
    public void showAndPlay() {
        show();
        start();
    }

    /**
     * 隐藏且停止
     */
    public void hideAndStop() {
        hide();
        stop();
    }

    /**
     * 给所有动画设置重复次数
     *
     * @param repeatCount
     */
    private void setAllAnimationsRepeatCount(int repeatCount) {
        for (Animator animator : mAnimatorSet.getChildAnimations()) {
            if (animator instanceof ObjectAnimator) {
                ((ObjectAnimator) animator).setRepeatCount(repeatCount);
            }
        }
    }

    /**
     * 自定义动画加载监听
     */
    public class InvalidateViewOnUpdate implements ValueAnimator.AnimatorUpdateListener {

        private final WeakReference<View> viewRef;

        public InvalidateViewOnUpdate(View view) {
            this.viewRef = new WeakReference<>(view);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final View view = viewRef.get();
            if (view != null) {
                view.invalidate();
            }
        }
    }

    /**
     * get
     *
     * @return
     */
    public boolean isHide() {
        return isHide;
    }

    /**
     * get
     *
     * @return
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * set
     *
     * @param jumpHeight
     */
    public void setJumpHeight(int jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    /**
     * set
     *
     * @param period
     */
    public void setPeriod(int period) {
        this.period = period;
    }
}
