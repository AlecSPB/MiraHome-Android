package com.mooring.mh.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.db.DbXUtils;
import com.mooring.mh.utils.MConstants;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * 自定义BaseFragment，支持View预加载，首次展现时数据加载
 * <p/>
 * Created by Will on 16/3/24.
 */
public abstract class BaseFragment extends Fragment {
    protected View rootView;//容器View
    protected FragmentActivity context;
    protected SharedPreferences sp;
    protected SharedPreferences.Editor editor;
    protected DbManager dbManager;//数据库管理者
    protected String deviceId;//设备ID

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();

        sp = InitApplicationHelper.sp;
        editor = sp.edit();
        editor.apply();

        DbManager.DaoConfig dao = DbXUtils.getDaoConfig(context);
        dbManager = x.getDb(dao);

        initFragment();

        if (getLayoutId() == 0) {
            throw new NullPointerException("Layout files can not be empty");
        }
        rootView = inflater.inflate(getLayoutId(), container, false);

        initView();

        return rootView;
    }

    /**
     * 获取布局文件Id
     *
     * @return 布局ID--R.id.main
     */
    protected abstract int getLayoutId();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化Fragment
     */
    protected abstract void initFragment();

    /**
     * 自定义OnResume,自定义的方法在当前fragment不可见状态下不执行
     * 针对,智成云的监听回调和友盟统计
     */
    protected abstract void OnResume();

    /**
     * 自定义OnPause,自定义的方法在当前fragment不可见状态下不执行
     */
    protected abstract void OnPause();

    @Override
    public void onResume() {
        super.onResume();
        deviceId = sp.getString(MConstants.DEVICE_ID, "");
        if (!isVisible()) return;
        OnResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isVisible()) return;
        OnPause();
    }
}
