package com.mooring.mh.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.mooring.mh.app.InitApplicationHelper;

import org.xutils.http.RequestParams;

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
     * @param r
     * @param dp
     * @return
     */
    public static float dp2px(Resources r, float dp) {
        DisplayMetrics metrics = r.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * sp转换成px单位
     *
     * @param resources
     * @param sp
     * @return
     */
    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
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
}
