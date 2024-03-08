package com.ruoyi.device.service.impl;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ruoyi.device.domain.Device;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.service.IAlarmRecordService;
import com.ruoyi.device.service.IAsyncService;
/**
 * 异步服务实现类
 *
 * @author Fy
 * @date 2024/03/01
 */
@Service
public class AsyncServiceImpl implements IAsyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncServiceImpl.class);

    //在线
    public static final String ONLINE_STATUS = "0";
    //离线
    public static final String OFFLINE_STATUS = "1";
    //已布防
    public static final String ALARMED_STATUS = "0";
    //未布防
    public static final String NOT_ALARMED_STATUS = "1";
    //手动布防
    public static final String MANUAL_ALARM = "0";
    //自动布防
    public static final String AUTO_ALARM = "1";

    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private IAlarmRecordService alarmRecordService;

    @Override
    @Async
    public void checkDeviceOnlineAndAlarmed() {
        // 查询出所有启用设备信息
        List<Device> deviceList = deviceMapper.selectDeviceList(new Device());

        for (Device device : deviceList) {
            String alarmStatus = device.getAlarmStatus();
            String manualAlarmStatus = device.getManualAlarmStatus();
            boolean isOnline = isCameraOnline(device.getDeviceIp());

            try {
                if (!isOnline) {
                    handleOfflineDevice(device);
                } else {
                    handleOnlineDevice(device, alarmStatus, manualAlarmStatus);
                }
            } catch (Exception e) {
                // 处理异常情况，记录日志或者进行其他处理
                LOGGER.error("处理设备时发生异常：" + e.getMessage());
            }
        }
        //        //查询出所有启用设备信息
        //        List<Device> deviceList = deviceMapper.selectDeviceList(new Device());
        //        //循环ping
        //        for (Device device : deviceList) {
        //            Long deviceId = device.getDeviceId();
        //            String alarmStatus = device.getAlarmStatus();
        //            String manualAlarmStatus = device.getManualAlarmStatus();
        //            //判断设备是否在线
        //            if (!isCameraOnline(device.getDeviceIp())) {
        //                //离线
        //                deviceMapper.updateDeviceStatus(deviceId, OFFLINE_STATUS);
        //                //设置成未布防
        //                deviceMapper.updateAlarmStatus(deviceId, NOT_ALARMED_STATUS);
        //                //设置成自动布防，下次连接后会自动布防
        //                deviceMapper.updateManualAlarmStatus(deviceId, AUTO_ALARM);
        //            } else {
        //                //在线
        //                deviceMapper.updateDeviceStatus(deviceId, ONLINE_STATUS);
        //                if (manualAlarmStatus.equals(MANUAL_ALARM)) {
        //                    // 如果手动布防状态
        //                    if (alarmStatus.equals(NOT_ALARMED_STATUS)) {
        //                        //未布防
        //                        continue;
        //                    } else {
        //                        //需要布防
        //                        if (alarmRecordService.setupAlarmChan(device)) {
        //                            //布防成功
        //                            deviceMapper.updateAlarmStatus(deviceId, ALARMED_STATUS);
        //                        } else {
        //                            //布防失败
        //                            deviceMapper.updateAlarmStatus(deviceId, NOT_ALARMED_STATUS);
        //                        }
        //                    }
        //                } else {
        //                    //自动布防
        //                    if (alarmRecordService.setupAlarmChan(device)) {
        //                        //布防成功
        //                        deviceMapper.updateAlarmStatus(deviceId, ALARMED_STATUS);
        //                    } else {
        //                        //布防失败
        //                        deviceMapper.updateAlarmStatus(deviceId, NOT_ALARMED_STATUS);
        //                    }
        //                }
        //            }
        //        }
    }
    /**
     * 处理离线设备
     *
     * @param device
     */
    private void handleOfflineDevice(Device device) {
        Long deviceId = device.getDeviceId();
        if (device.getAlarmStatus().equals(ALARMED_STATUS)) {
            //需要撤防
            if (alarmRecordService.closeAlarmChan(device)) {
                //撤防成功
                deviceMapper.updateAlarmStatus(deviceId, NOT_ALARMED_STATUS);
            }
        }
        deviceMapper.updateManualAlarmStatus(deviceId, AUTO_ALARM);
        deviceMapper.updateDeviceStatus(deviceId, OFFLINE_STATUS);
    }

    /**
     * 处理在线设备
     * @param device
     * @param alarmStatus
     * @param manualAlarmStatus
     *
     *
     * @author Fy
     * @date 2024/03/08
     */
    private void handleOnlineDevice(Device device, String alarmStatus, String manualAlarmStatus) {
        Long deviceId = device.getDeviceId();
        deviceMapper.updateDeviceStatus(deviceId, ONLINE_STATUS);

        if (manualAlarmStatus.equals(MANUAL_ALARM)) {
            if (alarmStatus.equals(ALARMED_STATUS)) {
                //检查是否真的布防
                Integer alarmId = Device.alarmMap.get(deviceId);
                if (alarmId != null) {
                    //已经布防
                    return;
                } else {
                    //目前没有布防,需要布防
                    alarmRecordService.setupAlarmChan(device);
                }
            }
            // 如果手动布防状态且未布防，则不执行布防操作
        }
        //自动布防 && 未布防
        if (manualAlarmStatus.equals(AUTO_ALARM) && alarmStatus.equals(NOT_ALARMED_STATUS)) {
            if (alarmRecordService.setupAlarmChan(device)) {
                deviceMapper.updateAlarmStatus(deviceId, ALARMED_STATUS);
            }
        }
    }
    /**
     * 通过ping的方式检查摄像头是否在线
     *
     * @param ipAddress
     * @return boolean
     */
    private boolean isCameraOnline(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            return address.isReachable(5000); // Timeout in milliseconds
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
