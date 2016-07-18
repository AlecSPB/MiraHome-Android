package com.mooring.mh.activity;

import com.mooring.mh.fragment.SwitchUserObserver;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象主题角色,观察者模式中被观察者
 * <p/>
 * Created by Will on 16/5/31.
 */
public abstract class SubjectActivity extends BaseActivity {
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
     *
     * @param userId   用户id,针对ControlFragment,只有切换头像时不为""
     * @param location 用户左右位置,-1针对温度单位切换,-2针对设备上下线
     * @param fTag     状态参数
     */
    public void notifyObservers(String userId, int location, String fTag) {
        for (SwitchUserObserver switchUserObserver : list) {
            switchUserObserver.onSwitch(userId, location, fTag);
        }
    }
}
