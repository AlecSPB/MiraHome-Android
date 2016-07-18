package com.mooring.mh.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.activity.ChangePasswordActivity;
import com.mooring.mh.activity.MainActivity;
import com.mooring.mh.db.DbXUtils;
import com.mooring.mh.db.LocalUser;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
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

    private EditText edit_userName;//用户名
    private EditText edit_userPwd;//密码
    private TextView tv_forget_psw;//忘记密码
    private TextView tv_login_btn;//登录按钮
    private TextView tv_login_error;//错误提示
    /**
     * 第三方登录
     */
    private ImageView imgView_sina;
    private ImageView imgView_weChart;
    private ImageView imgView_QQ;
    private ImageView imgView_facebook;

    private String userName;//手机号码
    private String userPwd;//密码
    private DbManager dbManager;

    private MSDKListener msdkListener;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initFragment() {
        DbManager.DaoConfig config = DbXUtils.getDaoConfig(getActivity());
        dbManager = x.getDb(config);

        msdkListener = new MSDKListener();
    }

    @Override
    protected void initView() {

        edit_userName = (EditText) rootView.findViewById(R.id.edit_phone);
        edit_userPwd = (EditText) rootView.findViewById(R.id.edit_psw);
        tv_forget_psw = (TextView) rootView.findViewById(R.id.tv_forget_psw);
        tv_login_btn = (TextView) rootView.findViewById(R.id.tv_login_btn);
        tv_login_error = (TextView) rootView.findViewById(R.id.tv_login_error);

        imgView_sina = (ImageView) rootView.findViewById(R.id.imgView_sina);
        imgView_weChart = (ImageView) rootView.findViewById(R.id.imgView_weChart);
        imgView_QQ = (ImageView) rootView.findViewById(R.id.imgView_QQ);
        imgView_facebook = (ImageView) rootView.findViewById(R.id.imgView_facebook);

        edit_userName.addTextChangedListener(EditTextChangeListener);
        edit_userPwd.addTextChangedListener(EditTextChangeListener);
        tv_forget_psw.setOnClickListener(this);
        tv_login_btn.setOnClickListener(this);
        imgView_sina.setOnClickListener(this);
        imgView_weChart.setOnClickListener(this);
        imgView_QQ.setOnClickListener(this);
        imgView_facebook.setOnClickListener(this);
        edit_userName.setText(sp.getString(MConstants.SP_KEY_USERNAME, ""));//取出本地默认账号

        //--------------暂时使用,真实情况去除!!!!!!!!---------------------------
        edit_userName.setText("18136093612");
        edit_userPwd.setText("123456");

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
            if (edit_userName.getText().length() != 0 || edit_userPwd.getText().length() != 0) {
                tv_login_error.setText("");
            }
        }
    };

    /**
     * 登陆
     */
    private void login() {

        MUtils.showLoadingDialog(context, null);
        MachtalkSDK.getInstance().userLogin(
                edit_userName.getText().toString(),
                edit_userPwd.getText().toString(), null);

        //执行一次查询列表
        MachtalkSDK.getInstance().queryDeviceList();

        /*RequestParams params = MUtils.getBaseParams(MConstants.LOGIN_BY_MOBILE_PHONE);
        params.addParameter("mobile_userName", userName);
        params.addParameter("password", userPwd);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null) {

                    //登陆成功   检查本地是否有用户

//                    checkHasLocalUser();

                    JSONObject data = result.optJSONObject("data");
                    if(data!=null){
                        editor.putString(MConstants.SP_KEY_USERNAME, userName);
                        editor.putString(MConstants.SP_KEY_PASSWORD, userPwd);
                        editor.commit();

                        LogUtil.i("store username: " + userName + " password: " + userPwd);

                        startActivity(new Intent(context, MainActivity.class));
                        context.finish();
                    }else{
                        MUtils.showToast(context, result.optString("message"));
                    }
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

//        editor.putString(MConstants.SP_KEY_USERNAME, userName);
//        editor.putString(MConstants.SP_KEY_PASSWORD, userPwd);
//        editor.commit();
//
//        LogUtil.i("store username: " + userName + " password: " + userPwd);
//
//        startActivity(new Intent(context, MainActivity.class));
//        context.finish();

    }

    /**
     * 检查本地是否有用户
     */
    private void checkHasLocalUser() {

        List<LocalUser> users = new ArrayList<LocalUser>();
        try {
            users = dbManager.findAll(LocalUser.class);
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
     * 设置错误信息
     *
     * @param error
     */
    private void setError(String error) {
        tv_login_error.setVisibility(View.VISIBLE);
        tv_login_error.setText(error);
    }

    /**
     * 第三方登陆
     */
    private void SSO() {
        MUtils.showToast(context, sp.getString(MConstants.SP_KEY_TOKEN, ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_psw:
                //跳转忘记密码
                startActivity(new Intent(context, ChangePasswordActivity.class));
                break;
            case R.id.tv_login_btn:
                userName = edit_userName.getText().toString().trim();
                userPwd = edit_userPwd.getText().toString().trim();


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

    class MSDKListener extends MachtalkSDKListener {

        @Override
        public void onServerConnectStatusChanged(MachtalkSDKConstant.ServerConnStatus scs) {
            super.onServerConnectStatusChanged(scs);
            if (scs == MachtalkSDKConstant.ServerConnStatus.CONNECT_TIMEOUT) {
                //者连接超时,重新登陆
                MachtalkSDK.getInstance().userLogin(
                        edit_userName.getText().toString(),
                        edit_userPwd.getText().toString(), null);
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

                MUtils.showToast(context, getString(R.string.login_success));
                startActivity(new Intent(context, MainActivity.class));
                context.finish();
            } else {
                if (errMsg == null) {
                    errMsg = getResources().getString(R.string.network_exception);
                }
                LogUtil.e(errMsg);
                setError(getString(R.string.login_failed));
            }
            MUtils.hideLoadingDialog();
        }
    }

    /**
     * 保存成员到本地
     *
     * @param result member_id, member_name, member_image, gender, birth_date, height, weight
     */
    private void saveToLocalUser(JSONObject result) {
        JSONArray data = result.optJSONArray("data");
        if (data != null) {
            for (int i = 0; i < data.length(); i++) {
                JSONObject o = data.optJSONObject(i);
                LocalUser user = new LocalUser();
                user.setId(o.optInt(MConstants.SP_KEY_UID));
                user.set_name(o.optString("telephone"));
                user.set_sex(o.optInt("sex"));
                user.set_platformId("platformId");
                user.set_location(MConstants.BED_LEFT);//登录用户默认在左边---初次使用,以后登录不在设置
                try {
                    dbManager.saveOrUpdate(user);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MachtalkSDK.getInstance().removeSdkListener(msdkListener);
        } else {
            MachtalkSDK.getInstance().setContext(context);
            MachtalkSDK.getInstance().setSdkListener(msdkListener);
        }
    }

    @Override
    protected void OnResume() {
        MachtalkSDK.getInstance().setContext(context);
        MachtalkSDK.getInstance().setSdkListener(msdkListener);
        MobclickAgent.onPageStart("Login");
    }

    @Override
    protected void OnPause() {
        MachtalkSDK.getInstance().removeSdkListener(msdkListener);
        MobclickAgent.onPageEnd("Login");
    }
}
