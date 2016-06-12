package com.mooring.mh.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.db.DbXUtils;
import com.mooring.mh.db.User;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.mooring.mh.utils.NetworkUtil;
import com.mooring.mh.views.WheelPicker.AbstractWheelPicker;
import com.mooring.mh.views.WheelPicker.widget.WheelDatePicker;
import com.mooring.mh.views.WheelPicker.widget.WheelHeightSelectPicker;
import com.mooring.mh.views.WheelPicker.widget.WheelWeightSelectPicker;

import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 用户信息查看和编辑页面
 * <p/>
 * Created by Will on 16/4/15.
 */
public class UserInfoActivity extends BaseActivity {

    private ImageView imgView_act_right;
    private ImageView imgView_add_user;
    private TextView tv_confirm;
    private TextView tv_on_mooring;
    private EditText edText_name;
    private TextView tv_sex;
    private TextView tv_birthday;
    private TextView tv_height;
    private TextView tv_weight;
    private int flag ;
    private int member_id;
    private int tempSex = 0, sex = 0;
    private DbManager dbManager;
    /**
     * 选择器中所需变量
     */
    private TextView male;
    private TextView female;
    private int padding;
    private int textSize;
    private int itemSpace;
    private ImageView select_confirm;
    private String date = "";
    private Dialog dialog;

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
        Intent it = getIntent();
        flag = it.getIntExtra(MConstants.ENTRANCE_FLAG,-1);
        if (flag == MConstants.USER_INFO_REQUEST) {
            member_id = it.getIntExtra(MConstants.SP_KEY_MEMBER_ID, -1);
        }
        DbManager.DaoConfig dao = DbXUtils.getDaoConfig(this);
        dbManager = x.getDb(dao);
    }

    @Override
    protected void initView() {

        imgView_act_right = (ImageView) findViewById(R.id.imgView_act_right);
        imgView_add_user = (ImageView) findViewById(R.id.imgView_add_user);
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
        imgView_add_user.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        tv_on_mooring.setOnClickListener(this);
        tv_sex.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        tv_height.setOnClickListener(this);
        tv_weight.setOnClickListener(this);

        if (flag == MConstants.USER_INFO_REQUEST) {
            tv_on_mooring.setVisibility(View.VISIBLE);
            imgView_act_right.setVisibility(View.VISIBLE);
            tv_act_title.setText(getString(R.string.title_user_info));
        }
        if (flag == MConstants.ADD_USER_REQUEST) {
            tv_on_mooring.setVisibility(View.GONE);
            imgView_act_right.setVisibility(View.GONE);
            tv_act_title.setText(getString(R.string.title_add_user));
        }
    }

    @Override
    protected void OnClick(View v) {
        switch (v.getId()) {
            case R.id.imgView_act_right:
                //删除
                deleteUser();
                break;
            case R.id.tv_confirm:
                //confirm
                if (flag == MConstants.ADD_USER_REQUEST) {
                    addUser();
                } else {
                    editUser();
                }
                break;
            case R.id.tv_on_mooring:
                sleepOnMooring();
                break;
            case R.id.imgView_add_user:
                SelectHeadImg();
                break;
            case R.id.tv_sex:
                SelectSex();
                break;
            case R.id.tv_birthday:
                SelectBirthday();
                break;
            case R.id.tv_height:
                SelectHeight();
                break;
            case R.id.tv_weight:
                SelectWeight();
                break;
        }
    }

    /**
     * 添加成员
     */
    private void addUser() {
        RequestParams params = MUtils.getBaseParams(MConstants.SERVICE_URL + MConstants.MEMBER);
        params.addParameter("member_name", edText_name.getText().toString().trim());
        params.addParameter("gender", sex);
        params.addParameter("birth_date", tv_birthday.getText().toString().trim());
        params.addParameter("height", tv_height.getText().toString().trim());
        params.addParameter("weight", tv_weight.getText().toString().trim());

        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null && result.optInt("code") == 0) {
                    JSONObject data = result.optJSONObject("data");
                    int member_id = data.optInt(MConstants.SP_KEY_MEMBER_ID);
                    User user = new User();
                    user.setId(member_id);
                    user.set_birthday(tv_birthday.getText().toString());
                    user.set_height(tv_height.getText().toString());
                    user.set_name(edText_name.getText().toString().trim());
                    user.set_sex(sex);
                    user.set_weight(tv_weight.getText().toString());
                    user.set_location(MConstants.BED_OUT);//添加成员默认不在床上
                    try {
                        dbManager.saveOrUpdate(user);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    Intent it = new Intent(context,CommonSuccessActivity.class);
                    it.putExtra(MConstants.ENTRANCE_FLAG,MConstants.ADD_USER_SUCCESS);
                    startActivity(it);
                } else {
                    MUtils.showToast(context, getString(R.string.error_add_failed));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MUtils.showToast(context, getString(R.string.error_add_failed));
            }

            @Override
            public void onCancelled(CancelledException cex) {
                MUtils.showToast(context, getString(R.string.error_add_cancel));
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 编辑成员
     */
    private void editUser() {
        RequestParams params = MUtils.getBaseParams(MConstants.SERVICE_URL + MConstants.MANAGE_MEMBER);
        params.addParameter("member_name", edText_name.getText().toString().trim());
        params.addParameter("gender", sex);
        params.addParameter("birth_date", tv_birthday.getText().toString().trim());
        params.addParameter("height", tv_height.getText().toString().trim());
        params.addParameter("weight", tv_weight.getText().toString().trim());

        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null && result.optInt("code") == 0) {
                    User user = new User();
                    user.setId(member_id);
                    user.set_birthday(tv_birthday.getText().toString());
                    user.set_height(tv_height.getText().toString());
                    user.set_name(edText_name.getText().toString().trim());
                    user.set_sex(sex);
                    user.set_weight(tv_weight.getText().toString());
                    user.set_header("");//当本地图片有的话设置,没有的话不设置
                    try {
                        dbManager.saveOrUpdate(user);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else {
                    MUtils.showToast(context, getString(R.string.error_edit_failed));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MUtils.showToast(context, getString(R.string.error_edit_failed));
            }

            @Override
            public void onCancelled(CancelledException cex) {
                MUtils.showToast(context, getString(R.string.error_edit_cancel));
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 删除成员
     */
    private void deleteUser() {
        RequestParams params = MUtils.getBaseParams(MConstants.SERVICE_URL + MConstants.MANAGE_MEMBER);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null && result.optInt("code") == 0) {
                    MUtils.showToast(context, getString(R.string.delete_user_success));
                    context.finish();
                } else {
                    MUtils.showToast(context, getString(R.string.error_delete_failed));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MUtils.showToast(context, getString(R.string.error_delete_failed));
            }

            @Override
            public void onCancelled(CancelledException cex) {
                MUtils.showToast(context, getString(R.string.error_delete_cancel));
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 选择将用户放置在左/右的位置上
     */
    private void sleepOnMooring() {
        dialog = new Dialog(this, R.style.CommonDialogStyle);
        dialog.setContentView(R.layout.dialog_sleep_on_mooring);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.tv_dialog_left).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //放在左边
                        try {
                            dbManager.update(User.class, WhereBuilder.b("id", "=", member_id),
                                    new KeyValue("_location", MConstants.BED_LEFT));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
        dialog.findViewById(R.id.tv_dialog_right).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //放在右边
                        try {
                            dbManager.update(User.class, WhereBuilder.b("id", "=", member_id),
                                    new KeyValue("_location", MConstants.BED_RIGHT));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 性别选择器,改变被选中文本颜色
     *
     * @param sex
     */
    private void showSexInSelector(int sex) {
        male.setTextColor(getResources().getColor(
                sex == 0 ? R.color.colorPurple : R.color.colorWhite50));
        female.setTextColor(getResources().getColor(
                sex == 0 ? R.color.colorWhite50 : R.color.colorPurple));
    }

    /**
     * 更改头像
     */
    private void SelectHeadImg() {
        dialog = new Dialog(this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_select_head);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.other_view).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
        dialog.findViewById(R.id.dialog_cancel).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
        // 拍照上传
        dialog.findViewById(R.id.choose_by_camera).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        MUtils.selectPicFromCamera(context);
                    }
                });
        // 相册上传
        dialog.findViewById(R.id.choose_by_local).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        MUtils.selectPicFromGallery(context);
                    }
                });
        dialog.show();
        MUtils.setDialogFullScreen(this, dialog);
    }

    /**
     * 选择性别
     */
    private void SelectSex() {
        dialog = new Dialog(this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_select_sex);
        dialog.setCanceledOnTouchOutside(true);
        male = (TextView) dialog.findViewById(R.id.sex_choose_male);
        female = (TextView) dialog.findViewById(R.id.sex_choose_female);
        View other_view = dialog.findViewById(R.id.sex_other_view);
        ImageView confirm = (ImageView) dialog.findViewById(R.id.imgView_chose_confirm);
        if (!TextUtils.isEmpty(tv_sex.getText().toString())) {
            showSexInSelector(sex);
        }
        other_view.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

        male.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showSexInSelector(tempSex = 0);
                    }
                });

        female.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showSexInSelector(tempSex = 1);
                    }
                });

        confirm.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        tv_sex.setText(getString(tempSex == 0 ? R.string.tv_male : R.string.tv_female));
                        sex = tempSex;
                        dialog.cancel();
                    }
                });
        dialog.show();
        MUtils.setDialogFullScreen(this, dialog);
    }

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
                tv_birthday.setText(date);
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
        MUtils.setDialogFullScreen(this, dialog);
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
                tv_height.setText(date);
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
        MUtils.setDialogFullScreen(this, dialog);

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
                tv_weight.setText(date);
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
        MUtils.setDialogFullScreen(this, dialog);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MConstants.CAMERA_PHOTO:// 照相机
                    MUtils.cropImg(this, Uri.fromFile(MUtils.tempFile));
                    break;
                case MConstants.GALLERY_PHOTO:// 相册(data2有返回值)
                    MUtils.cropImg(this, data.getData());
                    break;
                case MConstants.CROP_PHOTO:
                    if (NetworkUtil.isNetworkConnected(context)) {
                        imgView_add_user.setImageBitmap(BitmapFactory.decodeFile(MUtils.tempFileName));
                        editor.putString("user_head_path", MUtils.tempFileName).commit();
                    } else {
                        MUtils.showToast(context, getString(R.string.network_exception));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 上传头像,并且刷新现有头像
     */
    private void modifyHeadImg() {
        RequestParams param = MUtils.getBaseParams(MConstants.SERVICE_URL + MConstants.UPLOAD);
        param.addBodyParameter("Filedata", MUtils.tempFile, "image/jpeg");
        x.http().post(param, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                x.image().bind(imgView_add_user, MUtils.tempFile.getAbsolutePath());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
