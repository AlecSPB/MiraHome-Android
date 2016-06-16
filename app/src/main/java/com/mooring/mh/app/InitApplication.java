package com.mooring.mh.app;

import android.app.Application;

/**
 * 自定义Application类
 * <p/>
 * Created by Will on 16/3/23.
 */
public class InitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        InitApplicationHelper.getInstance().init(this);
    }
}
