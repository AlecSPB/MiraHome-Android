package com.mooring.mh.activity;

import android.app.Dialog;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.views.CustomImageView.CircleImageView;
import com.mooring.mh.views.WheelPicker.AbstractWheelPicker;
import com.mooring.mh.views.WheelPicker.widget.WheelDatePicker;
import com.mooring.mh.views.WheelPicker.widget.WheelHeightSelectPicker;
import com.mooring.mh.views.WheelPicker.widget.WheelWeightSelectPicker;

/**
 * 添加用户Activity
 * <p/>
 * Created by Will on 16/3/31.
 */
public class AddUserActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView imgView_add_user;
    private EditText add_user_name;
    private TextView add_user_sex;
    private TextView add_user_birthday;
    private TextView add_user_height;
    private TextView add_user_weight;
    private TextView tv_add_user_confirm;

    private Dialog dialog_choose_sex;

    private int sex = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_user;
    }

    @Override
    protected String getTitleName() {
        return "User profile";
    }

    @Override
    protected void initActivity() {


        initView();


        initData();
    }

    private void initView() {
        imgView_add_user = (CircleImageView) findViewById(R.id.imgView_add_user);
        add_user_name = (EditText) findViewById(R.id.add_user_name);
        add_user_sex = (TextView) findViewById(R.id.add_user_sex);
        add_user_birthday = (TextView) findViewById(R.id.add_user_birthday);
        add_user_height = (TextView) findViewById(R.id.add_user_height);
        add_user_weight = (TextView) findViewById(R.id.add_user_weight);
        tv_add_user_confirm = (TextView) findViewById(R.id.tv_add_user_confirm);

        imgView_add_user.setOnClickListener(this);
        add_user_sex.setOnClickListener(this);
        add_user_birthday.setOnClickListener(this);
        add_user_height.setOnClickListener(this);
        add_user_weight.setOnClickListener(this);
        tv_add_user_confirm.setOnClickListener(this);


    }

    private void initData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgView_add_user:

                break;
            case R.id.add_user_sex:
                SelectSex();
                break;
            case R.id.add_user_birthday:
                SelectBirthday();
                break;
            case R.id.add_user_height:
                SelectHeight();
                break;
            case R.id.add_user_weight:
                SelectWeight();
                break;
            case R.id.tv_add_user_confirm:

                break;
        }
    }

    /**
     * 选择性别
     */
    private void SelectSex() {
        dialog_choose_sex = new Dialog(this, R.style.MyDialogStyle);
        dialog_choose_sex.setContentView(R.layout.dialog_select_sex);
        dialog_choose_sex.setCanceledOnTouchOutside(true);
        dialog_choose_sex.findViewById(R.id.sex_other_view).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog_choose_sex.cancel();
                    }
                });

        dialog_choose_sex.findViewById(R.id.sex_choose_male).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        sex = 0;
                    }
                });

        dialog_choose_sex.findViewById(R.id.sex_choose_female).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        sex = 1;
                    }
                });

        dialog_choose_sex.findViewById(R.id.imgView_chose_confirm).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        add_user_sex.setText(sex == 0 ? "Male" : "Female");
                        dialog_choose_sex.cancel();
                    }
                });
        dialog_choose_sex.show();
        CommonUtils.setDialogFullScreen(this, dialog_choose_sex);
    }


    private int padding;
    private int textSize;
    private int itemSpace;
    private ImageView select_confirm;
    private String date = "";


    private Dialog dialog;

    /**
     * 选择生日
     */
    private void SelectBirthday() {
        dialog = new Dialog(this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_select_birthday);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.other_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        select_confirm = (ImageView) dialog.findViewById(R.id.select_confirm);
        select_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交
                add_user_birthday.setText(date);
                dialog.cancel();
            }
        });

        padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        textSize = getResources().getDimensionPixelSize(R.dimen.TextSizeLarge);
        itemSpace = getResources().getDimensionPixelSize(R.dimen.ItemSpaceLarge);
        WheelDatePicker datePicker = (WheelDatePicker) dialog.findViewById(R.id.wheel_date);
        datePicker.setPadding(0, 0, 0, 0);
        datePicker.setTextColor(getResources().getColor(R.color.colorWhite50));
        datePicker.setCurrentTextColor(getResources().getColor(R.color.colorPurple));
        datePicker.setTextSize(textSize);
        datePicker.setItemSpace(itemSpace);
        datePicker.setCurrentDate(2015, 12, 20);
        datePicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(View view, int index, String data) {
                date = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (state != AbstractWheelPicker.SCROLL_STATE_IDLE) {
                    select_confirm.setEnabled(false);
                } else {
                    select_confirm.setEnabled(true);
                }
            }
        });

        dialog.show();
        CommonUtils.setDialogFullScreen(this, dialog);
    }

    /**
     * 选择身高
     */
    private void SelectHeight() {
        dialog = new Dialog(this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_select_height);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.other_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        select_confirm = (ImageView) dialog.findViewById(R.id.select_confirm);
        select_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交
                add_user_height.setText(date);
                dialog.cancel();
            }
        });

        padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        textSize = getResources().getDimensionPixelSize(R.dimen.TextSizeLarge);
        itemSpace = getResources().getDimensionPixelSize(R.dimen.ItemSpaceLarge);
        WheelHeightSelectPicker dataPicker = (WheelHeightSelectPicker) dialog.findViewById(R.id.wheel_data_height);
        dataPicker.setPadding(0, 0, 0, 0);
        dataPicker.setTextColor(getResources().getColor(R.color.colorWhite50));
        dataPicker.setCurrentTextColor(getResources().getColor(R.color.colorPurple));
        dataPicker.setTextSize(textSize);
        dataPicker.setItemSpace(itemSpace);
        dataPicker.setCurrentData(170, "cm");
        dataPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(View view, int index, String data) {
                date = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (state != AbstractWheelPicker.SCROLL_STATE_IDLE) {
                    select_confirm.setEnabled(false);
                } else {
                    select_confirm.setEnabled(true);
                }
            }
        });

        dialog.show();
        CommonUtils.setDialogFullScreen(this, dialog);

    }

    /**
     * 选择重量
     */
    private void SelectWeight() {
        dialog = new Dialog(this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_select_weight);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.other_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        select_confirm = (ImageView) dialog.findViewById(R.id.select_confirm);
        select_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交
                add_user_weight.setText(date);
                dialog.cancel();
            }
        });

        padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        textSize = getResources().getDimensionPixelSize(R.dimen.TextSizeLarge);
        itemSpace = getResources().getDimensionPixelSize(R.dimen.ItemSpaceLarge);
        WheelWeightSelectPicker dataPicker = (WheelWeightSelectPicker) dialog.findViewById(R.id.wheel_data_weight);
        dataPicker.setPadding(0, 0, 0, 0);
        dataPicker.setTextColor(getResources().getColor(R.color.colorWhite50));
        dataPicker.setCurrentTextColor(getResources().getColor(R.color.colorPurple));
        dataPicker.setTextSize(textSize);
        dataPicker.setItemSpace(itemSpace);
        dataPicker.setCurrentData("70", "kg");
        dataPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(View view, int index, String data) {
                date = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (state != AbstractWheelPicker.SCROLL_STATE_IDLE) {
                    select_confirm.setEnabled(false);
                } else {
                    select_confirm.setEnabled(true);
                }
            }
        });

        dialog.show();
        CommonUtils.setDialogFullScreen(this, dialog);

    }


}
