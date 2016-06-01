package com.mooring.mh.fragment;

/**
 * 当前User改变,观察者
 * <p>
 * Created by Will on 16/5/31.
 */
public interface SwitchUserObserver {

    /**
     * 更新接口
     *
     * @param userId        最新的User
     * @param location 最新的位置
     * @param fTag     fragment对应的名称
     */
    void onSwitch(String userId, int location, String fTag);
}
