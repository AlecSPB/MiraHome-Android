package com.mooring.mh.fragment;

import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.views.other.GiftRainView;

/**
 * Created by Will on 16/3/24.
 */
public class ParameterFragment extends BaseFragment {

    private GiftRainView giftRainView;
    private boolean isStart;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_parameter;
    }

    @Override
    protected void initView() {
        giftRainView = (GiftRainView) rootView.findViewById(R.id.giftRainView);


        giftRainView.setImages(R.mipmap.ico_gold_money,R.mipmap.ico_money,R.mipmap.ic_launcher);

        giftRainView.startRain();
        isStart = true;

        giftRainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    giftRainView.startRain();
                    isStart = true;
                } else {
                    giftRainView.stopRainDely();
                    isStart = false;
                }
            }
        });
    }


    @Override
    protected void lazyLoad() {

    }
}
