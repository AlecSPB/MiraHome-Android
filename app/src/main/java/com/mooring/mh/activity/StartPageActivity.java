package com.mooring.mh.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.BuildConfig;
import com.mooring.mh.R;
import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 起始页--执行动画,判断是否首次使用,是否自动登录
 * <p/>
 * Created by Will on 16/3/24.
 */
public class StartPageActivity extends AppCompatActivity {

    private ImageView img_start;
    private AlphaAnimation inAnimation;
    private AppCompatActivity context = null;
    private MachtalkSDKListener baseListener;//自定义SDK回调监听
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String userName;//用户名
    private String userPwd;//密码

    private String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        sp = InitApplicationHelper.sp;
        editor = sp.edit();
        editor.apply();

        //设定Log输出等级以及输出到本地文件
        MachtalkSDK.getInstance().setLog(MachtalkSDKConstant.LOG_LEVEL.LOG_LEVEL_ALL, BuildConfig.LOG_DEBUG);
        MachtalkSDK.getInstance().startSDK(context, null);

        baseListener = new BaseListener();

        //如果不调用此方法，会导致按照"几天不活跃"条件来推送失效，
        PushAgent.getInstance(context).onAppStart();

        //友盟统计配置
        MobclickAgent.setDebugMode(BuildConfig.LOG_DEBUG);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.enableEncrypt(true);//日志加密

        //执行动画切换
        executeAnimation();

    }

    /**
     * 执行切换动画
     */
    private void executeAnimation() {

        setContentView(R.layout.activity_startpage);
        img_start = (ImageView) findViewById(R.id.img_start);

        inAnimation = new AlphaAnimation(0.05f, 1.0f);
        inAnimation.setDuration(1000);
        inAnimation.setRepeatCount(0);
        inAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_start.setVisibility(View.VISIBLE);
                applyForPermission();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                img_start.startAnimation(inAnimation);
            }
        }, 1500);
    }

    /**
     * 申请权限
     */
    private void applyForPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = findDeniedPermissions(PERMISSION);
            if (permissions.size() > 0) {
                this.requestPermissions(permissions.toArray(new String[permissions.size()]),
                        MConstants.PERMISSIONS_STORAGE);
                return;
            }
        }
        executeMethod();
    }

    /**
     * 执行方法
     */
    private void executeMethod() {
        if (sp.getBoolean(MConstants.SP_KEY_FIRST_START, true)) {
            // 引导页
            startActivity(new Intent(context, GuidePageActivity.class));
            context.finish();
        } else {
            // 自动登录
            userName = sp.getString(MConstants.SP_KEY_USERNAME, "");
            userPwd = sp.getString(MConstants.SP_KEY_PASSWORD, "");

            LogUtil.e("userName  " + userName + "  userPwd  " + userPwd);

            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd) &&
                    sp.getBoolean(MConstants.HAS_LOCAL_USER, false)) {
                MUtils.showLoadingDialog(context);
                MachtalkSDK.getInstance().userLogin(userName, userPwd, null);
            } else {
                startActivity(new Intent(context, LoginAndSignUpActivity.class));
                context.finish();
            }
        }
    }

    /**
     * 检测权限
     *
     * @param permissions
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permissions) {
            if (this.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MConstants.PERMISSIONS_STORAGE) {
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (deniedPermissions.size() > 0) {
                //需要继续设置
                if (deniedPermissions.get(0).equals(PERMISSION[0])) {
                    MUtils.showGoSettingDialog(this, getString(R.string.permission_storage));
                }
                if (deniedPermissions.get(0).equals(PERMISSION[1])) {
                    MUtils.showGoSettingDialog(this, getString(R.string.permission_location));
                }
                if (deniedPermissions.get(0).equals(PERMISSION[2])) {
                    MUtils.showGoSettingDialog(this, getString(R.string.permission_phone));
                }
            } else {
                //赋予了权限
                executeMethod();
            }
        }
    }


    /**
     * 自定义回调监听
     */
    class BaseListener extends MachtalkSDKListener {
        @Override
        public void onServerConnectStatusChanged(MachtalkSDKConstant.ServerConnStatus serverConnStatus) {
            super.onServerConnectStatusChanged(serverConnStatus);
            if (serverConnStatus == MachtalkSDKConstant.ServerConnStatus.LOGOUT_KICKOFF) {
                context.finish();
                return;
            }
        }

        @Override
        public void onUserLogin(Result result, String user) {
            int success = Result.FAILED;
            String errMsg = null;
            if (result != null) {
                success = result.getSuccess();
                errMsg = result.getErrorMessage();
            }
            if (success == Result.SUCCESS) {
                editor.putString(MConstants.SP_KEY_USERNAME, userName);
                editor.putString(MConstants.SP_KEY_PASSWORD, userPwd);
                editor.apply();

                LogUtil.i("store username: " + userName + " password: " + userPwd);

                startActivity(new Intent(context, MainActivity.class));
            } else {
                if (errMsg == null) {
                    errMsg = getResources().getString(R.string.network_exception);
                }
                MUtils.showToast(context, errMsg);
                startActivity(new Intent(context, LoginAndSignUpActivity.class));
            }
            MUtils.hideLoadingDialog();
            context.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(baseListener);
        MobclickAgent.onPageStart("StartPage");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(baseListener);
        MobclickAgent.onPageEnd("StartPage");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        MachtalkSDK.getInstance().removeSdkListener(baseListener);
        super.onDestroy();
    }
}
