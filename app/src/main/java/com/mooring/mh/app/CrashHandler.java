package com.mooring.mh.app;

import org.xutils.common.util.LogUtil;

/**
 * 自定义程序异常捕获
 * <p/>
 * Created by Will on 16/6/16.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler instance;//单例

    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (InitApplicationHelper.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    public void init() {  //初始化，把当前对象设置成UncaughtExceptionHandler处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.w("uncaughtException, thread: " + thread + " name: " + thread.getName()
                + " id: " + thread.getId() + "exception: " + ex.toString()
                + " Priority: " + thread.getPriority() + " State: " + thread.getState().toString());
        String threadName = thread.getName();
        if ("sub1".equals(threadName)) {
            LogUtil.d("");
        } else {
            //这里我们可以根据thread name来进行区别对待，同时，我们还可以把异常信息写入文件，以供后来分析。
        }
    }
}
