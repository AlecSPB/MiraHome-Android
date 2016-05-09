package com.mooring.mh.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 获取手机验证码
 * <p/>
 * 1.从忘记密码跳转过来
 * 2.从注册页面条转过来
 * Created by Will on 16/3/30.
 */
public class VerifyPhoneActivity extends BaseActivity {
    private TextView tv_verify_phone;//手机号
    private EditText edit_verify_code;//验证码
    private TextView tv_wait_times;//等待时间
    private TextView tv_verify_send;//发送按钮
    private TextView tv_verify_confirm;//验证按钮
    private TextView tv_verify_error;//错误提示

    private int entrance_flag; //跳转入口标志,重置密码 或 注册用户
    private String phone;//手机号码
    private String psw;//密码
    private String sms_code;//验证码

    private int time = 60;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verify_phone;
    }

    @Override
    protected String getTitleName() {
        return getResources().getString(R.string.title_verify_phone);
    }

    @Override
    protected void initActivity() {

        Intent it = getIntent();
        entrance_flag = it.getIntExtra(MConstants.ENTRANCE_FLAG, 0);
        phone = it.getStringExtra(MConstants.SP_KEY_USERNAME);

        if (entrance_flag == MConstants.SIGN_UP_SUCCESS) {
            psw = it.getStringExtra(MConstants.SP_KEY_PASSWORD);
        }

        initView();

        initData();

    }

    private void initView() {
        tv_verify_phone = (TextView) findViewById(R.id.tv_verify_phone);
        edit_verify_code = (EditText) findViewById(R.id.edit_verify_code);
        tv_wait_times = (TextView) findViewById(R.id.tv_wait_times);
        tv_verify_send = (TextView) findViewById(R.id.tv_verify_send);
        tv_verify_confirm = (TextView) findViewById(R.id.tv_verify_confirm);
        tv_verify_error = (TextView) findViewById(R.id.tv_verify_error);

        tv_verify_send.setOnClickListener(this);
        tv_verify_confirm.setOnClickListener(this);
    }

    private void initData() {
        tv_verify_phone.setText(phone);

    }

    /**
     * 发送消息
     */
    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String ts = "Please wait " + time + "s";
            tv_wait_times.setText(ts);
            time--;
            if (time < 0) {
                tv_verify_send.setEnabled(true);
                tv_verify_send.setBackgroundColor(getResources().getColor(R.color.colorPurple50));
                tv_verify_send.setTextColor(getResources().getColor(R.color.colorWhite));
                clearTimer();
                time = 60;
            }
        }
    };

    /**
     * 清除计时
     */
    private void clearTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 初始化计时
     */
    private void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                han.sendEmptyMessage(0x01);
            }
        };
    }

    /**
     * 改变发送验证码按钮的状态
     */
    private void changeIdentifyButton() {
        initTimer();
        timer.schedule(timerTask, 0, 1000);
        tv_verify_send.setEnabled(false);
        tv_verify_send.setText(getResources().getString(R.string.btn_tv_resend));
        tv_verify_send.setBackgroundColor(getResources().getColor(R.color.colorPurple50));
        tv_verify_send.setTextColor(getResources().getColor(R.color.colorWhite50));
    }

    /**
     * 发送获取验证码
     */
    private void sendIdentify() {

        changeIdentifyButton();

        RequestParams params = CommonUtils.getBaseParams(MConstants.SMS_CODE);
        params.addParameter("mobile_phone", phone);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null) {
                    CommonUtils.showToast(VerifyPhoneActivity.this, result.toString());

                    //成功之后进行保存验证码  sms_code
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(ex.getMessage(), ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(cex.getMessage(), cex);
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished");
            }
        });
    }

    /**
     * 执行注册
     */
    private void executeSignUp() {

        RequestParams params = CommonUtils.getBaseParams(MConstants.MOBILE_PHONE_USER);
        params.addParameter("password", psw);
        params.addHeader("mobile_phone", phone);
        params.addParameter("sms_code", sms_code);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null) {


                    //注册成功,跳转成功展示界面

//                    showSuccess();


                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(ex.getMessage(), ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(cex.getMessage(), cex);
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished");
            }
        });
    }

    /**
     * 执行重置密码
     */
    private void executeResetPassword() {
        RequestParams params = CommonUtils.getBaseParams(MConstants.SEND_RESET_PASSWORD_SMS_CODE);
        params.addParameter("mobile_phone", phone);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null) {


                    //重置密码获取验证码成功,回转修改界面

//                    returnToConfirmation();


                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(ex.getMessage(), ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(cex.getMessage(), cex);
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished");
            }
        });
    }

    /**
     * 跳转注册成功页
     */
    private void showSuccess() {
        Intent it = new Intent();
        it.setClass(VerifyPhoneActivity.this, CommonSuccessActivity.class);
        it.putExtra(MConstants.ENTRANCE_FLAG, MConstants.SIGN_UP_SUCCESS);
        startActivity(it);
    }

    /**
     * 回转到到输入新密码页
     */
    private void returnToConfirmation() {
        Intent it = new Intent();
        it.putExtra("isReturn", true);
        it.putExtra("verify_code", sms_code);
        this.setResult(MConstants.SIGN_UP_SUCCESS, it);
        this.finish();
    }

    @Override
    protected void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_verify_send:
//                sendIdentify();
                break;

            case R.id.tv_verify_confirm:
//                if (entrance_flag == MConstants.SIGN_UP_SUCCESS) {
//                    executeSignUp();
//                } else if (entrance_flag == MConstants.CONFIRM_SUCCESS) {
//                    executeResetPassword();
//                }
                break;
        }
    }

    @Override
    protected MachtalkSDKListener setSDKListener() {
        return null;
    }
}
