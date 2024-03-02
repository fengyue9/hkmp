package com.ruoyi.device.service.impl;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import javax.annotation.Resource;

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
                System.out.println("======================");
                System.out.println("设备id：" + deviceId + "在线！");
                System.out.println("======================");
                deviceMapper.updateDeviceStatus(deviceId, "0");
            } else {
                //离线
                System.out.println("======================");
                System.out.println("设备id：" + deviceId + "离线！");
                System.out.println("======================");
                deviceMapper.updateDeviceStatus(deviceId, "1");
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
