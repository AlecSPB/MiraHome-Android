package com.mooring.mh.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mooring.mh.R;
import com.mooring.mh.activity.MoreActivity;
import com.mooring.mh.views.CircleImageView;
import com.mooring.mh.views.other.CircleDisplay;

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

    private ImageView imgView_title_menu;
    private CircleImageView circleImg_left;
    private CircleImageView circleImg_right;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initView() {

        circleImg_left = (CircleImageView) rootView.findViewById(R.id.circleImg_left);
        circleImg_right = (CircleImageView) rootView.findViewById(R.id.circleImg_right);


        circleImg_left.setOnClickListener(this);
        circleImg_right.setOnClickListener(this);

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
            case R.id.circleImg_left:
                break;
            case R.id.circleImg_right:

                break;
        }
    }

}
