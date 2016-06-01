package com.mooring.mh.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.db.DbXUtils;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * 自定义BaseFragment，支持View预加载，首次展现时数据加载
 * <p>
 * Created by Will on 16/3/24.
 */
public abstract class BaseFragment extends Fragment {
    protected View rootView;
    protected Activity context;
    private BaseListener baseListener;
    protected SharedPreferences sp;
    protected SharedPreferences.Editor editor;
    protected DbManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();

        sp = InitApplicationHelper.sp;
        editor = sp.edit();
        editor.apply();

        DbManager.DaoConfig dao = DbXUtils.getDaoConfig(context);
        dbManager = x.getDb(dao);

        MachtalkSDK.getInstance().setContext(context);
        baseListener = new BaseListener();

        initFragment();

        if (getLayoutId() == 0) {
            throw new NullPointerException("Layout files can not be empty");
        }
        rootView = inflater.inflate(getLayoutId(), container, false);

        initView();

        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            MachtalkSDK.getInstance().setContext(context);
            MachtalkSDK.getInstance().setSdkListener(baseListener);
        } else {
            MachtalkSDK.getInstance().removeSdkListener(baseListener);
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
     * 初始化Fragment
     */
    protected abstract void initFragment();

    class BaseListener extends MachtalkSDKListener {
        @Override
        public void onServerConnectStatusChanged(MachtalkSDKConstant.ServerConnStatus scs) {
            super.onServerConnectStatusChanged(scs);
            if (scs == MachtalkSDKConstant.ServerConnStatus.LOGOUT_KICKOFF) {
                context.finish();
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(context);
        MachtalkSDK.getInstance().setSdkListener(baseListener);
    }

    @Override
    public void onDestroy() {
        MachtalkSDK.getInstance().removeSdkListener(baseListener);
        super.onDestroy();
        System.gc();
    }
}
