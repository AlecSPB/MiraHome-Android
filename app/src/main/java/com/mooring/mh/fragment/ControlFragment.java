package com.mooring.mh.fragment;

import android.content.Intent;
import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.activity.DryingControlActivity;
import com.mooring.mh.activity.HeatingControlActivity;

/**
 * 第二个fragment 负责控制仪器的温度
 * <p>
 * Created by Will on 16/3/24.
 */
public class ControlFragment extends BaseFragment implements View.OnClickListener, SwitchUserObserver {

    private View layout_heating;
    private View layout_drying;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_control;
    }

    @Override
    protected void initFragment() {
    }

    @Override
    protected void initView() {

        layout_heating = rootView.findViewById(R.id.layout_heating);
        layout_drying = rootView.findViewById(R.id.layout_drying);

        layout_heating.setOnClickListener(this);
        layout_drying.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent();
        switch (v.getId()) {
            case R.id.layout_heating:
                it.setClass(context, HeatingControlActivity.class);
                context.startActivity(it);
                break;
            case R.id.layout_drying:
                it.setClass(context, DryingControlActivity.class);
                context.startActivity(it);
                break;
        }
    }

    @Override
    public void onSwitch(String userId, int location, String fTag) {
    }

}

