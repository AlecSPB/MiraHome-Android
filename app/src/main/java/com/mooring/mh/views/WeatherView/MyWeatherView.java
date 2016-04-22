package com.mooring.mh.views.WeatherView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Will on 16/4/20.
 */
public class MyWeatherView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

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

    private int kind = LIGHT_BREEZE;//天气类型

    /**
     * ------------初始化 holder--------------
     */
    private SurfaceHolder holder;//holder
    private boolean isRunning = true;//绘制标志位
    private Canvas canvas = null;
    private int viewW;//当前view宽度
    private int viewH;//当前View高度
    private Calendar cal;
    private int sunRise = 6;//日出
    private int sunSet = 18;//日落
    /**
     * ------------背景图--------------
     */
    private Paint bgPaint = new Paint();
    private Bitmap bg_img;
    /**
     * ------------背景图--------------
     */
    private List<Bitmap> cloudList;
    /**
     * ------------下落物--------------
     */
    private static int NUM_SNOWFLAKES = 30;//雪花数量
    private static final int DELAY = 10;//延迟时间
    private List<SnowFlake> mSnowFlakes = null; // 碎片
    private List<Bitmap> snows = null;//存放不同碎片
    /**
     * ------------雷电--------------
     */
    private Paint paintLeft = new Paint();
    private Bitmap light_left;//左边雷电
    private Bitmap light_right;//右边雷电
    private int light_alpha = 0;
    /**
     * ------------风车--------------
     */
    private Bitmap pillar_big;//大风车柱子
    private Bitmap blade_big;//大风车扇叶
    private Bitmap pillar_small;//小风车柱子
    private Bitmap blade_small;//小风车扇叶
    float windScale = 0.0f;
    int windBig_x = CommonUtils.dp2px(getContext(), 150);
    int windBig_y = CommonUtils.dp2px(getContext(), 100);
    int windSma_x = CommonUtils.dp2px(getContext(), 30);
    int windSma_y = CommonUtils.dp2px(getContext(), 200);

    private Matrix matrixWind = new Matrix();

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

    /**
     * 初始化
     */
    private void initView() {

        mSnowFlakes = new ArrayList<>();
        snows = new ArrayList<>();

        cal = Calendar.getInstance();
        cloudList = new ArrayList<>();
        cloudList.add(createBitmap(R.mipmap.ic_day_cloud_1, false));
        cloudList.add(createBitmap(R.mipmap.ic_day_cloud_2, false));
        cloudList.add(createBitmap(R.mipmap.ic_day_cloud_3, false));
        cloudList.add(createBitmap(R.mipmap.ic_day_cloud_4, false));
        cloudList.add(createBitmap(R.mipmap.ic_day_cloud_5, false));
        cloudList.add(createBitmap(R.mipmap.ic_night_cloud_1, false));
        cloudList.add(createBitmap(R.mipmap.ic_night_cloud_2, false));
        cloudList.add(createBitmap(R.mipmap.ic_night_cloud_3, false));
        cloudList.add(createBitmap(R.mipmap.ic_night_cloud_4, false));
        cloudList.add(createBitmap(R.mipmap.ic_night_cloud_5, false));

        bg_img = createBitmap(R.mipmap.img_weather_bg_night, false);
        light_left = createBitmap(R.mipmap.ic_lightning_1, false);
        light_right = createBitmap(R.mipmap.ic_lightning_2, false);

        pillar_small = createBitmap(R.mipmap.ic_windmill_pillar_small, false);
        blade_small = createBitmap(R.mipmap.ic_windmill_blade_small, false);
        pillar_big = createBitmap(R.mipmap.ic_windmill_pillar_big, false);
        blade_big = createBitmap(R.mipmap.ic_windmill_blade_big, false);

        holder = this.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888); // 顶层绘制SurfaceView设成透明

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewW = w;
        viewH = h;

        if (w != oldw || h != oldh) {
            initSnow(w, h);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        if (myThread.getState() == Thread.State.NEW) {
//            myThread.start();
//        }
//        if (myThread.getState() == Thread.State.TERMINATED) {
//            myThread = new MyThread(holder);
//            myThread.start();
//        }
//
//        myThread.setRun(true);
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        isRunning = false;
//        myThread.setRun(false);
    }

    private void initSnow(int width, int height) {
        snows.clear();
        if (kind == LIGHT_SNOW || kind == HEAVY_SNOW || kind == SLEET) {
            snows.add(createBitmap(R.mipmap.ic_snow_2, false));
            snows.add(createBitmap(R.mipmap.ic_snow_3, false));
            snows.add(createBitmap(R.mipmap.ic_snow_5, false));
            snows.add(createBitmap(R.mipmap.ic_snow_6, false));
            if (kind == HEAVY_SNOW) {
                snows.add(createBitmap(R.mipmap.ic_snow_1, false));
                snows.add(createBitmap(R.mipmap.ic_snow_4, false));
            }
            if (kind == SLEET) {
                snows.add(createBitmap(R.mipmap.ic_rain_2, false));
                snows.add(createBitmap(R.mipmap.ic_rain_4, false));
            }
        }
        if (kind == LIGHT_RAIN || kind == HEAVY_RAIN || kind == SHOWER_RAIN || kind == FREEZING_RAIN) {
            snows.add(createBitmap(R.mipmap.ic_rain_1, false));
            snows.add(createBitmap(R.mipmap.ic_rain_2, false));
            snows.add(createBitmap(R.mipmap.ic_rain_3, false));
            snows.add(createBitmap(R.mipmap.ic_rain_4, false));
        }
        if (kind == HAIL) {
            snows.add(createBitmap(R.mipmap.ic_hail_1, false));
            snows.add(createBitmap(R.mipmap.ic_hail_3, false));
        }
        if (kind == HAIL || kind == FREEZING_RAIN) {
            snows.add(createBitmap(R.mipmap.ic_hail_2, false));
            snows.add(createBitmap(R.mipmap.ic_hail_4, false));
        }
        if (kind == MIST || kind == DUST_WHIRLS) {
            snows.add(createBitmap(R.mipmap.ic_mist_2, false));
            snows.add(createBitmap(R.mipmap.ic_mist_2, false));
            snows.add(createBitmap(R.mipmap.ic_mist_2, false));
            snows.add(createBitmap(R.mipmap.ic_mist_2, false));
        }
        if (kind == DUST_WHIRLS) {
            snows.add(createBitmap(R.mipmap.ic_mist_1, false));
            snows.add(createBitmap(R.mipmap.ic_mist_1, false));
        }
        mSnowFlakes.clear();
        //mSnowFlakes所有的雪花都生成放到这里面
        for (int i = 0; i < NUM_SNOWFLAKES; ++i) {
            mSnowFlakes.add(SnowFlake.create(width, height, kind));
        }
    }

    /**
     * 判别当前时间是否在白天
     *
     * @return true:白天 false:黑夜
     */
    private boolean judgeCurrTime() {
        int currTime = cal.get(Calendar.HOUR_OF_DAY);
        if (currTime >= sunRise && currTime <= sunSet) {
            return true;
        }
        return false;
    }

    /**
     * 创建Bitmap
     *
     * @param id   id
     * @param isBg 是否是背景图
     * @return
     */
    private Bitmap createBitmap(int id, boolean isBg) {
        Bitmap bitmap = null;
        if (id != 0) {
            bitmap = BitmapFactory.decodeResource(getResources(), id);
            if (isBg) {
                Matrix matrix = new Matrix();
                matrix.postScale((float) viewW / bitmap.getWidth(), (float) viewH / bitmap.getHeight());
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, viewW, viewH, matrix, true);
            }
        }
        return bitmap;
    }

    /**
     * 切换加载的碎片个数和类型
     *
     * @param numOfPieces
     */
    private void switchPieces(int numOfPieces) {
        //切换背景
        switchBackground(kind);
        //重新初始化下落物
        NUM_SNOWFLAKES = numOfPieces;
        initSnow(getWidth(), getHeight());
    }

    /**
     * 切换天气对应的背景
     *
     * @param kind
     */
    private void switchBackground(int kind) {
        switch (kind) {
            case CALM:
                bg_img = createBitmap(R.mipmap.img_weather_bg_calm, true);
                break;
            case CLOUDY_DAY:
            case FEW_CLOUDS:
                bg_img = createBitmap(R.mipmap.img_weather_bg_cloudy_day, true);
                break;
            case LIGHT_SNOW:
            case HEAVY_SNOW:
            case SLEET:
                bg_img = createBitmap(R.mipmap.img_weather_bg_sleet, true);
                break;
            case LIGHT_RAIN:
            case HEAVY_RAIN:
            case HAIL:
            case SHOWER_RAIN:
            case FREEZING_RAIN:
                bg_img = createBitmap(R.mipmap.img_weather_bg_and_hail, true);
                break;
            case LIGHT_BREEZE:
                bg_img = createBitmap(R.mipmap.img_weather_bg_light_breeze, true);
                break;
            case HIGH_WIND:
            case TORNADO:
                bg_img = createBitmap(R.mipmap.img_weather_bg_tornado, true);
                break;
            case MIST:
            case DUST_WHIRLS:
                bg_img = createBitmap(R.mipmap.img_weather_bg_mist, true);
                break;
            case COLD:
                bg_img = createBitmap(R.mipmap.img_weather_bg_cold, true);
                break;
            case HOT:
                bg_img = createBitmap(R.mipmap.img_weather_bg_hot, true);
                break;
        }
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
                switchPieces(60);
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
                switchPieces(50);
                break;
            case DUST_WHIRLS:
                switchPieces(100);
                break;
            case COLD:
                break;
            case HOT:
                break;
        }
        isRunning = true;
    }

    private int light_location = 1;

    @Override
    public void run() {

        while (isRunning) {
            synchronized (this) {
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        /**
                         * 绘制背景
                         */
                        bgPaint.setAntiAlias(true);
                        bgPaint.setFilterBitmap(true);
                        RectF rect = new RectF(0, 0, getWidth(), getHeight());
                        canvas.drawBitmap(bg_img, null, rect, bgPaint);
                        /**
                         * 绘制云朵
                         */
                        if (judgeCurrTime()) {
                            canvas.drawBitmap(cloudList.get(0), 0, 0, null);
                            canvas.drawBitmap(cloudList.get(1), 0, 0, null);
                            canvas.drawBitmap(cloudList.get(2), 0, 0, null);
                            canvas.drawBitmap(cloudList.get(3), 0, 0, null);
                            canvas.drawBitmap(cloudList.get(4), 0, 0, null);
                        } else {
                            canvas.drawBitmap(cloudList.get(5), 0, 0, null);
                            canvas.drawBitmap(cloudList.get(6), 0, 0, null);
                            canvas.drawBitmap(cloudList.get(7), 0, 0, null);
                            canvas.drawBitmap(cloudList.get(8), 0, 0, null);
                            canvas.drawBitmap(cloudList.get(9), 0, 0, null);
                        }
                        /**
                         * 绘制带有雷电
                         */
                        if (kind == SHOWER_RAIN || kind == FREEZING_RAIN) {
                            light_alpha += RandomGenerator.RANDOM.nextFloat() * 10;
                            if (light_alpha >= 255) {
                                light_alpha = 0;
                                light_location = (int) (RandomGenerator.RANDOM.nextFloat() * 2);
                            }
                            paintLeft.setAlpha(255 - light_alpha);
                            canvas.drawBitmap(light_location == 1 ? light_left : light_right, 0, 0, paintLeft);
                        }
                        /**
                         * 绘制---小雨,大雨,小雪,大雪,雷雨,冰雹,雨夹雪,雷雨加冰雹
                         */
                        if (isRunning && (kind == LIGHT_RAIN || kind == HEAVY_RAIN || kind == MIST ||
                                kind == LIGHT_SNOW || kind == HEAVY_SNOW || kind == SHOWER_RAIN ||
                                kind == DUST_WHIRLS || kind == HAIL || kind == SLEET || kind == FREEZING_RAIN)) {

                            for (SnowFlake s : mSnowFlakes) {
                                //然后进行绘制
                                if (snows.size() > 0) {
                                    s.draw(canvas, snows.get((int) s.mFlakeSize - 1));
                                }
                            }
                        }
                        /**
                         * 绘制---晴天,阴天,多云
                         */
                        if (kind == CALM || kind == CLOUDY_DAY || kind == FEW_CLOUDS) {

                        }
                        /**
                         * 绘制---微风,大风,龙卷风
                         */
                        if (kind == LIGHT_BREEZE || kind == HIGH_WIND || kind == TORNADO) {
                            canvas.drawBitmap(pillar_small, windSma_x + (blade_small.getWidth() -
                                    pillar_small.getWidth()) / 2, windSma_y + blade_small.getHeight() / 2 - 20, bgPaint);

                            canvas.drawBitmap(pillar_big, windBig_x + (blade_big.getWidth() -
                                    pillar_big.getWidth()) / 2, windBig_y + blade_big.getHeight() / 2 - 20, bgPaint);

                            matrixWind.reset();
                            matrixWind.postRotate((windScale -= 1) % 360f, blade_small.getWidth() / 2,
                                    blade_small.getHeight() / 2);
                            matrixWind.postTranslate(windSma_x, windSma_y);
                            canvas.drawBitmap(blade_small, matrixWind, bgPaint);

                            matrixWind.reset();
                            matrixWind.postRotate((windScale) % 360f, blade_big.getWidth() / 2,
                                    blade_big.getHeight() / 2);
                            matrixWind.postTranslate(windBig_x, windBig_y);
                            canvas.drawBitmap(blade_big, matrixWind, bgPaint);
                        }
                        Thread.sleep(DELAY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
