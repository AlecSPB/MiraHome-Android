package com.mooring.mh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.utils.MConstants;

/**
 * 注册成功和修改密码成功公用
 * <p/>
 * Created by Will on 16/3/30.
 */
public class CommonSuccessActivity extends AppCompatActivity {
    private ImageView imgView_success_icon;
    private TextView tv_success_tip;

    private int entranceFlag = 0;//0X11:signUp  0X22:confirm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_success);

        Intent it = getIntent();
        entranceFlag = it.getIntExtra("entranceFlag", 0);

        initView();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    Intent intent = new Intent();

                    if (entranceFlag == MConstants.SIGN_UP_SUCCESS) {
                        // 回到系统首页,表示直接注册成功<把之前所有Activity都关闭>
                        intent.setClass(CommonSuccessActivity.this, MainActivity.class);
                    } else if (entranceFlag == MConstants.CONFIRM_SUCCESS) {
                        // 回到登陆页,密码重置成功,继续登陆  <把之前所有Activity都关闭>
                        intent.setClass(CommonSuccessActivity.this, LoginAndSignUpActivity.class);
                    }
                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                    CommonSuccessActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    private void initView() {
        imgView_success_icon = (ImageView) findViewById(R.id.imgView_success_icon);
        tv_success_tip = (TextView) findViewById(R.id.tv_success_tip);

        if (entranceFlag != 0) {
            if (entranceFlag == MConstants.SIGN_UP_SUCCESS) {

//                展示注册成功图表和文字

            }
            if (entranceFlag == MConstants.CONFIRM_SUCCESS) {

//                展示修改密码成功图标和字体

            }
        }
    }
}
