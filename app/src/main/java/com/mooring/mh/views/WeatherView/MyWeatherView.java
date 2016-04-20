package com.mooring.mh.views.WeatherView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mooring.mh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 16/4/20.
 */
public class MyWeatherView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int CALM = 0;//晴天--空
    public static final int CLOUDY_DAY = 1;//阴天--空
    public static final int FEW_CLOUDS = 2;//多云--空

    public static final int LIGHT_SNOW = 3;//小雪--数
    public static final int HEAVY_SNOW = 4;//大雪--数
    public static final int LIGHT_RAIN = 5;//小雨
    public static final int HEAVY_RAIN = 6;//大雨
    public static final int HAIL = 7;//冰雹--
    public static final int SLEET = 8;//雨夹雪--中,小
    public static final int SHOWER_RAIN = 9;//雷雨--大雨+闪电
    public static final int FREEZING_RAIN = 10;//雷雨伴随冰雹--雨 小中 bao 小中 闪电

    public static final int LIGHT_BREEZE = 11;//微风--慢
    public static final int HIGH_WIND = 12;//大风--快
    public static final int TORNADO = 13;//龙卷风--同上+速度

    public static final int MIST = 14;//雾/雾霾--
    public static final int DUST_WHIRLS = 15;//沙尘暴--

    public static final int COLD = 16;//冷
    public static final int HOT = 17;//热

    private int kind = LIGHT_SNOW;//天气类型

    public MyThread myThread;
    private SurfaceHolder holder;

    Paint paint = new Paint();

    /**
     * ------------背景图--------------
     */
    private Paint bgPaint = new Paint();
    private Bitmap bg_img;

    /**
     * ------------下落物--------------
     */
    private static int NUM_SNOWFLAKES = 30;//雪花数量
    private static final int DELAY = 40;//延迟时间
    private List<SnowFlake> mSnowFlakes = null; // 碎片
    private List<Bitmap> snows = null;//存放不同碎片
    /**
     * ------------雷电--------------
     */
    private Paint paintLeft = new Paint();
    private Bitmap light_left;//左边雷电
    private Bitmap light_right;//右边雷电
    private int light_alpha = 0;

    public MyWeatherView(Context context) {
        this(context, null);
    }

    public MyWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        mSnowFlakes = new ArrayList<>();
        snows = new ArrayList<>();

        bg_img = BitmapFactory.decodeResource(getResources(), R.mipmap.img_weather_bg_night);
        light_left = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_lightning_1);
        light_right = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_lightning_2);

        holder = this.getHolder();
        holder.addCallback(this);
        myThread = new MyThread(holder);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w != oldw || h != oldh) {
            initSnow(w, h);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (myThread.getState() == Thread.State.NEW) {
            myThread.start();
        }
        if (myThread.getState() == Thread.State.TERMINATED) {
            myThread = new MyThread(holder);
            myThread.start();
        }

        myThread.setRun(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        myThread.setRun(false);
    }

    private void initSnow(int width, int height) {
        snows.clear();
        if (kind == LIGHT_SNOW || kind == HEAVY_SNOW || kind == SLEET) {
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_snow_2));
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_snow_3));
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_snow_5));
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_snow_6));
            if (kind == HEAVY_SNOW) {
                snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_snow_1));
                snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_snow_4));
            }
            if (kind == SLEET) {
                snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rain_2));
                snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rain_4));
            }
        }
        if (kind == LIGHT_RAIN || kind == HEAVY_RAIN || kind == SHOWER_RAIN || kind == FREEZING_RAIN) {
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rain_1));
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rain_2));
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rain_3));
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rain_4));
        }
        if (kind == HAIL) {
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_hail_1));
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_hail_3));
        }
        if (kind == HAIL || kind == FREEZING_RAIN) {
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_hail_2));
            snows.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_hail_4));
        }

        mSnowFlakes.clear();
        //mSnowFlakes所有的雪花都生成放到这里面
        for (int i = 0; i < NUM_SNOWFLAKES; ++i) {
            mSnowFlakes.add(SnowFlake.create(width, height, kind));
        }
    }

    /**
     * 切换加载的碎片个数和类型
     *
     * @param numOfPieces
     */
    private void switchPieces(int numOfPieces) {
        myThread.setRun(false);
        NUM_SNOWFLAKES = numOfPieces;
        initSnow(getWidth(), getHeight());
    }

    /**
     * 切换天气
     *
     * @param kind
     */
    public void switchWeather(int kind) {
        this.kind = kind;
        switch (kind) {
            case CALM:
                break;
            case CLOUDY_DAY:
                break;
            case FEW_CLOUDS:
                break;
            case LIGHT_SNOW:
                switchPieces(30);
                break;
            case HEAVY_SNOW:
                switchPieces(100);
                break;
            case LIGHT_RAIN:
                switchPieces(30);
                break;
            case HEAVY_RAIN:
                switchPieces(150);
                break;
            case HAIL:
                switchPieces(50);
                break;
            case SLEET:
                switchPieces(100);
                break;
            case SHOWER_RAIN:
                switchPieces(100);
                break;
            case FREEZING_RAIN:
                switchPieces(100);
                break;
            case LIGHT_BREEZE:
                break;
            case HIGH_WIND:
                break;
            case TORNADO:
                break;
            case MIST:
                break;
            case DUST_WHIRLS:
                break;
            case COLD:
                break;
            case HOT:
                break;
        }
        myThread.setRun(true);
    }

    /**
     * 暂停线程
     */
    public void onPauseThread() {
        if (myThread != null && myThread.getState() != Thread.State.TERMINATED) {
            myThread.setRun(false);
        }
    }

    /**
     * 重新启用线程
     */
    public void onRestartThread() {
        if (myThread != null && myThread.getState() == Thread.State.TERMINATED) {
            myThread = new MyThread(holder);
            myThread.start();
        }
        myThread.setRun(true);
    }

    /**
     * 自定义线程
     */
    public class MyThread extends Thread {

        private SurfaceHolder holder;
        private boolean run;
        public Canvas canvas = null;

        public MyThread(SurfaceHolder holder) {
            this.holder = holder;
            run = true;
        }

        @Override
        public void run() {
            while (run) {
                Log.e("run", "run   " + holder);
                try {
                    canvas = holder.lockCanvas();

                    //每次绘制前清空画布
//                    Paint paint = new Paint();
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    canvas.drawPaint(paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

                    if (canvas != null) {
                        bgPaint.setAlpha(255);
                        canvas.drawBitmap(bg_img, 0, 0, bgPaint);
                    }

                    //带有雷电
                    if (kind == SHOWER_RAIN || kind == FREEZING_RAIN) {
                        light_alpha += 10;
                        if (light_alpha >= 255)
                            light_alpha = 0;
                        paintLeft.setAlpha(light_alpha);
                        canvas.drawBitmap(light_left, 0, 0, paintLeft);
                        paintLeft.setAlpha(255 - light_alpha);
                        canvas.drawBitmap(light_right, 0, 0, paintLeft);

                    }

                    if (canvas != null && run && (kind == LIGHT_RAIN || kind == HEAVY_RAIN ||
                            kind == LIGHT_SNOW || kind == HEAVY_SNOW || kind == SHOWER_RAIN
                            || kind == HAIL || kind == SLEET || kind == FREEZING_RAIN)) {


                        for (SnowFlake s : mSnowFlakes) {
                            //然后进行绘制
                            if (snows.size() > 0) {
                                s.draw(canvas, snows.get((int) s.mFlakeSize - 1));
                            }
                        }
                    }
                    if (kind == CALM || kind == CLOUDY_DAY || kind == FEW_CLOUDS) {

                    }


                    Thread.sleep(DELAY);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        public boolean isRun() {
            return run;
        }

        public void setRun(boolean run) {
            this.run = run;
        }
    }
}
