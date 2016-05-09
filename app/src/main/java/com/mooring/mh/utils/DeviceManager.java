package com.mooring.mh.utils;

import android.text.TextUtils;

import com.machtalk.sdk.domain.Device;
import com.mooring.mh.app.InitApplicationHelper;

import java.util.List;

/**
 * 设备管理类
 * <p/>
 * Created by Will on 16/5/12.
 */
public class DeviceManager {

    /**
     * 静态单独实例
     */
    private static DeviceManager manager;
    /**
     * 设备列表
     */
    private List<Device> mDeviceList;

    public static DeviceManager getInstance() {
        if (manager == null) {
            synchronized (DeviceManager.class) {
                if (manager == null) {
                    manager = new DeviceManager();
                }
            }
        }
        return manager;
    }

    /**
     * DeviceList set
     *
     * @param deviceList 设备列表
     */
    public void setDeviceList(List<Device> deviceList) {
        mDeviceList = deviceList;
    }

    /**
     * DeviceList get
     *
     * @return 设备列表
     */
    public List<Device> getDeviceList() {
        return mDeviceList;
    }

    /**
     * 获取指定id设备
     *
     * @param id 设备id
     * @return 设备
     */
    public Device getDevice(String id) {
        if (mDeviceList == null) {
            return null;
        }
        for (Device device : mDeviceList) {
            if (device.getId().equals(id)) {
                return device;
            }
        }
        return null;
    }

    /**
     * 获取当前Device
     *
     * @return 当前Device
     */
    public Device getCurrDevice() {
        String device_id = InitApplicationHelper.sp.getString(MConstants.DEVICE_ID, "");
        if (!TextUtils.isEmpty(device_id)) {
            return getDevice(device_id);
        }
        return null;
    }
}
