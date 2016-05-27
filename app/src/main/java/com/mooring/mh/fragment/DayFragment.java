package com.mooring.mh.fragment;

import android.graphics.PointF;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.views.ChartView.MyChartView;
import com.mooring.mh.views.ChartView.YAxisView;
import com.mooring.mh.views.CircleProgress.CircleDisplay;
import com.mooring.mh.views.CircleProgress.DoubleCircleView;

import java.util.ArrayList;
import java.util.List;

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
    MyChartView myChartView1;
    MyChartView myChartView2;
    MyChartView myChartView3;

    YAxisView allChart1;
    YAxisView allChart2;
    YAxisView allChart3;
    List<String> xdata;
    List<String> ydata;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_day;
    }

    @Override
    protected void initFragment() {

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

        myChartView1 = (MyChartView) rootView.findViewById(R.id.myChartView1);
        myChartView2 = (MyChartView) rootView.findViewById(R.id.myChartView2);
        myChartView3 = (MyChartView) rootView.findViewById(R.id.myChartView3);

        allChart1 = (YAxisView) rootView.findViewById(R.id.allChart1);
        allChart2 = (YAxisView) rootView.findViewById(R.id.allChart2);
        allChart3 = (YAxisView) rootView.findViewById(R.id.allChart3);


        /**
         * 设置监听
         */
        imgView_left.setOnClickListener(this);
        imgView_right.setOnClickListener(this);
        imgView_left_middle.setOnClickListener(this);
        imgView_right_middle.setOnClickListener(this);

        initData();
    }

    private List<String> data1 = new ArrayList<>();
    private List<String> data2 = new ArrayList<>();
    private List<String> data3 = new ArrayList<>();

    private void initData() {

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


        for (int i = 0; i <= 24; i++) {
            xdata.add(i + ":00");
        }

        float[] test1 = new float[]{15, 21, 9, 21, 24, 15, 24, 18, 13, 20, 22, 19, 7};
        float[] test2 = new float[]{15, 13, 20, 22, 19, 21, 9, 21, 24, 15, 24, 18, 7};
        float[] test3 = new float[]{24, 18, 13, 20, 22, 15, 21, 19, 9, 21, 24, 15, 7};

        for (int i = 0; i < test1.length; i++) {
            data1.add(test1[i] + "");
            data2.add(test2[i] + "");
            data3.add(test3[i] + "");
        }

        myChartView1.setMax(60);
        myChartView1.setDatas(xdata, ydata, data1);
        myChartView2.setMax(60);
        myChartView2.setDatas(xdata, ydata, data2);
        myChartView3.setMax(60);
        myChartView3.setDatas(xdata, ydata, data3);

        allChart1.setYdatas(ydata);
        allChart2.setYdatas(ydata);
        allChart3.setYdatas(ydata);
    }

    @Override
    public void onClick(View v) {

    }

}
