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

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    @Async
    public void checkDeviceOnline() {
        //查询出所有启用设备信息
        List<Device> deviceList = deviceMapper.selectDeviceList(new Device());
        //循环ping
        for (Device device : deviceList) {
            String deviceId = String.valueOf(device.getDeviceId());
            if (isCameraOnline(device.getDeviceIp())) {
                //在线
                //仅当状态不对时才更新
                if (!device.getDeviceStatus().equals("0")) {
                    deviceMapper.updateDeviceStatus(deviceId, "0");
                    LOGGER.info(" 设备id：" + deviceId + "在线！");
                }
            } else {
                //离线
                //仅当状态不对时才更新
                if (!device.getDeviceStatus().equals("1")) {
                    deviceMapper.updateDeviceStatus(deviceId, "1");
                    LOGGER.info(" 设备id：" + deviceId + "离线！");
                }
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
