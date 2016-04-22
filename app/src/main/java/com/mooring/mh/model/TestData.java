package com.mooring.mh.model;

/**
 * Created by Will on 16/4/21.
 */
public class TestData {

    private int bgId;//背景图片
    private int iconId;//小图标
    private String title;//标题
    private String data;//显示数据
    private String unit;//单位

    public void setData(String data) {
        this.data = data;
    }

    public void setBgId(int bgId) {
        this.bgId = bgId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getIconId() {

        return iconId;
    }

    public int getBgId() {
        return bgId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    public String getUnit() {
        return unit;
    }

    public void clearData() {
        this.bgId = 0;
        this.iconId = 0;
        this.title = "";
        this.data = "";
        this.unit = "";
    }
}
