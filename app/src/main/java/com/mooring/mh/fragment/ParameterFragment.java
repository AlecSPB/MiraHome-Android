package com.mooring.mh.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.mooring.mh.R;
import com.mooring.mh.views.CustomImageView.CircleImageView;
import com.mooring.mh.views.CustomImageView.ZoomCircleView;
import com.mooring.mh.views.other.GiftRainView;

/**
 * Created by Will on 16/3/24.
 */
public class ParameterFragment extends BaseFragment {

    private GiftRainView giftRainView;
    private boolean isStart;
    private ZoomCircleView zoomCircleView;
    private ZoomCircleView zoomCircleView1;

    private ImageView imgView111;
    private ImageView imgView222;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_parameter;
    }

    @Override
    protected void initView() {
//        giftRainView = (GiftRainView) rootView.findViewById(R.id.giftRainView);
//
//
//        giftRainView.setImages(R.mipmap.ico_gold_money, R.mipmap.ico_money, R.mipmap.ic_launcher);
//
//        giftRainView.startRain();
//        isStart = true;
//
//        giftRainView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isStart) {
//                    giftRainView.startRain();
//                    isStart = true;
//                } else {
//                    giftRainView.stopRainDely();
//                    isStart = false;
//                }
//            }
//        });

        zoomCircleView = (ZoomCircleView) rootView.findViewById(R.id.circleView);
        zoomCircleView.setZoomInOrOut(true, false);

        zoomCircleView1 = (ZoomCircleView) rootView.findViewById(R.id.circleView1);
        zoomCircleView1.setZoomInOrOut(false, true);
        zoomCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomCircleView.setImageResource(R.mipmap.ee_2);
                zoomCircleView.executeScale(0.5f);
                zoomCircleView1.executeScale(2f);
            }
        });
        zoomCircleView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomCircleView.setImageResource(R.mipmap.ee_1);
                zoomCircleView.executeScale(2f);
                zoomCircleView1.executeScale(0.5f);
            }
        });


        imgView111 = (ImageView) rootView.findViewById(R.id.imgView111);
        imgView222 = (ImageView) rootView.findViewById(R.id.imgView222);

        imgView111.setImageBitmap(toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ee_1)));
        imgView222.setImageBitmap(toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ee_2)));

        imgView111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimation animation = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(2000);//设置动画持续时间
                imgView111.setAnimation(animation);
            }
        });


        imgView222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimation animation = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(2000);//设置动画持续时间
                imgView222.setAnimation(animation);
            }
        });


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
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); //以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    @Override
    protected void lazyLoad() {

    }
}
