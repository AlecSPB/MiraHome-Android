package com.mooring.mh.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.xutils.common.util.LogUtil;

/**
 * Created by Will on 16/4/25.
 */
public class NetworkUtil {

    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.isAvailable()) {
                return mWiFiNetworkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否打开
     *
     * @param context
     * @return
     */
    public static boolean isWifiOpened(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 获取当前ssid
     *
     * @return
     */
    public static String getSSID(Context context) {
        if (isWifiConnected(context)) {
            WifiInfo wifiInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            if (ssid != null) {
                LogUtil.e("Current wifi ssid is " + ssid);
                ssid = ssid.replaceAll("\"", "");
                if ("0x".equals(ssid) || "<unknown ssid>".equalsIgnoreCase(ssid)) {
                    ssid = null;
                }
            }
            return ssid;
        }
        return "";
    }
}
