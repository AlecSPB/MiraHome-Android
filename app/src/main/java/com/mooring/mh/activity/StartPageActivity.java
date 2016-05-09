package com.mooring.mh.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;

import org.xutils.common.util.LogUtil;

/**
 * 起始页--执行动画,判断是否首次使用,是否自动登录
 * <p/>
 * Created by Will on 16/3/24.
 */
public class StartPageActivity extends AppCompatActivity {

    protected Context context = null;
    private MachtalkSDKListener baseListener;//自定义SDK回调监听
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String userName;//用户名
    private String userPwd;//密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_startpage);

        sp = InitApplicationHelper.sp;
        editor = sp.edit();
        MachtalkSDK.getInstance().startSDK(this, null);
        baseListener = new BaseListener();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {

              /*      startActivity(new Intent(context, MainActivity.class));
                    overridePendingTransition(R.anim.fade_big_in, 0);
                    StartPageActivity.this.finish();*/


                    if (sp.getBoolean("appFirstStart", true)) {
                        // 引导页
                        startActivity(new Intent(context, GuidePageActivity.class));
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        StartPageActivity.this.finish();
                    } else {
                        // 自动登录
                        userName = sp.getString(MConstants.SP_KEY_USERNAME, "");
                        userPwd = sp.getString(MConstants.SP_KEY_PASSWORD, "");

                        Log.e("StartPageActivity", "userName  " + userName + "  userPwd  " + userPwd);

                        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd)) {
                            MachtalkSDK.getInstance().userLogin(userName, userPwd, null);
                        } else {
                            startActivity(new Intent(context, LoginAndSignUpActivity.class));

                            StartPageActivity.this.finish();
                            overridePendingTransition(0, R.anim.fade_big_out);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }


    /**
     * 自定义回调监听
     */
    class BaseListener extends MachtalkSDKListener {
        @Override
        public void onServerConnectStatusChanged(MachtalkSDKConstant.ServerConnStatus serverConnStatus) {
            super.onServerConnectStatusChanged(serverConnStatus);
            if (serverConnStatus == MachtalkSDKConstant.ServerConnStatus.LOGOUT_KICKOFF) {
                StartPageActivity.this.finish();
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
                editor.commit();

                LogUtil.i("store username: " + userName + " password: " + userPwd);

                startActivity(new Intent(context, MainActivity.class));
            } else {
                if (errMsg == null) {
                    errMsg = getResources().getString(R.string.network_exception);
                }
                CommonUtils.showToast(context, errMsg);
                startActivity(new Intent(context, LoginAndSignUpActivity.class));
            }
//            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            overridePendingTransition(R.anim.fade_big_in, 0);
            StartPageActivity.this.finish();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(this);
        MachtalkSDK.getInstance().setSdkListener(baseListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(baseListener);
    }

    @Override
    protected void onDestroy() {
        MachtalkSDK.getInstance().removeSdkListener(baseListener);
        super.onDestroy();
        System.gc();
    }

}
