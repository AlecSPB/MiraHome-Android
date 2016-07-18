package com.mooring.mh.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.utils.MConstants;
import com.umeng.analytics.MobclickAgent;

/**
 * 注册成功,修改密码,设备连接成功,添加用户公用
 * <p>
 * Created by Will on 16/3/30.
 */
public class CommonSuccessActivity extends BaseActivity {
    private ImageView imgView_success_icon;
    private TextView tv_success_tip;
    private int entrance_flag = 0;//0X11:signUp  0X12:confirm  0X21:connected 0X30:add user

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_success;
    }

    @Override
    protected String getTitleName() {
        return null;
    }

    @Override
    protected void initActivity() {
        Intent it = getIntent();
        entrance_flag = it.getIntExtra(MConstants.ENTRANCE_FLAG, 0);
    }

    @Override
    protected void initView() {
        imgView_success_icon = (ImageView) findViewById(R.id.imgView_success_icon);
        tv_success_tip = (TextView) findViewById(R.id.tv_success_tip);

        switch (entrance_flag) {
            case MConstants.ADD_USER_SUCCESS:
                editImageText(R.drawable.img_add_new_user, R.string.tip_add_user_success);
                break;
            case MConstants.SIGN_UP_SUCCESS:
                editImageText(R.drawable.img_badge_success, R.string.register_complete);
                break;
            case MConstants.CONFIRM_SUCCESS:
                editImageText(R.drawable.img_badge_confirm_success, R.string.modify_success);
                break;
            case MConstants.CONNECTED_SUCCESS:
                editImageText(R.drawable.img_badge_success, R.string.mooring_conn_success);
                break;
        }

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    Intent intent = new Intent();
                    switch (entrance_flag) {
                        case MConstants.ADD_USER_SUCCESS:
                            intent.setClass(CommonSuccessActivity.this, MainActivity.class);
                            break;
                        case MConstants.SIGN_UP_SUCCESS:
                            intent.putExtra(MConstants.ENTRANCE_FLAG, MConstants.ADD_USER_REQUEST);
                            intent.setClass(CommonSuccessActivity.this, UserInfoActivity.class);
                            break;
                        case MConstants.CONFIRM_SUCCESS:
                            intent.setClass(CommonSuccessActivity.this, LoginAndSignUpActivity.class);
                            break;
                        case MConstants.CONNECTED_SUCCESS:
                            intent.setClass(CommonSuccessActivity.this, MainActivity.class);
                            break;
                    }
                    startActivity(intent);
                    context.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    @Override
    protected void OnClick(View v) {
    }

    /**
     * 修改图片背景和展示文字
     *
     * @param imgRs 图片id
     * @param tvRs  文本id
     */
    private void editImageText(int imgRs, int tvRs) {
        imgView_success_icon.setImageResource(imgRs);
        tv_success_tip.setText(getString(tvRs));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CommonSuccess");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CommonSuccess");
        MobclickAgent.onPause(this);
    }
}