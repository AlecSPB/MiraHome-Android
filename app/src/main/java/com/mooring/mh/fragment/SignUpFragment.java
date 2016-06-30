package com.mooring.mh.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.activity.VerifyPhoneActivity;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 注册
 * <p>
 * Created by Will on 16/3/30.
 */
public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private EditText edit_phone;
    private EditText edit_psw;
    private TextView tv_sign_btn;
    private TextView tv_sign_error;

    private ImageView imgView_sina;
    private ImageView imgView_weChart;
    private ImageView imgView_QQ;
    private ImageView imgView_facebook;

    private String userName;
    private String userPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected void initFragment() {
    }

    @Override
    protected void initView() {

        edit_phone = (EditText) rootView.findViewById(R.id.edit_phone);
        edit_psw = (EditText) rootView.findViewById(R.id.edit_psw);
        tv_sign_btn = (TextView) rootView.findViewById(R.id.tv_sign_btn);
        tv_sign_error = (TextView) rootView.findViewById(R.id.tv_sign_error);

        imgView_sina = (ImageView) rootView.findViewById(R.id.imgView_sina);
        imgView_weChart = (ImageView) rootView.findViewById(R.id.imgView_weChart);
        imgView_QQ = (ImageView) rootView.findViewById(R.id.imgView_QQ);
        imgView_facebook = (ImageView) rootView.findViewById(R.id.imgView_facebook);

        tv_sign_btn.setOnClickListener(this);
        imgView_sina.setOnClickListener(this);
        imgView_weChart.setOnClickListener(this);
        imgView_QQ.setOnClickListener(this);
        imgView_facebook.setOnClickListener(this);
        edit_phone.addTextChangedListener(EditTextChangeListener);
        edit_psw.addTextChangedListener(EditTextChangeListener);

    }

    /**
     * 文本改变监听
     */
    private TextWatcher EditTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (edit_phone.getText().length() != 0 || edit_psw.getText().length() != 0) {
                tv_sign_error.setText("");
            }
        }
    };

    /**
     * 注册
     */
    private void signUp() {
        Intent it = new Intent();
        it.setClass(getActivity(), VerifyPhoneActivity.class);
        it.putExtra(MConstants.ENTRANCE_FLAG, MConstants.SIGN_UP_SUCCESS);
        it.putExtra(MConstants.SP_KEY_USERNAME, userName);
        it.putExtra(MConstants.SP_KEY_PASSWORD, userPwd);
        context.startActivity(it);
    }

    /**
     * 注册前检索
     *
     * @return
     */
    private boolean checkSignUp() {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            setError(getResources().getString(R.string.error_account_psw_empty));
            return false;
        }
        if (TextUtils.isDigitsOnly(userName)) {
            if (!MUtils.isMobileNO(userName)) {
                setError(getResources().getString(R.string.error_account_format));
                return false;
            }
        } else {
            if (!MUtils.isEmail(userName)) {
                setError(getResources().getString(R.string.error_account_format));
                return false;
            }
        }
        if (!MUtils.checkPsw(userPwd)) {
            setError(getResources().getString(R.string.error_psw_format));
            return false;
        }
        return true;
    }

    /**
     * 设置显示错误信息
     *
     * @param error
     */
    private void setError(String error) {
        tv_sign_error.setVisibility(View.VISIBLE);
        tv_sign_error.setText(error);
    }

    /**
     * 第三方登陆
     */
    private void SSO() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_btn:
                userName = edit_phone.getText().toString().trim();
                userPwd = edit_psw.getText().toString().trim();
                if (checkSignUp()) {
                    signUp();
                }
                break;
            case R.id.imgView_sina:
                SSO();
                break;
            case R.id.imgView_weChart:
                SSO();
                break;
            case R.id.imgView_QQ:
                SSO();
                break;
            case R.id.imgView_facebook:
                SSO();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SignUp");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SignUp");
    }
}
