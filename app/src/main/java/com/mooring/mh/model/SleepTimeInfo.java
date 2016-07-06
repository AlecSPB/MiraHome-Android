package com.mooring.mh.model;

import com.mooring.mh.R;

/**
 * 睡眠时间类
 * <p>
 * Created by Will on 16/4/27.
 */
public class SleepTimeInfo {
    private int type;//类型
    private int time;//小时

    public int getType() {
        return type;
    }

    public int getTime() {
        return time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getColor() {
        int color = -1;
        switch (type) {
            case 0:
                color = R.color.colorPurple;
                break;
            case 1:
                color = R.color.colorOrange;
                break;
            case 2:
                color = android.R.color.holo_green_light;
                break;
            case 3:
                color = android.R.color.holo_blue_light;
                break;
        }
        return color;
    }
}
