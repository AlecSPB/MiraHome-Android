package com.mooring.mh.views.other;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.views.WeatherView.RandomGenerator;
import com.mooring.mh.views.WeatherView.SnowFlake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Will on 16/3/24.
 */
public class WeatherView extends View {

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

    private int duration;

    private int kind = LIGHT_RAIN;//天气类型
    private int count;//下落物总数
    private int speed;//下落速度
    private float maxSize;
    private float minSize;


    private List<Pieces> piecesList;
    private Matrix m = new Matrix();
    private ValueAnimator animator;//The default duration is 300 milliseconds
    private long startTime;
    public static SparseArray<Bitmap> bitmapArray;

    //是否停止绘制下落物
    private boolean isDelyStop = false;

    private int[] piecesImgs = {R.mipmap.ic_rain_1, R.mipmap.ic_rain_2, R.mipmap.ic_rain_3,
            R.mipmap.ic_rain_4};

    private Bitmap bg_img;

    /**
     * ------------雪花--------------
     */
    private static int NUM_SNOWFLAKES = 30;//雪花数量
    private static final int DELAY = 10;//延迟时间
    private List<SnowFlake> mSnowFlakes = new ArrayList<>(); // 雪花
    private List<Bitmap> snows = new ArrayList<>();

    /**
     * ------------雷电--------------
     */
    private Paint paintLeft = new Paint();
    private Bitmap light_left;//左边雷电
    private Bitmap light_right;//右边雷电
    private int light_alpha = 0;


    public WeatherView(Context context) {
        this(context, null);
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //编辑状态
        if (isInEditMode())
            return;

        if (null == attrs) {
            throw new IllegalArgumentException("Attributes should be provided to this view,");
        }

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeatherStyle);
        count = typedArray.getInt(R.styleable.WeatherStyle_pieces_count, 50);
        speed = typedArray.getInt(R.styleable.WeatherStyle_pieces_speed, 500);
        minSize = typedArray.getFloat(R.styleable.WeatherStyle_pieces_min_size, 0.5f);
        maxSize = typedArray.getFloat(R.styleable.WeatherStyle_pieces_max_size, 1.2f);
        kind = typedArray.getInt(R.styleable.WeatherStyle_weather_kind, LIGHT_SNOW);
        typedArray.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {

        bg_img = BitmapFactory.decodeResource(getResources(), R.mipmap.img_weather_bg_night);
        bg_img = BitmapFactory.decodeResource(getResources(), R.mipmap.img_weather_bg_night);
        light_left = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_lightning_1);
        light_right = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_lightning_2);

        bitmapArray = new SparseArray<>();
        piecesList = new ArrayList<>();
//        animator = ValueAnimator.ofFloat(0, 1);
        //设置当前view为单层,不覆盖
        setLayerType(View.LAYER_TYPE_NONE, null);

        //设置动画循环监听
