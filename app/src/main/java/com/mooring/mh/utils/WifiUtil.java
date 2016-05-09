package com.mooring.mh.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 16/5/12.
 */
public class WifiUtil {

    public final static String TAG = WifiUtil.class.getSimpleName();
    // 定义WifiManager对象
    private WifiManager mWifiManager = null;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo = null;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList = null;
    // 扫描出的网络连接列表
    private List<String> mWifiSSIDList = null;
    // 定义一个WifiLock
    WifiManager.WifiLock mWifiLock = null;
    Context mContext = null;

    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID,
        WIFICIPHER_WPA_PSK_WPA2_PSK, WIFICIPHER_WPA_EAP_WPA2_EAP, WIFICIPHER_WPA_PSK,
        WIFICIPHER_WPA_EAP, WIFICIPHER_WPA2_PSK, WIFICIPHER_WPA2_EAP,
    }

    // 构造器
    public WifiUtil(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
        mContext = context;
        mWifiSSIDList = new ArrayList<>();
    }

    // 打开WIFI
    public boolean openWifi() {
        boolean enableFlag = true;

        if (!mWifiManager.isWifiEnabled()) {
            enableFlag = mWifiManager.setWifiEnabled(true);
        }
        return enableFlag;
    }

    // 关闭WIFI
    public boolean closeWifi() {
        boolean flag = true;

        if (mWifiManager.isWifiEnabled()) {
            flag = mWifiManager.setWifiEnabled(false);
        }
        return flag;
    }

    // 检查当前WIFI状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁WifiLock
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 创建一个WifiLock
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiManager.getConfiguredNetworks();
    }

    public void startScan() {
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();

        if (mWifiList != null) {
            mWifiSSIDList.clear();
            for (int i = 0; i < mWifiList.size(); i++) {
                mWifiSSIDList.add(mWifiList.get(i).SSID.toString());
            }
        }
    }

    /**
     * 连接wifi
     *
     * @param ssid
     * @param password
     * @return
     */
    public boolean connectWifi(String ssid, String password) {
        // 检测wifi是否开启
        if (!this.openWifi()) {
            return false;
        }

        // 如果未开启，等待
        while (checkState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }

        WifiConfiguration wifiConfig = isExsits(ssid);
        if (wifiConfig != null) {
            if (password != null) {
                mWifiManager.removeNetwork(wifiConfig.networkId);
            } else {
                return mWifiManager.enableNetwork(wifiConfig.networkId, true);
            }
        }

        startScan();

        // 获取wifi加密方式
        WifiCipherType type = getWifiTypeBySSID(ssid);
        LogUtil.i("WifiCipherType=" + type.name());
        if (type == WifiCipherType.WIFICIPHER_INVALID) {
            LogUtil.i("WifiCipherType:WIFICIPHER_INVALID" + type);
            return false;
        }

        // 创建wifi配置
        wifiConfig = CreateWifiInfo(ssid, password, type);
        if (wifiConfig == null) {
            LogUtil.i("CreateWifiInfo failed");
            return false;
        }

        // 清除已保存配置
        /*
         * WifiConfiguration tmpConfig = IsExsits(ssid); if (tmpConfig != null)
		 * { mWifiManager.removeNetwork(tmpConfig.networkId); }
		 */
        boolean flag = addNetwork(wifiConfig);
        if (flag) {
            mWifiInfo = mWifiManager.getConnectionInfo();
        }
        return flag;

    }

    /**
     * 根据ssid获取加密方式
     *
     * @param ssid
     * @return
     */
    public WifiCipherType getWifiTypeBySSID(String ssid) {
        WifiCipherType type = WifiCipherType.WIFICIPHER_INVALID;
        if (mWifiList != null) {
            String tmpSSID;
            for (ScanResult point : mWifiList) {
                tmpSSID = point.SSID.replaceAll("\"", "");
                if (tmpSSID.equals(ssid)) {
                    type = getWifiTypeByCapabilitiesBak(point.capabilities);
                    break;
                }
            }
        }
        return type;
    }

    public int checkWifiCipherIndex(String ssid) {
        int type = 0;
        if (mWifiList != null) {
            String tmpSSID;
            for (ScanResult point : mWifiList) {
                tmpSSID = point.SSID.replaceAll("\"", "");
                if (tmpSSID.equals(ssid)) {
                    type = getWifiCipherIndex(point.capabilities);
                    break;
                }
            }
        }
        return type;
    }

    public int getWifiCipherIndex(String capabilities) {
        // WifiCipherType type = WifiCipherType.WIFICIPHER_INVALID;
        boolean wep = capabilities.contains("WEP");
        boolean WpaPsk = capabilities.contains("WPA-PSK");
        boolean Wpa2Psk = capabilities.contains("WPA2-PSK");
        // boolean Wpa = capabilities.contains("WPA-EAP");
        // boolean Wpa2 = capabilities.contains("WPA2-EAP");

        if (wep) {
            return 1;
        }

        if (WpaPsk && Wpa2Psk) {
            return 4;
        } else if (Wpa2Psk) {
            return 3;
        } else if (WpaPsk) {
            return 2;
        }
        return 0;
    }

    public WifiCipherType getWifiTypeByCapabilitiesBak(String capabilities) {
        WifiCipherType type = WifiCipherType.WIFICIPHER_INVALID;
        boolean wep = capabilities.contains("WEP");
        boolean WpaPsk = capabilities.contains("WPA-PSK");
        boolean Wpa2Psk = capabilities.contains("WPA2-PSK");
        boolean Wpa = capabilities.contains("WPA-EAP");
        boolean Wpa2 = capabilities.contains("WPA2-EAP");

        if (wep) {
            type = WifiCipherType.WIFICIPHER_WEP;
            return type;
        }

        if (WpaPsk && Wpa2Psk) {
            type = WifiCipherType.WIFICIPHER_WPA_PSK_WPA2_PSK;
            return type;
        } else if (Wpa2Psk) {
            type = WifiCipherType.WIFICIPHER_WPA2_PSK;
            return type;
        } else if (WpaPsk) {
            type = WifiCipherType.WIFICIPHER_WPA_PSK;
            return type;
        }

        if (Wpa && Wpa2) {
            type = WifiCipherType.WIFICIPHER_WPA_EAP_WPA2_EAP;
            return type;
        } else if (Wpa2) {
            type = WifiCipherType.WIFICIPHER_WPA2_EAP;
            return type;
        } else if (Wpa) {
            type = WifiCipherType.WIFICIPHER_WPA_EAP;
            return type;
        }

        type = WifiCipherType.WIFICIPHER_NOPASS;
        return type;
    }

    /**
     * 检查当前ssid 对应的wifi是否可用
     *
     * @param SSID
     * @return SSID在wifi扫描列表中时返回true，否则返回false。
     */
    public boolean isSSIDEnable(String SSID) {
        if (SSID == null) {
            return false;
        }

        startScan();

        if (mWifiSSIDList.contains(SSID)) {
            return true;
        }
        return false;
    }

    /**
     * 检测wifi是否打开
     *
     * @return
     */
    public boolean isWifiEnable() {
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 从capabilities信息中提取加密方式
     *
     * @param capabilities
     * @return
     */
    public WifiCipherType getWifiTypeByCapabilities(String capabilities) {
        LogUtil.i("capabilities:" + capabilities);
        WifiCipherType type = WifiCipherType.WIFICIPHER_INVALID;
        boolean wep = capabilities.contains("WEP");
        boolean wpa = capabilities.contains("WPA");

        if (wep) {
            type = WifiCipherType.WIFICIPHER_WEP;
            return type;
        }

        if (wpa) {
            type = WifiCipherType.WIFICIPHER_WPA;
            return type;
        }

        type = WifiCipherType.WIFICIPHER_NOPASS;
        LogUtil.i("WifiCipherType:" + type);
        return type;
    }

    // 得到网络列表
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    public List<String> getmWifiSSIDList() {
        return mWifiSSIDList;
    }

    // 查看扫描结果
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder.append("Index_" + Integer.valueOf(i + 1) + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append(mWifiList.get(i).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    // 得到MAC地址
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    // 得到接入点的BSSID
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    // 得到IP地址
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    // 得到连接的ID
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 得到WifiInfo的所有信息包
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    public WifiInfo getCurrWifiInfo() {
        return mWifiManager.getConnectionInfo();
    }

    // 添加一个网络并连接
    public boolean addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        return mWifiManager.enableNetwork(wcgID, true);

    }

    // 断开指定ID的网络
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    /**
     * 创建wifi配置 TODO加密方式不全
     *
     * @param SSID
     * @param Password
     * @param type
     * @return
     */
    public WifiConfiguration CreateWifiInfo(String SSID, String Password, WifiCipherType type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.isExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        switch (type) {
            case WIFICIPHER_NOPASS: // WIFICIPHER_NOPASS
                // config.wepKeys[0] = "";
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                // config.wepTxKeyIndex = 0;
                break;
            case WIFICIPHER_WEP: // WIFICIPHER_WEP
                config.hiddenSSID = true;
                config.wepKeys[0] = "\"" + Password + "\"";
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
                break;

            case WIFICIPHER_WPA: // WIFICIPHER_WPA
                config.preSharedKey = "\"" + Password + "\"";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.status = WifiConfiguration.Status.ENABLED;
                break;
            case WIFICIPHER_WPA_PSK:
            case WIFICIPHER_WPA2_PSK:
            case WIFICIPHER_WPA_PSK_WPA2_PSK: // WIFICIPHER_WPA_PSK_WPA2_PSK
                config.preSharedKey = "\"" + Password + "\"";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.status = WifiConfiguration.Status.ENABLED;
                break;
            default:
                return null;
        }

        return config;
    }

    /**
     * 是否保存过ssid的wifi配置
     *
     * @param SSID
     * @return
     */
    public WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * 获取当前wifi 的SSID，未连接wifi返回空
     *
     * @return
     */
    public String getCurrentSSID() {
        if (checkState() == WifiManager.WIFI_STATE_ENABLED) {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
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
        return null;
    }

    /**
     * 连接指定ssid的wifi网络，若多次连接不成功，认为此wifi无法连接
     *
     * @param ssid
     * @param pwd
     * @return
     */
    public boolean connect(String ssid, String pwd) {
        int count = 0;
        while (!connectWifi(ssid, pwd) && count < 10) {
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (count < 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测指定的wifi是否连接成功 检测原理：用指定的ssid与正在连接的wifi ssid对比，一样即为成功
     *
     * @param ssid
     * @return
     */
    public boolean isConnectWifiSuccess(String ssid) {
        if (ssid == null)
            return false;
        int count = 0;
        while (!ssid.equals(getCurrentSSID()) && count < 10) {
            LogUtil.i("target ssid=" + ssid + " curSsid=" + getCurrentSSID());
            count++;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (count < 10 && isGetIpSuccess())
            return true;
        else
            return false;
    }

    public boolean isGetIpSuccess() {
        int count = 0;
        while (0 == getCurrWifiInfo().getIpAddress() && count < 10) {
            count++;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (count < 10)
            return true;
        else
            return false;
    }
}
