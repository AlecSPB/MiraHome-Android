package com.mooring.mh.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.activity.MoreActivity;
import com.mooring.mh.views.CircleProgress.CircleDisplay;

/**
 * Created by Will on 16/3/24.
 */
public class WeatherFragment extends BaseFragment implements CircleDisplay.SelectionListener, View.OnClickListener {

    private CircleDisplay circle_progress_1;
    private CircleDisplay circle_progress_2;
    private CircleDisplay circle_progress_3;
    private CircleDisplay circle_progress_4;

    private TextView tv_more;

    private View layout_show;
    private View layout_data_fail;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weather;
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

        tv_more = (TextView) rootView.findViewById(R.id.tv_more);
        tv_more.setOnClickListener(this);

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

        //检测当前用户是否更改

    }

    @Override
    public void onValueUpdate(float value, float maxValue) {
        Log.e("onValueUpdate", "value  " + value + "  maxValue  " + value);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more:
                Intent it = new Intent();
                it.setClass(getActivity(), MoreActivity.class);
                getActivity().startActivity(it);
                break;
        }
    }
}