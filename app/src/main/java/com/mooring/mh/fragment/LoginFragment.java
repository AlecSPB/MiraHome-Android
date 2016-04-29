package com.mooring.mh.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.activity.ConfirmationPswActivity;
import com.mooring.mh.activity.MainActivity;
import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.db.DbXUtils;
import com.mooring.mh.db.User;
import com.mooring.mh.utils.CommonUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 登陆
 * <p/>
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

    MyYuulinkSDKListener listener;


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

        listener = new MyYuulinkSDKListener();

        MachtalkSDK.getInstance().startSDK(getContext(), null);
        MachtalkSDK.getInstance().setSdkListener(listener);


    }

    class MyYuulinkSDKListener extends MachtalkSDKListener {

        @Override
        public void onUserLogin(Result result, String user) {

            Log.e("onUserLogin", "onUserLogin  " + user + "  " + result.getErrorCode() + "  " + result.getSuccess());

            int success = Result.FAILED;
            String errmesg = null;
            if (result != null) {
                success = result.getSuccess();
                errmesg = result.getErrorMessage();
            }
//            closeDialog();
            if (success == Result.SUCCESS) {
//                editor.putString(Constant.SP_KEY_USERNAME, telephone);
//                editor.putString(Constant.SP_KEY_PASSWORD, password);
//                editor.commit();
//                Log.i(TAG, "store username: " + telephone + " password: " + password);
//                DemoGlobal.instance().setUserName(telephone);
//                DemoGlobal.instance().setPassword(password);
//
//                Intent it = new Intent(UserLogin.this, Main.class);
//                startActivity(it);
//                UserLogin.this.finish();

                SharedPreferences.Editor edit = InitApplicationHelper.sp.edit();
                edit.putBoolean("appFirstStart", false);
                edit.commit();

                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            } else {
            }
        }


    }

    @Override
    protected void lazyLoad() {

    }

    /**
     * 登陆
     */
    private void login() {


//        MachtalkSDK.getInstance().userLogin("13661498824", "123456", 1);

        MachtalkSDK.getInstance().userLogin("mirahome", "n935rq", 1);

        /*RequestParams params = CommonUtils.getBaseParams(MConstants.LOGIN_BY_MOBILE_PHONE);
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
        });*/

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


                /*if (checkLogin()) {
                    login();
                }*/

                login();


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
    public void onDestroy() {
        super.onDestroy();
        MachtalkSDK.getInstance().stopSDK();
        MachtalkSDK.getInstance().removeSdkListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(getContext());
        MachtalkSDK.getInstance().setSdkListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(listener);
    }
}
