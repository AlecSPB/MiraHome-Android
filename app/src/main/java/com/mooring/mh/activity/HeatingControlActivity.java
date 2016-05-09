package com.mooring.mh.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.mooring.mh.R;
import com.mooring.mh.views.CommonDialog;
import com.mooring.mh.views.ControlView.DragScaleTwoView;
import com.mooring.mh.views.ControlView.DragScaleView;
import com.mooring.mh.views.CustomImageView.CircleImageView;
import com.mooring.mh.views.CustomToggle;

/**
 * Heating 控制Activity
 * <p/>
 * Created by Will on 16/4/8.
 */
public class HeatingControlActivity extends AppCompatActivity implements CustomToggle.OnCheckedChangeListener {
    private ImageView imgView_act_back;
    private View layout_two_header;
    private CircleImageView circleImg_left;
    private CircleImageView circleImg_right;

    private View layout_one_user;
    private DragScaleView dragScaleView;
    private CustomToggle toggle_middle;

    private View layout_two_user;
    private DragScaleTwoView dragScaleTwoView;
    private CustomToggle toggle_left;
    private CustomToggle toggle_right;

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
        toggle_middle = (CustomToggle) findViewById(R.id.toggle_middle);
        toggle_middle.setChecked(true);

        layout_two_user = findViewById(R.id.layout_two_user);
        dragScaleTwoView = (DragScaleTwoView) findViewById(R.id.dragScaleTwoView);
        toggle_left = (CustomToggle) findViewById(R.id.toggle_left);
        toggle_right = (CustomToggle) findViewById(R.id.toggle_right);
        toggle_left.setChecked(true);
        toggle_right.setChecked(true);


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

            toggle_middle.setOnCheckedChange(this);
            dragScaleView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                    OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    dragScaleView.setCurrTemperature(69);
                    dragScaleView.setIsDropAble(true);
                    dragScaleView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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

            toggle_left.setOnCheckedChange(this);
            toggle_right.setOnCheckedChange(this);

            dragScaleTwoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                    OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    dragScaleTwoView.setCurrLeftTemp(70);
                    dragScaleTwoView.setCurrRightTemp(88);
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

    /**
     * show提示dialog
     */
    private void showDialog(final CustomToggle toggle) {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage("stop heating?");
        builder.setLogo(R.drawable.img_close_heating);
        builder.setCanceledOnTouchOtherPlace(false);
        builder.setPositiveButton(true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (toggle == toggle_middle) {
                    toggle_middle.setChecked(false);
                    dragScaleView.setIsDropAble(false);
                }
                if (toggle == toggle_left) {
                    toggle_left.setChecked(false);
                    dragScaleTwoView.setIsLeftDropAble(false);
                }
                if (toggle == toggle_right) {
                    toggle_right.setChecked(false);
                    dragScaleTwoView.setIsRightDropAble(false);
                }
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


    @Override
    public void onCheckedChanged(View v, boolean isChecked) {
        switch (v.getId()) {
            case R.id.toggle_middle:
                if (isChecked) {
                    toggle_middle.setChecked(true);
                    dragScaleView.setIsDropAble(true);
                } else {
                    showDialog(toggle_middle);
                }
                break;

            case R.id.toggle_left:
                if (isChecked) {
                    toggle_left.setChecked(true);
                    dragScaleTwoView.setIsLeftDropAble(true);
                } else {
                    showDialog(toggle_middle);
                }
                break;

            case R.id.toggle_right:
                if (isChecked) {
                    toggle_right.setChecked(true);
                    dragScaleTwoView.setIsRightDropAble(true);
                } else {
                    showDialog(toggle_middle);
                }
                break;
        }
    }
}
