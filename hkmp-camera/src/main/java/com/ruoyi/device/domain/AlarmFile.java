package com.ruoyi.device.domain;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
/**
 *
 *
 * @author Fy
 * @date 2024/03/05
 */
public class AlarmFile extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 报警记录id  */
    @Excel(name = "报警记录id")
    private String alarmRecordId;

    /** 报警文件关键字  */
    private String alarmFileKey;

    /** 记录时间 */
    @Excel(name = "报警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    public String getAlarmRecordId() {
        return alarmRecordId;
    }
    public void setAlarmRecordId(String alarmRecordId) {
        this.alarmRecordId = alarmRecordId;
    }
    public String getAlarmFileKey() {
        return alarmFileKey;
    }
    public void setAlarmFileKey(String alarmFileKey) {
        this.alarmFileKey = alarmFileKey;
    }

    public Date getRecordTime() {
        return recordTime;
    }
    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }
}
