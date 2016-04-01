package com.mooring.mh.activity;

import android.app.Dialog;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.views.CircleImageView;
import com.mooring.mh.views.WheelPicker.AbstractWheelPicker;
import com.mooring.mh.views.WheelPicker.WheelDatePicker;

/**
 * Created by Will on 16/3/31.
 */
public class AddUserActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView imgView_add_user;
    private EditText add_user_name;
    private EditText add_user_sex;
    private EditText add_user_birthday;
    private EditText add_user_height;
    private EditText add_user_weight;
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
        add_user_sex = (EditText) findViewById(R.id.add_user_sex);
        add_user_birthday = (EditText) findViewById(R.id.add_user_birthday);
        add_user_height = (EditText) findViewById(R.id.add_user_height);
        add_user_weight = (EditText) findViewById(R.id.add_user_weight);
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

                break;
            case R.id.add_user_weight:

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
        setDialogFullScreen(dialog_choose_sex);
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
        setDialogFullScreen(dialog);
    }

    /**
     * 选择身高
     */
    private void SelectHeight() {


    }

    /**
     * 选择重量
     */
    private void SelectWeight() {


    }

    /**
     * 设置dialog全屏充满
     *
     * @param dialog
     */
    private void setDialogFullScreen(Dialog dialog) {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }
}