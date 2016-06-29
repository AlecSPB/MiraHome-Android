package com.mooring.mh.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.mooring.mh.BuildConfig;
import com.mooring.mh.R;
import com.mooring.mh.app.InitApplicationHelper;

import org.xutils.common.util.FileUtil;
import org.xutils.http.RequestParams;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共工具类
 * <p/>
 * Created by Will on 16/3/25.
 */
public class MUtils {

    public static File tempFile;//临时文件,用于相机或相册的图片存储
    public static String tempFileName = "";//对应相机或相册文件的文件全路径
    public static Dialog loading_dialog;//加载loading dialog

    /**
     * dp转换成px单位
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px2dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 判别邮箱 包含aa@qq.vip.com，aa@qq.com
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        // logger.info(m.matches()+"---");
        return m.matches();
    }

    /**
     * 判断是否是电话号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断密码是否符合
     *
     * @param psw
     * @return
     */
    public static boolean checkPsw(String psw) {
        String str = "[a-zA-Z0-9_]{8,20}";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(psw);
        return m.matches();
    }

    /**
     * 提示
     *
     * @param c
     * @param text
     */
    public static void showToast(Context c, String text) {
        Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取公共请求Param
     *
     * @param uri
     * @return
     */
    public static RequestParams getBaseParams(String uri) {
        RequestParams params = new RequestParams(uri);
        params.addBodyParameter(MConstants.SP_KEY_TOKEN,
                InitApplicationHelper.sp.getString(MConstants.SP_KEY_TOKEN, ""));
        return params;
    }

    /**
     * 设置dialog的内容全屏展示
     *
     * @param act
     * @param dialog
     */
    public static void setDialogFullScreen(Activity act, Dialog dialog) {
        WindowManager windowManager = act.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 创建指定大小的Bitmap
     *
     * @param bgimage
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) newWidth,
                (int) newHeight, matrix, true);
        return bitmap;
    }

    /**
     * 获取当天是周几
     *
     * @param pTime 2015-03-04
     * @return
     */
    public static String getWeek(String pTime) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "Sun";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "Mon";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "Tue";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "Wed";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "Thu";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "Fri";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "Sat";
        }
        return Week;
    }

    /**
     * 获取当然系统时间
     *
     * @return
     */
    public static String getCurrDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());//设置日期格式
        return df.format(new Date());
    }

    /**
     * 获取当然系统时间
     *
     * @return
     */
    public static String getCurrTime(String format) {

        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());//设置日期格式

        return df.format(new Date());
    }

    /**
     * 判断当前时间是都在指定区间内是白天
     *
     * @param before
     * @param after
     * @return true:白天
     */
    public static boolean judgeTimeInterval(String before, String after) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date d1 = sdf.parse(before);
            Date d2 = sdf.parse(after);
            long curr = System.currentTimeMillis();
            if (curr > d1.getTime() && curr < d2.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析时间城成08:00格式
     *
     * @param time
     * @return
     */
    public static String ParsingTime(String time) {
        if (time != null) {
            String s1 = time.substring(0, 2);
            String s2 = time.substring(2);
            return s1 + ":" + s2;
        }
        return "";
    }

    /**
     * 解析闹钟设定日期
     *
     * @param alarmDay
     * @return
     */
    public static ArrayList<String> ParsingDay(String alarmDay) {
        if (alarmDay == null) {
            return null;
        }
        ArrayList<String> daySet = new ArrayList<>();
        for (int i = 0; i < alarmDay.length(); i++) {
            daySet.add(String.valueOf(alarmDay.charAt(i)));
        }
        return daySet;
    }

    /**
     * 摄氏度转华氏度
     *
     * @param temperature
     * @return
     */
    public static float C2F(float temperature) {
        return (float) (32 + 1.8 * temperature);
    }

    /**
     * 华氏度转摄氏度
     *
     * @param temperature
     * @return
     */
    public static float F2C(float temperature) {
        return (temperature - 32) * 5 / 9;
    }

    /**
     * 相机获取图片
     *
     * @param mContext
     * @return 图片路径
     */
    public static void selectPicFromCamera(Activity mContext) {
        if (!FileUtil.existsSdcard()) {
            showToast(mContext, "SD卡不存在，不能拍照");
            return;
        }
        getTempFile(mContext);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        mContext.startActivityForResult(intent, MConstants.CAMERA_PHOTO);
    }

    /**
     * 相册获取图片
     *
     * @param mContext
     */
    public static void selectPicFromGallery(Activity mContext) {
        try {
            getTempFile(mContext);
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            mContext.startActivityForResult(intent, MConstants.GALLERY_PHOTO);
        } catch (ActivityNotFoundException e) {
            showToast(mContext, "没有找到相册");
        }
    }

    /**
     * 剪裁图片
     */
    public static void cropImg(Activity activity, Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 300);
        intent.putExtra("private boolean isZh() {\n" +
                "Locale locale = getResources().getConfiguration().locale;\n" +
                "String language = locale.getLanguage();\n" +
                "if (language.endsWith(\"zh\"))\n" +
                "return true;\n" +
                "else\n" +
                "return false;\n" +
                "}", 300);
        // 图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        activity.startActivityForResult(intent, MConstants.CROP_PHOTO);
    }

    /**
     * 获取文件路径
     *
     * @param context
     * @return android/data/...
     */
    public static String getFilePath(Context context, int type) {
        String path;
        if (FileUtil.existsSdcard()) {
            path = context.getExternalFilesDir(null).getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        switch (type) {
            case 1:
                path += File.separator + "userHead" + File.separator;
                break;
            case 2:
                path += File.separator + "dbFile" + File.separator;
                break;
        }

        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return path;
    }

    /**
     * 删除非指定名称以外的文件
     *
     * @param file
     * @param name
     */
    public static void deleteNonNameFile(File file, String name) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile() && !file.getName().equals(name)) { // 判断是否是文件且是否是指定名称
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteNonNameFile(files[i], name); // 把每个文件 用这个方法进行迭代
                }
            }
        }
    }

    /**
     * 删除非指定名称以外的文件
     *
     * @param file
     * @param name
     */
    public static void deleteFileWithName(File file, String name) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile() && file.getName().equals(name)) { // 判断是否是文件且是否是指定名称
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteNonNameFile(files[i], name); // 把每个文件 用这个方法进行迭代
                }
            }
        }
    }

    /**
     * 获取temp文件
     *
     * @param context
     */
    public static void getTempFile(Context context) {
        if ("".equals(tempFileName)) {
            String path = getFilePath(context, 1);
            tempFileName = path + "user_head_" + System.currentTimeMillis() + ".jpg";
            tempFile = new File(tempFileName);
        }
    }

    /**
     * 跳转到设置界面的Dialog提示
     *
     * @param msg
     */
    public static void showGoSettingDialog(final Activity act, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setMessage(msg);
        builder.setTitle(act.getString(R.string.tip_access_request));
        builder.setPositiveButton(act.getString(R.string.tip_go_setting),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        act.startActivity(intent);
                        act.finish();
                    }
                });
        builder.setNegativeButton(act.getString(R.string.tv_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        act.finish();
                    }
                });
        builder.create().show();
    }

    /**
     * 获取当前系统的语言
     *
     * @param context
     * @return
     */
    public static String getLocalLanguageCode(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.getLanguage();
    }

    /**
     * 显示Loading的dialogView
     *
     * @param c
     */
    public static void showLoadingDialog(Context c) {
        if (loading_dialog != null && !loading_dialog.isShowing()) {
            loading_dialog.show();
            return;
        }
        loading_dialog = new Dialog(c, R.style.LoadingDialogStyle);
        loading_dialog.setContentView(R.layout.dialog_loading);
        loading_dialog.setCanceledOnTouchOutside(false);
        ImageView imageView = (ImageView) loading_dialog.findViewById(R.id.imgView_loading);
        AnimationDrawable _animation = (AnimationDrawable) imageView.getDrawable();
        _animation.start();
        loading_dialog.show();
    }


    /**
     * 隐藏已显示的loading dialog
     */
    public static void hideLoadingDialog() {
        if (loading_dialog != null && loading_dialog.isShowing()) {
            loading_dialog.dismiss();
            loading_dialog = null;
        }
    }

}
