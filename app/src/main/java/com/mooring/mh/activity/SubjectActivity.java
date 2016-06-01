package com.mooring.mh.activity;

import android.support.v4.app.FragmentActivity;

import com.mooring.mh.fragment.SwitchUserObserver;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象主题角色,观察者模式中被观察者
 * <p>
 * Created by Will on 16/5/31.
 */
public abstract class SubjectActivity extends FragmentActivity {
    /**
     * 用来保存注册的观察者对象
     */
    private List<SwitchUserObserver> list = new ArrayList<>();

    /**
     * 注册观察者对象
     *
     * @param switchUserObserver 观察者对象
     */
    public void attach(SwitchUserObserver switchUserObserver) {

        list.add(switchUserObserver);
        LogUtil.i("  Attached an SwitchUserObserver  ");
    }

    /**
     * 删除观察者对象
     *
     * @param switchUserObserver 观察者对象
     */
    public void detach(SwitchUserObserver switchUserObserver) {

        list.remove(switchUserObserver);

        LogUtil.i("  Detach an SwitchUserObserver  ");
    }

    /**
     * 通知所有注册的观察者对象
     */
    public void notifyObservers(String userId, int location, String fTag) {

        for (SwitchUserObserver switchUserObserver : list) {
            switchUserObserver.onSwitch(userId, location, fTag);
        }
    }
}
