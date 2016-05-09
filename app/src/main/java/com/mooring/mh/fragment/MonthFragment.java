package com.mooring.mh.fragment;

import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.mooring.mh.R;
import com.mooring.mh.views.ChartView.XXChartView;
import com.mooring.mh.views.ChartView.YAxisView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 16/3/28.
 */
public class MonthFragment extends BaseFragment {

    List<String> xDatas = new ArrayList<>();
    List<String> sleep = new ArrayList<>();
    List<String> deep = new ArrayList<>();
    XXChartView month_chart;
    YAxisView month_yAxis;
    List<String> yString;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_month;
    }

    @Override
    protected void initView() {
        float[] test1 = new float[]{5, 6, 7, 8, 9, 10, 12, 8, 6, 4, 2, 2, 2, 8, 9, 10, 11, 12, 7,
                6, 5, 4, 3, 2, 1, 10, 9, 8, 7, 5};
        float[] test2 = new float[]{3, 5, 5, 6, 7, 8, 10, 6, 5, 2, 1, 1, 1, 7, 8, 8, 9, 10, 5, 4,
                3, 2, 1, 1, 0, 8, 7, 6, 5, 0};
        month_chart = (XXChartView) rootView.findViewById(R.id.month_chart);
        month_yAxis = (YAxisView) rootView.findViewById(R.id.month_yAxis);

        for (int i = 1; i <= 30; i++) {
            xDatas.add(i + "");
        }

        yString = new ArrayList<>();
        for (int i = 0; i <= 12; i++) {
            yString.add(i + "h");
        }

        for (int i = 0; i < test1.length; i++) {
            sleep.add(test1[i] + "");
        }
        for (int i = 0; i < test2.length; i++) {
            deep.add(test2[i] + "");
        }

        month_chart.setDatas(xDatas, "2016-05", sleep, deep);

        month_yAxis.setYdatas(yString);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected MachtalkSDKListener setSDKListener() {
        return null;
    }
}
