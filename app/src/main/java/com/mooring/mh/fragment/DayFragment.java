package com.mooring.mh.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.views.CircleProgress.CircleDisplay;
import com.mooring.mh.views.CircleProgress.DoubleCircleView;
import com.mooring.mh.views.MyLineChartView;

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
 * Created by Will on 16/3/28.
 */
public class DayFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 日期选择
     */
    private ImageView imgView_left;
    private ImageView imgView_right;
    private TextView tv_middle_time;
    /**
     * 次数选择
     */
    private ImageView imgView_left_middle;
    private ImageView imgView_right_middle;
    private DoubleCircleView dcv_day;
    private TextView tv_middle_score;
    /**
     * 睡眠数据展示
     */
    private TextView tv_sleep_time;
    private TextView tv_sleep_range;
    private CircleDisplay circle_progress_1;
    private CircleDisplay circle_progress_2;
    private CircleDisplay circle_progress_3;
    private CircleDisplay circle_progress_4;
    /**
     * 图表展示
     */
    private MyLineChartView chart_heart_rate;
    private LineChartView chart_breathing_rate;
    private LineChartView chart_body_movement;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_day;
    }

    @Override
    protected void initView() {
        imgView_left = (ImageView) rootView.findViewById(R.id.imgView_left);
        imgView_right = (ImageView) rootView.findViewById(R.id.imgView_right);
        tv_middle_time = (TextView) rootView.findViewById(R.id.tv_middle_time);

        imgView_left_middle = (ImageView) rootView.findViewById(R.id.imgView_left_middle);
        imgView_right_middle = (ImageView) rootView.findViewById(R.id.imgView_right_middle);
        dcv_day = (DoubleCircleView) rootView.findViewById(R.id.dcv_day);
        tv_middle_score = (TextView) rootView.findViewById(R.id.tv_middle_score);

        tv_sleep_time = (TextView) rootView.findViewById(R.id.tv_sleep_time);
        tv_sleep_range = (TextView) rootView.findViewById(R.id.tv_sleep_range);
        circle_progress_1 = (CircleDisplay) rootView.findViewById(R.id.circle_progress_1);
        circle_progress_2 = (CircleDisplay) rootView.findViewById(R.id.circle_progress_2);
        circle_progress_3 = (CircleDisplay) rootView.findViewById(R.id.circle_progress_3);
        circle_progress_4 = (CircleDisplay) rootView.findViewById(R.id.circle_progress_4);

        chart_heart_rate = (MyLineChartView) rootView.findViewById(R.id.chart_heart_rate);
        chart_breathing_rate = (LineChartView) rootView.findViewById(R.id.chart_breathing_rate);
        chart_body_movement = (LineChartView) rootView.findViewById(R.id.chart_body_movement);

        /**
         * 设置监听
         */
        imgView_left.setOnClickListener(this);
        imgView_right.setOnClickListener(this);
        imgView_left_middle.setOnClickListener(this);
        imgView_right_middle.setOnClickListener(this);

        initData();
    }


    private LineChartData data;

    private void initData() {
        generateDefaultData();

        chart_heart_rate.setLineChartData(data);
        chart_heart_rate.setInteractive(true);

        chart_heart_rate.setZoomEnabled(false);
        chart_heart_rate.setScrollEnabled(true);
        Viewport tempViewport = new Viewport(chart_heart_rate.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        chart_heart_rate.setCurrentViewport(tempViewport);
    }

    private void generateDefaultData() {

        int numValues = 50;

        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, (float) Math.random() * 100f));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);
        line.setHasPoints(false);// too many values so don't draw points.

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        data = new LineChartData(lines);
        data.setAxisXBottom(new Axis());
        data.setAxisYRight(new Axis().setHasLines(false));
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View v) {

    }

}
