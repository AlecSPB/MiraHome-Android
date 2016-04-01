package com.mooring.mh.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.activity.VerifyPhoneActivity;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;

/**
 * 注册
 * <p/>
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

    private String phone;
    private String psw;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected void initView() {

        edit_phone = (EditText) rootView.findViewById(R.id.edit_phone);
        edit_psw = (EditText) rootView.findViewById(R.id.edit_phone);
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

    }

    @Override
    protected void lazyLoad() {

    }

    /**
     * 注册
     */
    private void signUp() {
        Intent it = new Intent();
        it.setClass(getActivity(), VerifyPhoneActivity.class);
        it.putExtra("entranceFlag", MConstants.SIGN_UP_SUCCESS);
        it.putExtra("phone", phone);
        it.putExtra("psw", psw);
        getActivity().startActivity(it);
    }

    /**
     * 注册前检索
     *
     * @return
     */
    private boolean checkSignUp() {
        if ("".equals(phone)) {
            setError("Login name can not be empty");
            return false;
        }
        if ("".equals(psw)) {
            setError("The password can not be empty");
            return false;
        }
        if (!(CommonUtils.isMobileNO(phone) || CommonUtils.isEmail(phone))) {
            setError("Please use the phone number or E-mail login!");
            return false;
        }
        if (!CommonUtils.checkPsw(psw)) {
            setError("Please enter 8-12 by the numbers and letters in the password");
            return false;
        }
        return true;
    }

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
                phone = edit_phone.getText().toString().trim();
                psw = edit_psw.getText().toString().trim();
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
}