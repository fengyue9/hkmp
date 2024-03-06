package com.ruoyi.device.config;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ruoyi.device.sdk.HCNetSDK;
import com.ruoyi.device.service.IDeviceService;
import com.sun.jna.Pointer;

@Service
public class FMSGCallBack_V31 implements HCNetSDK.FMSGCallBack_V31 {

    @Resource
    private IDeviceService deviceService;

    // 报警信息回调函数
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
            Pointer pUser) {
        deviceService.alarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
        return true;

    }
}
