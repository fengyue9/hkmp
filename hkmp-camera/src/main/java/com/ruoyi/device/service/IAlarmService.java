package com.ruoyi.device.service;
/**
 * 报警Service接口
 *
 * @author hongrongjian
 * @date 2023/12/10
 */
public interface IAlarmService {

    /**
     * 建立布防上传通道
     *
     * @param userId
     * @param lAlarmHandle
     * @return int
     */
    int setupAlarmChan(int userId, int lAlarmHandle);

}
