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
 * 注册成功,修改密码,设备连接成功公用
 * <p/>
 * Created by Will on 16/3/30.
 */
public class CommonSuccessActivity extends AppCompatActivity {
    private ImageView imgView_success_icon;
    private TextView tv_success_tip;

    private int entrance_flag = 0;//0X11:signUp  0X12:confirm  0X21:connected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_success);

        Intent it = getIntent();
        entrance_flag = it.getIntExtra(MConstants.ENTRANCE_FLAG, 0);

        initView();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    Intent intent = new Intent();

                    if (entrance_flag == MConstants.SIGN_UP_SUCCESS) {
                        // 回到系统首页,表示直接注册成功<把之前所有Activity都关闭>
                        intent.setClass(CommonSuccessActivity.this, MainActivity.class);
                    } else if (entrance_flag == MConstants.CONFIRM_SUCCESS) {
                        // 回到登陆页,密码重置成功,继续登陆  <把之前所有Activity都关闭>
                        intent.setClass(CommonSuccessActivity.this, LoginAndSignUpActivity.class);
                    } else if (entrance_flag == MConstants.CONNECTED_SUCCESS) {
                        intent.setClass(CommonSuccessActivity.this, ExistingDeviceActivity.class);
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

        if (entrance_flag != 0) {
            if (entrance_flag == MConstants.SIGN_UP_SUCCESS) {
                //展示注册成功图表和文字
                imgView_success_icon.setImageResource(R.drawable.img_badge_success);
                tv_success_tip.setText(getResources().getString(R.string.register_complete));
            }
            if (entrance_flag == MConstants.CONFIRM_SUCCESS) {
                //展示修改密码成功图标和字体
                imgView_success_icon.setImageResource(R.drawable.img_badge_confirm_success);
                tv_success_tip.setText(getResources().getString(R.string.modify_success));
            }
            if (entrance_flag == MConstants.CONNECTED_SUCCESS) {
                //链接设备成功
                imgView_success_icon.setImageResource(R.drawable.img_badge_success);
                tv_success_tip.setText(getResources().getString(R.string.device_connected));
            }
        }
    }
}
