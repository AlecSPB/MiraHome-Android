package com.mooring.mh.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.mooring.mh.app.InitApplicationHelper;

import org.xutils.http.RequestParams;

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
public class CommonUtils {

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
     * 使用SharedPreferences存储String值
     *
     * @param name
     * @param value
     */
    public static void addSP(String name, String value) {
        if (name != null && value != null) {
            SharedPreferences.Editor editor = InitApplicationHelper.sp.edit();
            editor.putString(name, value);
            editor.commit();
        }
    }

    /**
     * 使用SharedPreferences存储int值
     *
     * @param name
     * @param value
     */
    public static void addSP(String name, int value) {
        if (name != null) {
            SharedPreferences.Editor editor = InitApplicationHelper.sp.edit();
            editor.putInt(name, value);
            editor.commit();
        }
    }

    /**
     * 获取指定key和defValue的SharedPreferences值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getSP(String key, String defValue) {
        if (key != null && !"".equals(key)) {
            return InitApplicationHelper.sp.getString(key, defValue);
        }
        return null;
    }

    /**
     * 获取指定key的SharedPreferences值
     *
     * @param key
     * @return
     */
    public static String getSP(String key) {
        if (key != null && !"".equals(key)) {
            return InitApplicationHelper.sp.getString(key, "");
        }
        return null;
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
        RequestParams params = new RequestParams(MConstants.SERVICE_URL + uri);
        params.addBodyParameter("token", getSP("token"));
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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


}
