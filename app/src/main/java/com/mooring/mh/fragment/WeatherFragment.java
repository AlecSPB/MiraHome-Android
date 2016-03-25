package com.mooring.mh.fragment;

import android.util.Log;

import com.mooring.mh.R;
import com.mooring.mh.views.other.CircleDisplay;

/**
 * Created by Will on 16/3/24.
 */
public class WeatherFragment extends BaseFragment implements CircleDisplay.SelectionListener {

    private CircleDisplay circle_progress_1;
    private CircleDisplay circle_progress_2;
    private CircleDisplay circle_progress_3;
    private CircleDisplay circle_progress_4;


    @Override
    protected int getLayoutId() {
        return R.layout.test;
    }

    @Override
    protected void initView() {

        circle_progress_1 = (CircleDisplay) rootView.findViewById(R.id.circle_progress_1);
        circle_progress_2 = (CircleDisplay) rootView.findViewById(R.id.circle_progress_2);
        circle_progress_3 = (CircleDisplay) rootView.findViewById(R.id.circle_progress_3);
        circle_progress_4 = (CircleDisplay) rootView.findViewById(R.id.circle_progress_4);


        setmCircleDisplay(circle_progress_1, 66f);
        setmCircleDisplay(circle_progress_2, 20f);
        setmCircleDisplay(circle_progress_3, 68f);
        setmCircleDisplay(circle_progress_4, 90f);

    }

    private void setmCircleDisplay(CircleDisplay c, float value) {
        c.setFormatDigits(0);
        c.setAnimDuration(3000);
        c.setSelectionListener(this);
        c.setUnit("%");
        c.showValue(value, 100f, true);

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onValueUpdate(float value, float maxValue) {
        Log.e("onValueUpdate", "value  " + value + "  maxValue  " + value);
    }

}
