package com.mooring.mh.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 修改密码
 * 真正主要功能验证手机号是否复合格式
 * Created by Will on 16/3/30.
 */
public class ConfirmationPswActivity extends BaseActivity {
    private EditText edit_confirm_phone;
    private TextView tv_confirm_next;
    private TextView tv_confirm_error;

    private String phone;
    private String newPsw;
    private String verify_code;

    private boolean isReturn = false;//是否回调回来

    @Override
    protected int getLayoutId() {
        return R.layout.activity_confirmation_psw;
    }

    @Override
    protected String getTitleName() {
        return "Confirmation Password";
    }

    @Override
    protected void initActivity() {
        edit_confirm_phone = (EditText) findViewById(R.id.edit_confirm_phone);
        tv_confirm_next = (TextView) findViewById(R.id.tv_confirm_next);
        tv_confirm_error = (TextView) findViewById(R.id.tv_confirm_error);

        tv_confirm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReturn) {
                    newPsw = edit_confirm_phone.getText().toString().trim();
                    if (checkPsw()) {
                        resetPassword();
                    }
                } else {
                    phone = edit_confirm_phone.getText().toString();
                    if (!(CommonUtils.isEmail(phone) || CommonUtils.isMobileNO(phone))) {
                        setError("Please use the phone number or E-mail login");
                    } else {
                        Intent it = new Intent();
                        it.setClass(ConfirmationPswActivity.this, VerifyPhoneActivity.class);
                        it.putExtra("entranceFlag", MConstants.CONFIRM_SUCCESS);
                        it.putExtra("phone", phone);
                        startActivity(it);
                        startActivityForResult(it, 100001);
                    }
                }
            }
        });
    }

    /**
     * 使用手机号重置密码
     */
    private void resetPassword() {
        RequestParams params = CommonUtils.getBaseParams(MConstants.RESET_PASSWORD_BY_PHONE);
        params.addParameter("verify_code", verify_code);
        params.addParameter("new_password", newPsw);
        params.addParameter("mobile_phone", phone);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null) {


                    //重置密码成功,跳转成功展示界面

//                    showSuccess();


                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

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
        it.setClass(ConfirmationPswActivity.this, CommonSuccessActivity.class);
        it.putExtra("entranceFlag", MConstants.CONFIRM_SUCCESS);
        startActivity(it);
    }

    /**
     * 修改前检查密码格式
     *
     * @return
     */
    private boolean checkPsw() {

        if (TextUtils.isEmpty(newPsw)) {
            setError("The password can not be empty");
            return false;
        }
        if (!CommonUtils.checkPsw(newPsw)) {
            setError("Please enter 8-12 by the numbers and letters in the password");
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
        if (requestCode == 100001 && resultCode == MConstants.CONFIRM_SUCCESS) {
            edit_confirm_phone.setText("");
            edit_confirm_phone.setHint("New Password");
            tv_confirm_next.setText("Confirm");
            tv_confirm_error.setVisibility(View.INVISIBLE);
            if (data != null) {
                isReturn = data.getBooleanExtra("isReturn", false);
                verify_code = data.getStringExtra("verify_code");
            }
        }
    }
}
