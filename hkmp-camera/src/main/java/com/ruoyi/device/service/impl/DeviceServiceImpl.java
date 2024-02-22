package com.ruoyi.device.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.device.domain.Device;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.sdk.HCNetSDK;
import com.ruoyi.device.service.IDeviceService;
import com.ruoyi.device.utils.LoginUtils;
import com.ruoyi.framework.websocket.WebSocketUsers;

/**
 * 设备信息管理Service业务层处理
 *
 * @author hongrongjian
 * @date 2023-10-23
 */
@Service
public class DeviceServiceImpl implements IDeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private HCNetSDK hcNetSDK;


    /**
     * 更新设备在线状态 每5s执行一次
     *
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void updateDeviceStatus() {
        //查询出所有启用设备信息
        List<Device> deviceList = deviceMapper.selectDeviceList(new Device());
        for (Device device : deviceList) {
            int userId = LoginUtils.login(device);
            if (userId == -1) {
                //登录失败, 将状态设置成离线
                device.setDeviceStatus("1");
                deviceMapper.updateDevice(device);
                LOGGER.info("设备id: " + device.getDeviceId() + " 离线!");
            } else {
                //登录成功，将状态设置成在线
                device.setDeviceStatus("0");
                deviceMapper.updateDevice(device);
                LOGGER.info("设备id: " + device.getDeviceId() + " 在线!");
                LoginUtils.logout(userId);
            }
        }
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
}
