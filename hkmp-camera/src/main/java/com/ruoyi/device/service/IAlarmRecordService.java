package com.ruoyi.device.service;
import java.util.List;

import com.ruoyi.device.domain.AlarmRecord;
import com.ruoyi.device.domain.Device;
import com.ruoyi.device.sdk.HCNetSDK;
import com.sun.jna.Pointer;
/**
 * 报警Service接口
 *
 * @author hongrongjian
 * @date 2023/12/10
 */
public interface IAlarmRecordService {

    /**
     * 建立布防上传通道, 返回lAlarmHandle句柄
     *
     * @return int
     @param device
     */
    int setupAlarmChan(Device device);

    void closeAlarmChan();

    void test();

    void alarmDataHandle(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
            Pointer pUser);

    /**
     * 查询报警记录
     *
     * @param deviceId 报警记录主键
     * @return 报警记录
     */
    public AlarmRecord selectAlarmRecordById(String deviceId);

    /**
     * 查询报警记录列表
     *
     * @param alarmRecord 报警记录
     * @return 报警记录集合
     */
    public List<AlarmRecord> selectAlarmRecordList(AlarmRecord alarmRecord);

    /**
     * 新增报警记录
     *
     * @param alarmRecord 报警记录
     * @return 结果
     */
    public int insertAlarmRecord(AlarmRecord alarmRecord);

    /**
     * 修改报警记录
     *
     * @param alarmRecord 报警记录
     * @return 结果
     */
    public int updateAlarmRecord(AlarmRecord alarmRecord);

    /**
     * 批量删除报警记录
     *
     * @param deviceIds 需要删除的报警记录主键集合
     * @return 结果
     */
    public int deleteAlarmRecordByDeviceIds(Long[] deviceIds);

    /**
     * 删除报警记录信息
     *
     * @param deviceId 报警记录主键
     * @return 结果
     */
    public int deleteAlarmRecordByDeviceId(Long deviceId);

}
