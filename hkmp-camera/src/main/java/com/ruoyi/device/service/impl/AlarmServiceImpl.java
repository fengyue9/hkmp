package com.ruoyi.device.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ruoyi.device.service.IAlarmService;
/**
 * 报警Service业务层
 *
 * @author hongrongjian
 * @date 2023/12/10
 */
@Service
public class AlarmServiceImpl implements IAlarmService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmServiceImpl.class);
//    @Resource
//    private HCNetSDK hcNetSDK;

//    @Resource
//    private HCNetSDK.FMSGCallBack fMSFCallBack;

    public static int lListenHandle = -1;//报警监听句柄

    /**
     * 建立布防上传通道
     *
     * @param userId
     * @param lAlarmHandle
     * @return int
     */
    @Override
    public int setupAlarmChan(int userId, int lAlarmHandle) {
        if (userId == -1) {
            LOGGER.error("请先登录");
            return userId;
        }
//        HCNetSDK.FMessageCallBack fMessageCallBack = new HCNetSDK.FMessageCallBack() {
//            int a;
//
//            @Override
//            public boolean invoke(int lCommand, String sDVRIP, String pBuf, int dwBufLen, int dwUser) {
//                a = lCommand;
//                return false;
//            }
//        };
        //        Pointer pointer = new Pointer();
        //        hcNetSDK.NET_DVR_SetDVRMessageCallBack_V50(0, fMessageCallBack, pointer);
        return 1;
    }



}

