//package com.ruoyi.device.config;
//import org.springframework.stereotype.Service;
//
//import com.ruoyi.device.alarm.AlarmDataParse;
//import com.ruoyi.device.sdk.HCNetSDK;
//import com.sun.jna.Pointer;
//@Service
//public class FMSGCallBack implements HCNetSDK.FMSGCallBack {
//    @Override
//    public void invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
//            Pointer pUser) {
//        System.out.println("FMSGCallBack 回调到信息");
//        System.out.println("lCommand = " + lCommand);
//        System.out.println("pAlarmer = " + pAlarmer);
//        System.out.println("pAlarmInfo = " + pAlarmInfo);
//        System.out.println("dwBufLen = " + dwBufLen);
//        //报警处理
//        AlarmDataParse.alarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
//    }
//}
