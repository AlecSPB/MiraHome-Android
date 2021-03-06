package com.mooring.mh.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mooring.mh.R;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.mooring.mh.views.WheelPicker.AbstractWheelPicker;
import com.mooring.mh.views.WheelPicker.widget.WheelTimePicker;
import com.mooring.mh.views.other.AlarmDaySelectView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 闹钟编辑和添加Activity
 * <p/>
 * Created by Will on 16/5/5.
 */
public class AlarmEditActivity extends BaseActivity {

    private WheelTimePicker time_picker;
    private ImageView imgView_act_right;
    private View layout_repeat;
    private AlarmDaySelectView asv_alarm_edit;
    private ToggleButton tglBtn_alarm_smart;
    private TextView tv_confirm;

    private String flag;//编辑/添加 标志
    private String time;//时间
    private ArrayList<String> repeat;//重复
    private boolean smart;//只能开关
    private boolean set;//闹钟开关
    private int position = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_edit;
    }

    @Override
    protected String getTitleName() {
        return getString(R.string.title_alarm_edit);
    }

    @Override
    protected void initActivity() {
        Intent it = getIntent();
        flag = it.getStringExtra("flag");
        if ("edit".equals(flag)) {
            time = it.getStringExtra("time");
            repeat = MUtils.ParsingDay(it.getStringExtra("repeat"));
            smart = it.getBooleanExtra("smart", false);
            set = it.getBooleanExtra("set", false);
            position = it.getIntExtra("position", -1);
        } else {
            repeat = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                repeat.add("0");
            }
            time = MUtils.getCurrTime("HHmm");
            smart = true;
            set = true;
        }
    }

    @Override
    protected void initView() {
        imgView_act_right = (ImageView) findViewById(R.id.imgView_act_right);
        layout_repeat = findViewById(R.id.layout_repeat);
        asv_alarm_edit = (AlarmDaySelectView) findViewById(R.id.asv_alarm_edit);
        tglBtn_alarm_smart = (ToggleButton) findViewById(R.id.tglBtn_alarm_smart);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        time_picker = (WheelTimePicker) findViewById(R.id.time_picker);
        if ("edit".equals(flag)) {
            imgView_act_right.setVisibility(View.VISIBLE);
            imgView_act_right.setOnClickListener(this);
        } else {
            tv_act_title.setText(getString(R.string.title_alarm_add));
        }
        layout_repeat.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        tglBtn_alarm_smart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                smart = isChecked;
            }
        });

        initData();
    }

    private void initData() {
        time_picker.setTextColor(getResources().getColor(R.color.colorWhite50));
        time_picker.setCurrentTextColor(getResources().getColor(R.color.colorPurple));
        time_picker.setTextSize(MUtils.sp2px(this, 35));
        time_picker.setItemSpace(MUtils.dp2px(this, 40));
        time_picker.setCurrentData(time.substring(0, 2), time.substring(2));
        time_picker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {
            }

            @Override
            public void onWheelSelected(View view, int index, String data) {
                time = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (state != AbstractWheelPicker.SCROLL_STATE_IDLE) {
                    tv_confirm.setEnabled(false);
                } else {
                    tv_confirm.setEnabled(true);
                }
            }
        });

        asv_alarm_edit.setTvData(repeat);
        tglBtn_alarm_smart.setChecked(smart);
    }

    @Override
    protected void OnClick(View v) {
        Intent it = new Intent();
        switch (v.getId()) {
            case R.id.imgView_act_right:
                it.putExtra("position", position);
                it.putExtra("flag", "delete");
                this.setResult(MConstants.ALARM_EDIT_RESULT, it);
                this.finish();
                break;
            case R.id.layout_repeat:
                it.putExtra("repeat", repeat);
                it.putStringArrayListExtra("repeat", repeat);
                it.setClass(AlarmEditActivity.this, RepeatAlarmActivity.class);
                startActivityForResult(it, MConstants.REPEAT_ALARM_REQUEST);
                break;
            case R.id.tv_confirm:
                it.putExtra("flag", flag);
                it.putStringArrayListExtra("repeat", repeat);
                it.putExtra("time", time);
                it.putExtra("smart", smart);
                it.putExtra("set", set);
                it.putExtra("position", position);
                this.setResult(MConstants.ALARM_EDIT_RESULT, it);
                this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MConstants.REPEAT_ALARM_REQUEST &&
                resultCode == MConstants.REPEAT_ALARM_RESULT) {
            repeat = data.getStringArrayListExtra("repeat");
            asv_alarm_edit.setTvData(repeat);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AlarmEdit");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AlarmEdit");
        MobclickAgent.onPause(this);
    }
}
