package com.ruoyi.device.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.device.domain.Device;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.sdk.HCNetSDK;
import com.ruoyi.device.service.IAlarmRecordService;
import com.ruoyi.device.service.IAsyncService;
import com.ruoyi.device.service.IDeviceService;
import com.ruoyi.framework.websocket.WebSocketUsers;
import com.sun.jna.Pointer;

/**
 * 设备信息管理Service业务层处理
 *
 * @author hongrongjian
 * @date 2023-10-23
 */
@Service
public class DeviceServiceImpl implements IDeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServiceImpl.class);
    public static boolean alarmStatus = false;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private IAsyncService asyncService;
    @Resource
    private IAlarmRecordService alarmRecordService;

    /**
     * 更新设备在线状态 每3s执行一次
     *
     */
    @Scheduled(cron = "0/3 * * * * ?")
    @Override
    public void updateDeviceStatus() {
        //检查设备在线状态以及布防状态
        asyncService.checkDeviceOnlineAndAlarmed();
        //给前端发送消息
        WebSocketUsers.sendMessageToUsersByText("updateDeviceStatus");
    }

    /**
     * 查询设备信息管理
     *
     * @param deviceId 设备信息管理主键
     * @return 设备信息管理
     */
    @Override
    public Device selectDeviceByDeviceId(Long deviceId) {
        return deviceMapper.selectDeviceByDeviceId(deviceId);
    }

    /**
     * 查询设备信息管理列表
     *
     * @param device 设备信息管理
     * @return 设备信息管理
     */
    @Override
    public List<Device> selectDeviceList(Device device) {
        return deviceMapper.selectDeviceList(device);
    }

    /**
     * 新增设备信息管理
     *
     * @param device 设备信息管理
     * @return 结果
     */
    @Override
    public int insertDevice(Device device) {
        device.setCreateTime(DateUtils.getNowDate());
        return deviceMapper.insertDevice(device);
    }

    /**
     * 修改设备信息管理
     *
     * @param device 设备信息管理
     * @return 结果
     */
    @Override
    public int updateDevice(Device device) {
        device.setUpdateTime(DateUtils.getNowDate());
        return deviceMapper.updateDevice(device);
    }

    /**
     * 批量删除设备信息管理
     *
     * @param deviceIds 需要删除的设备信息管理主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByDeviceIds(Long[] deviceIds) {
        return deviceMapper.deleteDeviceByDeviceIds(deviceIds);
    }

    /**
     * 删除设备信息管理信息
     *
     * @param deviceId 设备信息管理主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByDeviceId(Long deviceId) {
        return deviceMapper.deleteDeviceByDeviceId(deviceId);
    }
    /**
     * 报警回调函数处理
     *
     * @param lCommand
     * @param pAlarmer
     * @param pAlarmInfo
     * @param dwBufLen
     * @param pUser
     */
    @Override
    @Async
    public void alarmDataHandle(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
            Pointer pUser) {
        if (!alarmStatus) {
            alarmStatus = true;
            // 执行处理报警的代码
            alarmRecordService.handleAlarm(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
            alarmStatus = false;
        }
    }
    @Override
    public void setUpAlarm(Device device) {
        Integer alarmId = Device.alarmMap.get(device.getDeviceId());
        if (alarmId == null) {
            //更新为手动布防

            //开始布防

        }

        //布防
    }

}
