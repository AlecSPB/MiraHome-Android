package com.mooring.mh.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.views.ControlView.DragScaleTwoView;
import com.mooring.mh.views.ControlView.DragScaleView;
import com.mooring.mh.views.CustomImageView.CircleImageView;
import com.mooring.mh.views.other.CommonDialog;

/**
 * Heating 控制Activity
 * <p/>
 * Created by Will on 16/4/8.
 */
public class HeatingControlActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private ImageView imgView_act_back;
    private View layout_two_header;
    private CircleImageView circleImg_left;
    private CircleImageView circleImg_right;

    private View layout_one_user;
    private DragScaleView dragScaleView;
    private Switch switch_one_user;

    private View layout_two_user;
    private DragScaleTwoView dragScaleTwoView;
    private Switch switch_left;
    private Switch switch_right;

    /**
     * 当前用户的个数
     */
    private int currUsers = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heating_control);

        initView();

        initData();
    }

    private void initView() {

        imgView_act_back = (ImageView) findViewById(R.id.imgView_act_back);
        layout_two_header = findViewById(R.id.layout_two_header);
        circleImg_left = (CircleImageView) findViewById(R.id.circleImg_left);
        circleImg_right = (CircleImageView) findViewById(R.id.circleImg_right);

        layout_one_user = findViewById(R.id.layout_one_user);
        dragScaleView = (DragScaleView) findViewById(R.id.dragScaleView);
        switch_one_user = (Switch) findViewById(R.id.switch_one_user);

        layout_two_user = findViewById(R.id.layout_two_user);
        dragScaleTwoView = (DragScaleTwoView) findViewById(R.id.dragScaleTwoView);
        switch_left = (Switch) findViewById(R.id.switch_left);
        switch_right = (Switch) findViewById(R.id.switch_right);


    }

    private void initData() {
        //判断当前使用户的个数
        imgView_act_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeatingControlActivity.this.finish();
            }
        });

        if (currUsers == 1) {
            layout_two_user.setVisibility(View.GONE);
            layout_two_header.setVisibility(View.GONE);
            layout_one_user.setVisibility(View.VISIBLE);

            switch_one_user.setOnCheckedChangeListener(this);
            switch_one_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });
            dragScaleView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                    OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    dragScaleView.setCurrTemperature(69);
                    dragScaleView.setIsDropAble(true);
                }
            });

            dragScaleView.setOnDropListener(new DragScaleView.OnDropListener() {
                @Override
                public void onDrop(String currTemp) {

                }
            });

        } else {
            layout_one_user.setVisibility(View.GONE);
            layout_two_user.setVisibility(View.VISIBLE);
            layout_two_header.setVisibility(View.VISIBLE);

            switch_left.setOnCheckedChangeListener(this);
            switch_right.setOnCheckedChangeListener(this);

            dragScaleTwoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                    OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    dragScaleTwoView.setCurrLeftTemp("70℉");
                    dragScaleTwoView.setCurrRightTemp("88℉");
                    dragScaleTwoView.setIsLeftDropAble(true);
                    dragScaleTwoView.setIsRightDropAble(true);
                    dragScaleTwoView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });

            dragScaleTwoView.setOnDropListener(new DragScaleTwoView.OnDropListener() {
                @Override
                public void onDrop(String currTemp, int location) {

                }
            });
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_one_user:
                if (isChecked) {
                    dragScaleView.setIsDropAble(true);
                } else {
                    dragScaleView.setIsDropAble(false);
                }
                break;

            case R.id.switch_left:
                if (isChecked) {
                    dragScaleTwoView.setIsLeftDropAble(true);
                } else {
                    dragScaleTwoView.setIsLeftDropAble(false);
                }
                break;

            case R.id.switch_right:
                if (isChecked) {
                    dragScaleTwoView.setIsRightDropAble(true);
                } else {
                    dragScaleTwoView.setIsRightDropAble(false);
                }
                break;
        }
    }

    /**
     * show提示dialog
     */
    private void showDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage("stop drying?");
        builder.setLogo(R.mipmap.img_close_heating);
        builder.setCanceledOnTouchOtherPlace(false);
        builder.setPositiveButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


}
