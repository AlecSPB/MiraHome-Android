package com.mooring.mh.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.xutils.x;

/**
 * 自定义Application 辅助类
 * 初始化程序所有需要默认初始化
 * <p/>
 * Created by Will on 16/3/23.
 */
public class InitApplicationHelper {

    public static Application mApp;
    private static InitApplicationHelper instance;
    public static SharedPreferences sp;

    public static InitApplicationHelper getInstance() {
        if (instance == null) {
            synchronized (InitApplicationHelper.class) {
                if (instance == null) {
                    instance = new InitApplicationHelper();
                }
            }
        }
        return instance;
    }

    public void init(Application app) {
        InitApplicationHelper.mApp = app;
        sp = app.getSharedPreferences("mooring", Context.MODE_PRIVATE);

        x.Ext.init(app);
        x.Ext.setDebug(true);//发布版本时要设置false

        CrashHandler.getInstance().init(mApp.getApplicationContext());

//        MachtalkSDK.getInstance().startSDK(mApp, null);

        //用来更换app key,用来覆盖AndroidManifest中
//        AMapLocationClient.setApiKey("921d6e61c0c6b31c2772adb34bb63e43");
    }

}
