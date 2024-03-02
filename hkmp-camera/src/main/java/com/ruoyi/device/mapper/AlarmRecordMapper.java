package com.ruoyi.device.mapper;
import java.util.List;

import com.ruoyi.device.domain.AlarmRecord;
/**
 * 报警记录Mapper接口
 *
 * @author hongrongjian
 *
 @date 2024/03/02
 */
public interface AlarmRecordMapper {


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
     * 删除报警记录
     *
     * @param deviceId 报警记录主键
     * @return 结果
     */
    public int deleteAlarmRecordByDeviceId(Long deviceId);

    /**
     * 批量删除报警记录
     *
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAlarmRecordByDeviceIds(Long[] deviceIds);

    AlarmRecord selectAlarmRecordById(String alarmRecordId);
}
