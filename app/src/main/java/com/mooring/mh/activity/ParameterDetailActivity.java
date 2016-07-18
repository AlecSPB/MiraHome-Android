package com.mooring.mh.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.utils.MUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 展示各种数据详情
 * <p/>
 * Created by Will on 16/4/12.
 */
public class ParameterDetailActivity extends BaseActivity {

    private int number;
    private String param;
    private String title;

    private View detail_title;
    private View layout_detail;
    private TextView tv_detail_param;
    private TextView tv_detail_unit;
    private ImageView imgView_detail_ic;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_parameter_detail;
    }

    @Override
    protected String getTitleName() {
        return getString(R.string.tv_heart_rate);
    }

    @Override
    protected void initActivity() {
        Intent it = getIntent();
        number = it.getIntExtra("number", -1);
        param = it.getStringExtra("param");
        title = it.getStringExtra("title");
    }

    @Override
    protected void initView() {
        detail_title = findViewById(R.id.detail_title);
        layout_detail = findViewById(R.id.layout_detail);
        tv_detail_param = (TextView) findViewById(R.id.tv_detail_param);
        tv_detail_unit = (TextView) findViewById(R.id.tv_detail_unit);
        imgView_detail_ic = (ImageView) findViewById(R.id.imgView_detail_ic);

        detail_title.setBackgroundColor(getResources().getColor(R.color.transparent_4));
        tv_act_title.setText(title);
        tv_detail_param.setText(param);
        switch (number) {
            case 0:
                layout_detail.setBackgroundResource(R.drawable.bg_heart_rate);
                imgView_detail_ic.setImageResource(R.drawable.ic_heart_rate);
                tv_detail_unit.setText(getString(R.string.unit_bmp));
                break;
            case 1:
                layout_detail.setBackgroundResource(R.drawable.bg_breathing_rate);
                imgView_detail_ic.setImageResource(R.drawable.ic_breathing_rate);
                tv_detail_unit.setText(getString(R.string.unit_min));
                break;
            case 2:
                layout_detail.setBackgroundResource(R.drawable.bg_body_movement);
                imgView_detail_ic.setImageResource(R.drawable.ic_body_movement);
                tv_detail_unit.setVisibility(View.GONE);
                break;
            case 3:
                layout_detail.setBackgroundResource(R.drawable.bg_humidity_layout);
                imgView_detail_ic.setImageResource(R.drawable.ic_humidity_icon);
                tv_detail_unit.setText(getString(R.string.unit_percent));
                break;
            case 4:
                layout_detail.setBackgroundResource(R.drawable.bg_temperature_layout);
                imgView_detail_ic.setImageResource(R.drawable.ic_temperature);
                tv_detail_unit.setText(MUtils.getCurrTempUnit() ?
                        getString(R.string.unit_celsius) : getString(R.string.unit_fahrenheit));
                break;
            case 5:
                layout_detail.setBackgroundResource(R.drawable.bg_bed_temperature);
                imgView_detail_ic.setImageResource(R.drawable.ic_bed_temperature);
                tv_detail_unit.setText(MUtils.getCurrTempUnit()  ?
                        getString(R.string.unit_celsius) : getString(R.string.unit_fahrenheit));
                break;
            case 6:
                layout_detail.setBackgroundResource(R.drawable.bg_light_layout);
                imgView_detail_ic.setImageResource(R.drawable.ic_light);
                tv_detail_unit.setVisibility(View.GONE);
                break;
            case 7:
                layout_detail.setBackgroundResource(R.drawable.bg_noise_layout);
                imgView_detail_ic.setImageResource(R.drawable.ic_noise);
                tv_detail_unit.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void OnClick(View v) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ParameterDetail");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ParameterDetail");
        MobclickAgent.onPause(this);
    }
}
