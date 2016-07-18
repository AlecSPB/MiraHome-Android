package com.mooring.mh.app;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.mooring.mh.BuildConfig;
import com.mooring.mh.R;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

/**
 * 自定义Application 辅助类
 * 初始化程序所有需要默认初始化
 * <p/>
 * Created by Will on 16/3/23.
 */
public class InitApplicationHelper {

    public static Application mApp;
    public static InitApplicationHelper instance;
    public static SharedPreferences sp;
    private PushAgent mPushAgent;

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
        sp = app.getSharedPreferences("mooring", Context.MODE_PRIVATE);//实例化

        //xUtils初始化
        x.Ext.init(app);
        x.Ext.setDebug(BuildConfig.LOG_DEBUG);//发布版本时要设置false

        //捕获异常初始化
        CrashHandler.getInstance().init();

        //注册推送
        mPushAgent = PushAgent.getInstance(mApp);
        mPushAgent.setDebugMode(BuildConfig.LOG_DEBUG);

        //自定义推送样式
        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                                R.layout.custom_notification_layout);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(R.drawable.umeng_push_notification_default)
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        LogUtil.w("_ticker:" + msg.ticker + "_icon:" + msg.icon + "_title:" +
                                msg.title + "_text:" + msg.text + "_largeIcon:" + msg.largeIcon +
                                "_img:" + msg.img + "_activity:" + msg.activity + "_display_type:" +
                                msg.display_type + "_message_id" + msg.message_id + "_msg_id" + msg.msg_id);
                        return builder.build();

                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);//自定义推送界面
    }
}
