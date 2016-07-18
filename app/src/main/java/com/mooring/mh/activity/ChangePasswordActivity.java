package com.mooring.mh.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 修改密码(忘记密码) ————真正主要功能验证手机号是否符合格式
 * 1.当前页面输入账号(手机号/邮箱号)
 * 2.跳转到VerifyPhoneActivity获取验证码,并且回传
 * 3.利用验证码和最新的密码提交更改密码
 * <p/>
 * Created by Will on 16/3/30.
 */
public class ChangePasswordActivity extends BaseActivity {
    private EditText edit_confirm_phone;
    private TextView tv_confirm_next;
    private TextView tv_confirm_error;

    private String phone;
    private String newPsw;
    private String verify_code;

    private boolean isReturn = false;//是否回调回来

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected String getTitleName() {
        return getString(R.string.title_confirmation_psw);
    }

    @Override
    protected void initActivity() {

    }

    @Override
    protected void initView() {
        edit_confirm_phone = (EditText) findViewById(R.id.edit_confirm_phone);
        tv_confirm_next = (TextView) findViewById(R.id.tv_confirm_next);
        tv_confirm_error = (TextView) findViewById(R.id.tv_confirm_error);

        tv_confirm_next.setOnClickListener(this);
    }

    @Override
    protected void OnClick(View v) {
        if (isReturn) {
            newPsw = edit_confirm_phone.getText().toString().trim();
            if (checkPsw()) {
                resetPassword();
            }
        } else {
            phone = edit_confirm_phone.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                setError(getString(R.string.error_account_empty));
            } else if (!(MUtils.isEmail(phone) || MUtils.isMobileNO(phone))) {
                setError(getString(R.string.error_account_format));
            } else {
                Intent it = new Intent();
                it.setClass(ChangePasswordActivity.this, VerifyPhoneActivity.class);
                it.putExtra(MConstants.ENTRANCE_FLAG, MConstants.CONFIRM_SUCCESS);
                it.putExtra(MConstants.SP_KEY_USERNAME, phone);
                startActivityForResult(it, MConstants.CHANGE_PSW_REQUEST);
            }
        }
    }

    /**
     * 使用手机号重置密码
     */
    private void resetPassword() {
        RequestParams params = MUtils.getBaseParams(MConstants.RESET_PASSWORD_BY_PHONE);
        params.addParameter("verify_code", verify_code);
        params.addParameter("new_password", newPsw);
        params.addParameter("mobile_phone", phone);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null) {
                    String code = result.optString("code");
                    if ("0".equals(code)) {
                        //重置密码成功,跳转成功展示界面
                        showSuccess();
                    }else if("2".equals(code)){
                        setError(getString(R.string.error_verify));
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException hex = (HttpException) ex;
                    //提示网络原因导致错误
                    MUtils.showToast(context, getString(R.string.network_exception));
                } else { // 其他错误
                    LogUtil.e("onError message: " + ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.w("onCancel message: " + cex.getMessage());
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 跳转注册成功页
     */
    private void showSuccess() {
        Intent it = new Intent();
        it.setClass(ChangePasswordActivity.this, CommonSuccessActivity.class);
        it.putExtra(MConstants.ENTRANCE_FLAG, MConstants.CONFIRM_SUCCESS);
        startActivity(it);
    }

    /**
     * 修改前检查密码格式
     *
     * @return
     */
    private boolean checkPsw() {
        if (TextUtils.isEmpty(newPsw)) {
            setError(getString(R.string.tv_new_psw_empty));
            return false;
        }
        if (!MUtils.checkPsw(newPsw)) {
            setError(getString(R.string.error_psw_format));
            return false;
        }
        return true;
    }

    private void setError(String error) {
        tv_confirm_error.setVisibility(View.VISIBLE);
        tv_confirm_error.setText(error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MConstants.CHANGE_PSW_REQUEST && resultCode == MConstants.CHANGE_PSW_RESULT) {
            edit_confirm_phone.setText("");
            edit_confirm_phone.setHint(getString(R.string.tv_new_password));
            tv_confirm_next.setText(getString(R.string.tv_confirm));
            tv_confirm_error.setVisibility(View.INVISIBLE);
            if (data != null) {
                isReturn = data.getBooleanExtra("isReturn", false);
                verify_code = data.getStringExtra("verify_code");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ChangePassword");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ChangePassword");
        MobclickAgent.onPause(this);
    }
}
