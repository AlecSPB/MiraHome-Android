package com.mooring.mh.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.xutils.common.util.LogUtil;

/**
 * 网络工具类 包括移动数据网络和wifi
 * <p/>
 * Created by Will on 16/4/25.
 */
public class NetworkUtil {

    /**
     * 判断WIFI网络是否可用
     *
     * @param context 上下文
     * @return true 连接
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager cManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.isAvailable()) {
                return mWiFiNetworkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否打开
     *
     * @param context 上下文
     * @return true 打开
     */
    public static boolean isWifiOpened(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 获取当前ss_id
     *
     * @param context 上下文
     * @return wifi名称
     */
    public static String getSSID(Context context) {
        if (isWifiConnected(context)) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ss_id = wifiInfo.getSSID();
            if (ss_id != null) {
                LogUtil.e("Current wifi ss_id is " + ss_id);
                ss_id = ss_id.replaceAll("\"", "");
                if ("0x".equals(ss_id) || "<unknown ssid>".equalsIgnoreCase(ss_id)) {
                    ss_id = null;
                }
            }
            return ss_id;
        }
        return "";
    }

    /**
     * 判断网络是否可用
     *
     * @param context 上下文
     * @return true 可用
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.isConnected();
            }
        }
        return false;
    }
}
