package com.mooring.mh.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mooring.mh.R;
import com.mooring.mh.utils.MConstants;

import java.util.ArrayList;

/**
 * 编辑闹钟重复天数
 * <p/>
 * Created by Will on 16/5/6.
 */
public class RepeatAlarmActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView imgView_act_right;
    private View layout_mon;
    private View layout_tue;
    private View layout_wed;
    private View layout_tues;
    private View layout_fri;
    private View layout_sat;
    private View layout_sun;
    private TextView tv_mon;
    private TextView tv_tue;
    private TextView tv_wed;
    private TextView tv_tues;
    private TextView tv_fri;
    private TextView tv_sat;
    private TextView tv_sun;
    private ToggleButton tglBtn_mon;
    private ToggleButton tglBtn_tue;
    private ToggleButton tglBtn_wed;
    private ToggleButton tglBtn_tues;
    private ToggleButton tglBtn_fri;
    private ToggleButton tglBtn_sat;
    private ToggleButton tglBtn_sun;
    private TextView tv_confirm;

    private ArrayList<String> repeat;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_repeat_alarm;
    }

    @Override
    protected String getTitleName() {
        return "Repeat alarm";
    }

    @Override
    protected void initActivity() {

        Intent it = getIntent();
        repeat = it.getStringArrayListExtra("repeat");

        initView();

        initData();

    }

    private void initView() {

        imgView_act_right = (ImageView) findViewById(R.id.imgView_act_right);
        imgView_act_right.setVisibility(View.VISIBLE);
        imgView_act_right.setImageResource(R.drawable.bg_repeat_alarm);
        imgView_act_right.setOnClickListener(this);

        layout_mon = findViewById(R.id.layout_mon);
        layout_tue = findViewById(R.id.layout_tue);
        layout_wed = findViewById(R.id.layout_wed);
        layout_tues = findViewById(R.id.layout_tues);
        layout_fri = findViewById(R.id.layout_fri);
        layout_sat = findViewById(R.id.layout_sat);
        layout_sun = findViewById(R.id.layout_sun);

        tv_mon = (TextView) findViewById(R.id.tv_mon);
        tv_tue = (TextView) findViewById(R.id.tv_tue);
        tv_wed = (TextView) findViewById(R.id.tv_wed);
        tv_tues = (TextView) findViewById(R.id.tv_tues);
        tv_fri = (TextView) findViewById(R.id.tv_fri);
        tv_sat = (TextView) findViewById(R.id.tv_sat);
        tv_sun = (TextView) findViewById(R.id.tv_sun);

        tglBtn_mon = (ToggleButton) findViewById(R.id.tglBtn_mon);
        tglBtn_tue = (ToggleButton) findViewById(R.id.tglBtn_tue);
        tglBtn_wed = (ToggleButton) findViewById(R.id.tglBtn_wed);
        tglBtn_tues = (ToggleButton) findViewById(R.id.tglBtn_tues);
        tglBtn_fri = (ToggleButton) findViewById(R.id.tglBtn_fri);
        tglBtn_sat = (ToggleButton) findViewById(R.id.tglBtn_sat);
        tglBtn_sun = (ToggleButton) findViewById(R.id.tglBtn_sun);

        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(this);

        tglBtn_mon.setOnCheckedChangeListener(this);
        tglBtn_tue.setOnCheckedChangeListener(this);
        tglBtn_wed.setOnCheckedChangeListener(this);
        tglBtn_tues.setOnCheckedChangeListener(this);
        tglBtn_fri.setOnCheckedChangeListener(this);
        tglBtn_sat.setOnCheckedChangeListener(this);
        tglBtn_sun.setOnCheckedChangeListener(this);

    }


    private void initData() {
        tglBtn_mon.setChecked(!repeat.get(0).equals("0"));
        tglBtn_tue.setChecked(!repeat.get(1).equals("0"));
        tglBtn_wed.setChecked(!repeat.get(2).equals("0"));
        tglBtn_tues.setChecked(!repeat.get(3).equals("0"));
        tglBtn_fri.setChecked(!repeat.get(4).equals("0"));
        tglBtn_sat.setChecked(!repeat.get(5).equals("0"));
        tglBtn_sun.setChecked(!repeat.get(6).equals("0"));

        changeBg(layout_mon, tv_mon, !repeat.get(0).equals("0"));
        changeBg(layout_tue, tv_tue, !repeat.get(1).equals("0"));
        changeBg(layout_wed, tv_wed, !repeat.get(2).equals("0"));
        changeBg(layout_tues, tv_tues, !repeat.get(3).equals("0"));
        changeBg(layout_fri, tv_fri, !repeat.get(4).equals("0"));
        changeBg(layout_sat, tv_sat, !repeat.get(5).equals("0"));
        changeBg(layout_sun, tv_sun, !repeat.get(6).equals("0"));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgView_act_right) {
            tglBtn_mon.setChecked(true);
            tglBtn_tue.setChecked(true);
            tglBtn_wed.setChecked(true);
            tglBtn_tues.setChecked(true);
            tglBtn_fri.setChecked(true);
            tglBtn_sat.setChecked(true);
            tglBtn_sun.setChecked(true);
        }
        if (v.getId() == R.id.tv_confirm) {
            Intent it = new Intent();
            it.putStringArrayListExtra("repeat", repeat);
            this.setResult(MConstants.REPEAT_ALARM_RESULT, it);
            this.finish();
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
        switch (cb.getId()) {
            case R.id.tglBtn_mon:
                repeat.set(0, isChecked ? "1" : "0");
                changeBg(layout_mon, tv_mon, isChecked);
                break;
            case R.id.tglBtn_tue:
                repeat.set(1, isChecked ? "2" : "0");
                changeBg(layout_tue, tv_tue, isChecked);
                break;
            case R.id.tglBtn_wed:
                repeat.set(2, isChecked ? "3" : "0");
                changeBg(layout_wed, tv_wed, isChecked);
                break;
            case R.id.tglBtn_tues:
                repeat.set(3, isChecked ? "4" : "0");
                changeBg(layout_tues, tv_tues, isChecked);
                break;
            case R.id.tglBtn_fri:
                repeat.set(4, isChecked ? "5" : "0");
                changeBg(layout_fri, tv_fri, isChecked);
                break;
            case R.id.tglBtn_sat:
                repeat.set(5, isChecked ? "6" : "0");
                changeBg(layout_sat, tv_sat, isChecked);
                break;
            case R.id.tglBtn_sun:
                repeat.set(6, isChecked ? "7" : "0");
                changeBg(layout_sun, tv_sun, isChecked);
                break;
        }
    }

    /**
     * 更改选中的颜色和背景
     *
     * @param layout
     * @param tv
     * @param isChecked
     */
    private void changeBg(View layout, TextView tv, boolean isChecked) {
        if (isChecked) {
            tv.setTextColor(getResources().getColor(R.color.colorPurple));
            layout.setBackgroundColor(getResources().getColor(R.color.colorClockItem));
        } else {
            tv.setTextColor(getResources().getColor(R.color.colorPurpleDeep));
            layout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

}
