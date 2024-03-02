package com.ruoyi.device.config;
import org.springframework.stereotype.Service;

import com.ruoyi.device.alarm.AlarmDataParse;
import com.ruoyi.device.sdk.HCNetSDK;
import com.sun.jna.Pointer;

/**
 * 报警事件异常回调函数
 *
 * @author Fy
 * @date 2024/02/29
 */
@Service
public class FMSGCallBack_V31 implements HCNetSDK.FMSGCallBack_V31 {
    //报警信息回调函数
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
            Pointer pUser) {
        AlarmDataParse.alarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
        return true;
    }
}
