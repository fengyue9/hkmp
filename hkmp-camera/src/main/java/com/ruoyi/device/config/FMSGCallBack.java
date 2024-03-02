package com.ruoyi.device.config;
import org.springframework.stereotype.Service;

import com.ruoyi.device.alarm.AlarmDataParse;
import com.ruoyi.device.sdk.HCNetSDK;
import com.sun.jna.Pointer;
@Service
public class FMSGCallBack implements HCNetSDK.FMSGCallBack {
    @Override
    public void invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
            Pointer pUser) {
        //报警处理
        AlarmDataParse.alarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
    }
}
