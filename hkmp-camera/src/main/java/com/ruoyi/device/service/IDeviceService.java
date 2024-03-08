package com.ruoyi.device.service;

import java.util.List;

import com.ruoyi.device.domain.Device;
import com.ruoyi.device.sdk.HCNetSDK;
import com.sun.jna.Pointer;

/**
 * 设备信息管理Service接口
 *
 * @author hongrongjian
 * @date 2023-10-23
 */
public interface IDeviceService {
    /**
     * 查询设备信息管理
     *
     * @param deviceId 设备信息管理主键
     * @return 设备信息管理
     */
    Device selectDeviceByDeviceId(Long deviceId);

    void updateDeviceStatus();

    /**
     * 查询设备信息管理列表
     *
     * @param device 设备信息管理
     * @return 设备信息管理集合
     */
    List<Device> selectDeviceList(Device device);

    /**
     * 新增设备信息管理
     *
     * @param device 设备信息管理
     * @return 结果
     */
    int insertDevice(Device device);

    /**
     * 修改设备信息管理
     *
     * @param device 设备信息管理
     * @return 结果
     */
    int updateDevice(Device device);

    /**
     * 批量删除设备信息管理
     *
     * @param deviceIds 需要删除的设备信息管理主键集合
     * @return 结果
     */
    int deleteDeviceByDeviceIds(Long[] deviceIds);

    /**
     * 删除设备信息管理信息
     *
     * @param deviceId 设备信息管理主键
     * @return 结果
     */
    int deleteDeviceByDeviceId(Long deviceId);

    void alarmDataHandle(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
            Pointer pUser);

    boolean setUpAlarmManually(Device device);

    boolean closeAlarmManually(Device device);
}
