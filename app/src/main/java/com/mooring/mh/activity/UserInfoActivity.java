package com.mooring.mh.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.mooring.mh.R;

/**
 * 用户信息查看和编辑页面
 * <p/>
 * Created by Will on 16/4/15.
 */
public class UserInfoActivity extends BaseActivity {

    private ImageView imgView_act_right;
    private TextView tv_confirm;
    private TextView tv_on_mooring;
    private EditText edText_name;
    private TextView tv_sex;
    private TextView tv_birthday;
    private TextView tv_height;
    private TextView tv_weight;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected String getTitleName() {
        return getString(R.string.title_user_info);
    }

    @Override
    protected void initActivity() {

        initView();
    }


    private void initView() {
        imgView_act_right = (ImageView) findViewById(R.id.imgView_act_right);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_on_mooring = (TextView) findViewById(R.id.tv_on_mooring);
        edText_name = (EditText) findViewById(R.id.edText_name);
        if (edText_name != null) {
            edText_name.setFocusable(true);
            edText_name.setFocusableInTouchMode(true);
            edText_name.requestFocus();
        }
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_weight = (TextView) findViewById(R.id.tv_weight);

        imgView_act_right.setVisibility(View.VISIBLE);
        imgView_act_right.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        tv_on_mooring.setOnClickListener(this);
        tv_sex.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        tv_height.setOnClickListener(this);
        tv_weight.setOnClickListener(this);
    }

    @Override
    protected void OnClick(View v) {
        switch (v.getId()) {
            case R.id.imgView_act_right:

                break;
            case R.id.tv_confirm:

                break;
            case R.id.tv_on_mooring:

                break;
            case R.id.tv_sex:

                break;
            case R.id.tv_birthday:

                break;
            case R.id.tv_height:

                break;
            case R.id.tv_weight:

                break;
        }
    }

    @Override
    protected MachtalkSDKListener setSDKListener() {
        return null;
    }
}
