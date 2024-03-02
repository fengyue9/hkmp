package com.ruoyi.device.config;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ruoyi.device.sdk.HCNetSDK;
import com.ruoyi.device.service.IAlarmRecordService;
import com.sun.jna.Pointer;

/**
 * 报警事件异常回调函数
 *
 * @author Fy
 * @date 2024/02/29
 */
@Service
public class FMSGCallBack_V31 implements HCNetSDK.FMSGCallBack_V31 {
    private final AtomicBoolean alarmHandled = new AtomicBoolean(false);

    @Resource
    private IAlarmRecordService alarmService;
    //报警信息回调函数
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
            Pointer pUser) {
        System.out.println("FMSGCallBack_V31 回调到信息");
        if (alarmHandled.compareAndSet(false, true)) {
            // 只有第一个到达的线程会执行这里的代码，其他线程会直接返回
            alarmService.alarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 处理完报警后将 alarmHandled 设置为 false
            alarmHandled.set(false);
            return true;
        }
        return true; // 其他线程直接返回true，表示处理成功
    }
}
