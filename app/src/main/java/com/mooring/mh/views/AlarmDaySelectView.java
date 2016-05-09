package com.mooring.mh.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 16/5/6.
 */
public class AlarmDaySelectView extends LinearLayout {

    private TextView tv;
    private int normalColor;//正常颜色
    private int selectColor;//选中颜色
    private int textSize;
    private String[] text;//文本
    private List<TextView> tvList;

    public AlarmDaySelectView(Context context) {
        this(context, null);
    }

    public AlarmDaySelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlarmDaySelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {

        setHorizontalGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);

        text = new String[]{
                getResources().getString(R.string.tv_monday),
                getResources().getString(R.string.tv_tuesday),
                getResources().getString(R.string.tv_wednesday),
                getResources().getString(R.string.tv_thursday),
                getResources().getString(R.string.tv_friday),
                getResources().getString(R.string.tv_saturday),
                getResources().getString(R.string.tv_sunday)
        };

        normalColor = getResources().getColor(R.color.colorWhite50);
        selectColor = getResources().getColor(R.color.colorPurple);
        textSize = CommonUtils.sp2px(getContext(), 5);

        tvList = new ArrayList<>();
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.rightMargin = CommonUtils.dp2px(getContext(), 10);
        for (int i = 0; i < text.length; i++) {
            tv = new TextView(getContext());
            tv.setPadding(0, 0, 0, 0);
            tv.setText(text[i]);
            tv.setTextColor(normalColor);
            tv.setTextSize(textSize);
            tvList.add(tv);
            addView(tv, lp);
        }
    }

    /**
     * 改变字体颜色
     *
     * @param data
     */
    public void setTvData(List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            tvList.get(i).setTextColor(!"0".equals(data.get(i)) ? selectColor : normalColor);
        }
        invalidate();
    }

    /**
     * 设置默认颜色
     *
     * @param normalColor R.color.colorPurple
     */
    public void setNormalColor(int normalColor) {
        this.normalColor = getResources().getColor(normalColor);
    }

    /**
     * 设置选中颜色值
     *
     * @param selectColor R.color.colorPurple
     */
    public void setSelectColor(int selectColor) {
        this.selectColor = getResources().getColor(selectColor);
        ;
    }

    /**
     * 设置显示文本
     *
     * @param text
     */
    public void setText(String[] text) {
        this.text = text;
    }

    /**
     * 设置字体大小,当前数值为5
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = CommonUtils.sp2px(getContext(), textSize);
    }
}
