package com.mooring.mh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义BaseFragment，支持View预加载，首次展现时数据加载
 * <p/>
 * Created by Will on 16/3/24.
 */
public abstract class BaseFragment extends Fragment {
    public View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() == 0) {
            throw new NullPointerException();
        }
        rootView = inflater.inflate(getLayoutId(), container, false);

        initView();

        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            lazyLoad();
        }
    }

    /**
     * 获取布局文件Id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 每次展现的时候执行,但是除此展现不会执行(展现时执行)
     */
    protected abstract void lazyLoad();
}
