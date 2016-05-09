package com.mooring.mh.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.mooring.mh.app.InitApplicationHelper;

import org.xutils.common.util.LogUtil;

/**
 * 自定义BaseFragment，支持View预加载，首次展现时数据加载
 * <p/>
 * Created by Will on 16/3/24.
 */
public abstract class BaseFragment extends Fragment {
    protected View rootView;
    protected Activity context;
    protected SharedPreferences sp;
    protected SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        if (getLayoutId() == 0) {
            throw new NullPointerException();
        }
        rootView = inflater.inflate(getLayoutId(), container, false);

        sp = InitApplicationHelper.sp;
        editor = sp.edit();

        initView();

        MachtalkSDK.getInstance().setContext(context);
        MachtalkSDK.getInstance().setSdkListener(setSDKListener());

        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            MachtalkSDK.getInstance().setContext(context);
            MachtalkSDK.getInstance().setSdkListener(setSDKListener());
            lazyLoad();
        } else {
            MachtalkSDK.getInstance().removeSdkListener(setSDKListener());
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

    protected abstract MachtalkSDKListener setSDKListener();

    @Override
    public void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(context);
        MachtalkSDK.getInstance().setSdkListener(setSDKListener());
        LogUtil.e("智成云监听此时为null    " + (setSDKListener() == null));
    }

    @Override
    public void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(setSDKListener());
        LogUtil.e("智成云监听此时为null    " + (setSDKListener() == null));
    }

}
