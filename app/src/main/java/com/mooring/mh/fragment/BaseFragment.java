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
    protected boolean isVisible;
    public View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(getLayoutId(), container, false);

        initView();

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void lazyLoad();
}
