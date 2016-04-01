package com.mooring.mh.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.activity.ConfirmationPswActivity;
import com.mooring.mh.db.DbXUtils;
import com.mooring.mh.db.User;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;

import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.db.DbManagerImpl;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 登陆
 * <p>
 * Created by Will on 16/3/30.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private EditText edit_phone;
    private EditText edit_psw;
    private TextView tv_forget_psw;
    private TextView tv_login_btn;
    private TextView tv_login_error;

    private ImageView imgView_sina;
    private ImageView imgView_weChart;
    private ImageView imgView_QQ;
    private ImageView imgView_facebook;


    private String phone;//手机号码
    private String psw;//密码
    private DbManager dbManager;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {

        edit_phone = (EditText) rootView.findViewById(R.id.edit_phone);
        edit_psw = (EditText) rootView.findViewById(R.id.edit_psw);
        tv_forget_psw = (TextView) rootView.findViewById(R.id.tv_forget_psw);
        tv_login_btn = (TextView) rootView.findViewById(R.id.tv_login_btn);
        tv_login_error = (TextView) rootView.findViewById(R.id.tv_login_error);

        imgView_sina = (ImageView) rootView.findViewById(R.id.imgView_sina);
        imgView_weChart = (ImageView) rootView.findViewById(R.id.imgView_weChart);
        imgView_QQ = (ImageView) rootView.findViewById(R.id.imgView_QQ);
        imgView_facebook = (ImageView) rootView.findViewById(R.id.imgView_facebook);


        tv_forget_psw.setOnClickListener(this);
        tv_login_btn.setOnClickListener(this);
        imgView_sina.setOnClickListener(this);
        imgView_weChart.setOnClickListener(this);
        imgView_QQ.setOnClickListener(this);
        imgView_facebook.setOnClickListener(this);

    }

    @Override
    protected void lazyLoad() {

    }

    /**
     * 登陆
     */
    private void login() {

        RequestParams params = CommonUtils.getBaseParams(MConstants.LOGIN_BY_MOBILE_PHONE);
        params.addParameter("mobile_phone", phone);
        params.addParameter("password", psw);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null) {

                    //登陆成功   检查本地是否有用户

                    checkHasLocalUser();
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
     * 检查本地是否有用户
     */
    private void checkHasLocalUser() {
        DbManager.DaoConfig config = DbXUtils.getDaoConfig(getActivity());
        dbManager = x.getDb(config);

        List<User> users = new ArrayList<User>();
        try {
            users = dbManager.findAll(User.class);
            if (users.size() == 0) {
                //跳转添加用户界面
            } else {
                //跳转到首页
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    /**
     * 登陆前检索
     *
     * @return
     */
    private boolean checkLogin() {
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
        return true;
    }

    private void setError(String error) {
        tv_login_error.setVisibility(View.VISIBLE);
        tv_login_error.setText(error);
    }

    /**
     * 第三方登陆
     */
    private void SSO() {
        CommonUtils.showToast(getActivity(), CommonUtils.getSP("token"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_psw:
                Intent it = new Intent();
                it.setClass(getActivity(), ConfirmationPswActivity.class);
                getActivity().startActivity(it);
                break;
            case R.id.tv_login_btn:
                phone = edit_phone.getText().toString().trim();
                psw = edit_psw.getText().toString().trim();
                if (checkLogin()) {
                    login();
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
