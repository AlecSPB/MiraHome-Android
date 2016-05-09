package com.mooring.mh.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.views.AlarmDaySelectView;
import com.mooring.mh.views.WheelPicker.AbstractWheelPicker;
import com.mooring.mh.views.WheelPicker.widget.WheelTimePicker;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.TimeZone;

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


        LogUtil.e("______" + getCurrentTimeZone());

        Intent it = getIntent();

        flag = it.getStringExtra("flag");
        if ("edit".equals(flag)) {
            time = it.getStringExtra("time");
            repeat = CommonUtils.ParsingDay(it.getStringExtra("repeat"));
            smart = it.getBooleanExtra("smart", false);
            set = it.getBooleanExtra("set", false);
            position = it.getIntExtra("position", -1);
        } else {
            repeat = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                repeat.add("0");
            }
            time = CommonUtils.getCurrTime("HHmm");
            smart = true;
            set = true;
        }

        initView();

        initData();
    }

    private void initView() {

        imgView_act_right = (ImageView) findViewById(R.id.imgView_act_right);
        layout_repeat = findViewById(R.id.layout_repeat);
        asv_alarm_edit = (AlarmDaySelectView) findViewById(R.id.asv_alarm_edit);
        tglBtn_alarm_smart = (ToggleButton) findViewById(R.id.tglBtn_alarm_smart);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        time_picker = (WheelTimePicker) findViewById(R.id.time_picker);
        if ("edit".equals(flag)) {
            imgView_act_right.setVisibility(View.VISIBLE);
            imgView_act_right.setOnClickListener(this);
        }
        layout_repeat.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        tglBtn_alarm_smart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                smart = isChecked;
            }
        });

    }


    private void initData() {
        time_picker.setTextColor(getResources().getColor(R.color.colorWhite50));
        time_picker.setCurrentTextColor(getResources().getColor(R.color.colorPurple));
        time_picker.setTextSize(CommonUtils.sp2px(this, 35));
        time_picker.setItemSpace(CommonUtils.dp2px(this, 40));
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

    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        LogUtil.e("______" + tz.getRawOffset());
        return createGmtOffsetString(true, true, tz.getRawOffset());
    }

    public static String createGmtOffsetString(boolean includeGmt,
                                               boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
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
    protected MachtalkSDKListener setSDKListener() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MConstants.REPEAT_ALARM_REQUEST && resultCode == MConstants.REPEAT_ALARM_RESULT) {
            repeat = data.getStringArrayListExtra("repeat");
            asv_alarm_edit.setTvData(repeat);
        }
    }
}
