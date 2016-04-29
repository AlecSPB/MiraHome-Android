package com.mooring.mh.activity;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.mooring.mh.R;
import com.mooring.mh.model.SleepTime;
import com.mooring.mh.views.AllChartView;
import com.mooring.mh.views.CircleProgress.DoubleCircleView;
import com.mooring.mh.views.MyChartView;
import com.mooring.mh.views.WeatherView.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 用于测试
 * Created by Will on 16/4/27.
 */
public class TestActivity extends AppCompatActivity {

    DoubleCircleView doubleCircle;

    Button test_date_picker;

    RandomGenerator mRandom;

    List<SleepTime> outDatas;
    List<SleepTime> innDatas;

    private LineChartView chart_heart_rate;
    private LineChartData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

       /* mRandom = new RandomGenerator();

        doubleCircle = (DoubleCircleView) findViewById(R.id.doubleCircle);

        outDatas = new ArrayList<>();
        innDatas = new ArrayList<>();

        test_date_picker = (Button) findViewById(R.id.test_date_picker);
        test_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                outDatas.clear();
                innDatas.clear();

                SleepTime sleep = new SleepTime();
                sleep.setType(-1);
                sleep.setTime(12);
                outDatas.add(sleep);
                for (int i = 0; i < 10; i++) {
                    SleepTime sleepTime = new SleepTime();
                    sleepTime.setType(i % 4);
                    sleepTime.setTime(20 + mRandom.getRandom(30));
                    outDatas.add(sleepTime);
                }

                for (int i = 0; i < 5; i++) {
                    SleepTime sleepTime = new SleepTime();
                    sleepTime.setType(mRandom.getRandom(3));
                    sleepTime.setTime(mRandom.getRandom(60));
                    innDatas.add(sleepTime);
                }


                doubleCircle.setDatass(outDatas, innDatas);
            }
        });
        *//**
         *chart_heart_rate
         *//*
        chart_heart_rate = (LineChartView) findViewById(R.id.chart_heart_rate);
        int numValues = 50;

        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, (float) Math.random() * 100f));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN);
        line.setHasPoints(false);// too many values so don't draw points.

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        data = new LineChartData(lines);
        data.setAxisXBottom(new Axis());
        data.setAxisYRight(new Axis().setHasLines(false));

        chart_heart_rate.setLineChartData(data);
        // Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
        // zoom/scroll is unnecessary.
        chart_heart_rate.setZoomEnabled(false);
        chart_heart_rate.setScrollEnabled(true);
        Viewport tempViewport = new Viewport(chart_heart_rate.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        chart_heart_rate.setCurrentViewport(tempViewport);*/


        myChartView = (MyChartView) findViewById(R.id.myChartView);
        xdata = new ArrayList<>();
        ydata = new ArrayList<>();

        ydata.add("15h");
        ydata.add("20h");
        ydata.add("25h");
        ydata.add("30h");
        ydata.add("35h");
        ydata.add("40h");
        ydata.add("45h");
        ydata.add("50h");
        ydata.add("55h");
        ydata.add("60h");

        PointF[] po = new PointF[]{};


        for (int i = 0; i < 34; i++) {
            xdata.add(i + ":00");
        }

        test_date_picker = (Button) findViewById(R.id.test_date_picker);
        test_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myChartView.setDatas(xdata, ydata, new float[]{15, 21, 9, 21, 24, 15, 24, 18, 13, 20, 22, 19, 7});
            }
        });


//        myChartView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    oldX = event.getX();
//                    oldY = event.getY();
//                }
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    X = X + myChartView.getLeft() + event.getX() - oldX;
//                    Y = Y + myChartView.getTop() + event.getY() - oldY;
//
//                    myChartView.scrollTo(-(int) X,myChartView.getTop());
//
//                    oldX = event.getX();
//                    oldY = event.getY();
//                }
//
//                return true;
//            }
//        });

    }

    MyChartView myChartView;
    List<String> xdata;
    List<String> ydata;

    private float oldX = 0;
    private float oldY = 0;

    private float X = 0;
    private float Y = 0;
}