//        setAnimator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        piecesList.clear();
        setPiecesCount(count);
        startTime = System.currentTimeMillis();

        initSnow(w, h);
    }

    long start = System.currentTimeMillis();
    long stop = System.currentTimeMillis();

    int loca = 1;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        if (kind == HAIL) {
//        }

//        if (kind == HEAVY_RAIN) {
//            for (int i = 0; i < piecesList.size(); ++i) {
//                Pieces pieces = piecesList.get(i);
//                m.setTranslate(-pieces.width / 2, -pieces.height / 2);
//                m.postTranslate(pieces.width / 2 + pieces.x, pieces.height / 2
//                        + pieces.y);
//                canvas.drawBitmap(pieces.bitmap, m, null);
//            }
//        }

        canvas.drawBitmap(bg_img, 0, 0, null);


        //带有雷电
        if (kind == SHOWER_RAIN || kind == FREEZING_RAIN) {
            //255全不透

            light_alpha += RandomGenerator.RANDOM.nextFloat() * 5;
            if (light_alpha >= 255) {
                light_alpha = 0;
                loca = (int) (RandomGenerator.RANDOM.nextFloat() * 2);
            }
            paintLeft.setAlpha(255 - light_alpha);
            canvas.drawBitmap(loca == 1 ? light_left : light_right, 0, 0, paintLeft);

        }

        /**
         * 小雨,大雨,小雪,大雪,雷雨,冰雹,雨夹雪,雷雨加冰雹
         */
        if (kind == LIGHT_RAIN || kind == HEAVY_RAIN || kind == LIGHT_SNOW || kind == HEAVY_SNOW
                || kind == SHOWER_RAIN || kind == HAIL || kind == SLEET || kind == FREEZING_RAIN) {
            for (SnowFlake s : mSnowFlakes) {
                //然后进行绘制
                s.draw(canvas, snows.get((int) s.mFlakeSize - 1));
            }
            // 隔一段时间重绘一次, 动画效果
            getHandler().postDelayed(runnable, DELAY);
        }
    }

    // 重绘线程
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //自动刷新
            invalidate();
        }
    };

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
     * 添加动画加载监听
     */
    private void setAnimator() {
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long nowTime = System.currentTimeMillis();
                float secs = (float) (nowTime - startTime) / 1000f;
                startTime = nowTime;
                for (int i = 0; i < piecesList.size(); ++i) {

                    Pieces pieces = piecesList.get(i);

                    pieces.y += (pieces.speed * secs);

                    if (pieces.y > getHeight()) {
                        if (isDelyStop) {
                            piecesList.remove(i);
                        } else {
                            pieces.y = 0 - pieces.height;
                        }
                    }
                }
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);//无限重复动画
        animator.setDuration(300);//执行时间
    }


    /**
     * 设置下落物总数量
     *
     * @param quantity
     */
    public void setPiecesCount(int quantity) {
        if (piecesImgs == null || piecesImgs.length == 0)
            return;
        int leftCount = Math.abs(quantity - piecesList.size());
        for (int i = 0; i < leftCount; ++i) {
            Bitmap originalBitmap = BitmapFactory
                    .decodeResource(getResources(), piecesImgs[i % piecesImgs.length]);
            Pieces pieces = new Pieces(getWidth(), originalBitmap, speed);
            pieces.bitmap = bitmapArray.get(pieces.width);
            if (pieces.bitmap == null) {
                pieces.bitmap = originalBitmap;
                bitmapArray.put(pieces.width, pieces.bitmap);
            }
            piecesList.add(pieces);
        }
    }

    /**
     * 将屏幕中的动画显示完成后，
     */
    public void stopRainDely() {
        this.isDelyStop = true;
    }

    /**
     * 停止绘制和动画
     */
    public void stopRainNow() {
        isDelyStop = true;
        piecesList.clear();
        invalidate();
        animator.cancel();
    }


    /**
     * 重新开始显示动画
     */
    public void startRain() {
        this.isDelyStop = false;
        setPiecesCount(count);
        animator.start();
    }

    /**
     * 设置下落物
     *
     * @param pieces
     */
    private void setPieces(int... pieces) {
        this.piecesImgs = pieces;
        piecesList.clear();
        this.setPiecesCount(count);
    }

    /**
     * 设置下降速度
     *
     * @param speed
     */
    public void setSpeed(int speed) {
        for (Pieces pieces : piecesList) {
            pieces.setSpeed(speed);
        }
    }

    /**
     * 切换加载的碎片个数和类型
     *
     * @param numOfPieces
     */
    private void switchPieces(int numOfPieces) {
        getHandler().removeCallbacks(runnable);
        NUM_SNOWFLAKES = numOfPieces;
        initSnow(getWidth(), getHeight());
    }

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
                break;
            case DUST_WHIRLS:
                break;
            case COLD:
                break;
            case HOT:
                break;
        }
        invalidate();
    }


    /**
     * 碎片类
     */
    public class Pieces {
        private float x, y;//xy坐标
        private float speed;//速度
        private int width, height;
        private Bitmap bitmap;

        public Pieces(float xRange, Bitmap originalBitmap, int speed) {
            double widthRandom = Math.random();
            if (widthRandom < minSize || widthRandom > maxSize) {
                widthRandom = maxSize;
            }
            width = (int) (originalBitmap.getWidth() * widthRandom);
            float hwRatio = originalBitmap.getHeight() * 1.0f / originalBitmap.getWidth();
            height = (int) (width * hwRatio);
            x = (float) Math.random() * (xRange - width);
            y = 0 - (height + (float) Math.random() * height);
            this.speed = speed + (float) Math.random() * 1000;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }
    }
}
