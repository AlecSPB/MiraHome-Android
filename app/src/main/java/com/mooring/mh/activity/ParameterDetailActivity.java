package com.mooring.mh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;

/**
 * 展示各种数据详情
 * <p/>
 * Created by Will on 16/4/12.
 */
public class ParameterDetailActivity extends AppCompatActivity {

    private int number;
    private String param;
    private String title;

    private View layout_detail;
    private TextView tv_detail_title;
    private TextView tv_detail_param;
    private TextView tv_detail_unit;
    private ImageView imgView_detail_ic;
    private ImageView imgView_act_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter_detail);


        Intent it = getIntent();
        number = it.getIntExtra("number", -1);
        param = it.getStringExtra("param");
        title = it.getStringExtra("title");

        initView();

        initData();

    }

    private void initView() {
        layout_detail = findViewById(R.id.layout_detail);
        tv_detail_title = (TextView) findViewById(R.id.tv_detail_title);
        tv_detail_param = (TextView) findViewById(R.id.tv_detail_param);
        tv_detail_unit = (TextView) findViewById(R.id.tv_detail_unit);
        imgView_detail_ic = (ImageView) findViewById(R.id.imgView_detail_ic);
        imgView_act_back = (ImageView) findViewById(R.id.imgView_act_back);


        imgView_act_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParameterDetailActivity.this.finish();
            }
        });
    }

    private void initData() {
        tv_detail_title.setText(title);
        tv_detail_param.setText(param);
        switch (number) {
            case 0:
                imgView_detail_ic.setImageResource(R.mipmap.ic_launcher);
                tv_detail_unit.setText("bpm");
                break;
            case 1:
                imgView_detail_ic.setImageResource(R.mipmap.ic_launcher);
                tv_detail_unit.setText("/min");
                break;
            case 2:
                imgView_detail_ic.setImageResource(R.mipmap.ic_launcher);
                tv_detail_unit.setVisibility(View.GONE);
                break;
            case 3:
                imgView_detail_ic.setImageResource(R.mipmap.ic_launcher);
                tv_detail_unit.setText("%");
                break;
            case 4:
                imgView_detail_ic.setImageResource(R.mipmap.ic_launcher);
                tv_detail_unit.setText("℃");
                break;
            case 5:
                imgView_detail_ic.setImageResource(R.mipmap.ic_launcher);
                tv_detail_unit.setText("℃");
                break;
            case 6:
                imgView_detail_ic.setImageResource(R.mipmap.ic_launcher);
                tv_detail_unit.setVisibility(View.GONE);
                break;
            case 7:
                imgView_detail_ic.setImageResource(R.mipmap.ic_launcher);
                tv_detail_unit.setVisibility(View.GONE);
                break;
        }
    }
}
