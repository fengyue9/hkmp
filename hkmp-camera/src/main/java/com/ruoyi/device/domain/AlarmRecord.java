package com.ruoyi.device.domain;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报警记录对象 alarm_record
 *
 * @author hongrongjian
 * @date 2024-03-02
 */
public class AlarmRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 报警记录id  */
    @Excel(name = "报警记录id")
    private String alarmRecordId;

    /** 设备id */
    @Excel(name = "设备id")
    private Long deviceId;

    /** 设备名称  */
    @Excel(name = "设备名称")
    private String deviceName;
    /** 报警时间 */
    @Excel(name = "报警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alarmTime;

    /** 报警类型 */
    @Excel(name = "报警类型")
    private String alarmType;

    /** 描述 */
    @Excel(name = "描述")
    private String alarmDesc;

    /** 报警事件状态 */
    @Excel(name = "报警事件状态 0:未处理，1:已处理")
    private String alarmStatus;

    /** 处理时间 */
    @Excel(name = "处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;
    /** 报警抓图关键字 */
    private String screenshotKey;

    /** 报警抓图URL */
    private String screenshotURL;
    public String getScreenshotURL() {
        return screenshotURL;
    }
    public void setScreenshotURL(String screenshotURL) {
        this.screenshotURL = screenshotURL;
    }
    public String getScreenshotKey() {
        return screenshotKey;
    }
    public void setScreenshotKey(String screenshotKey) {
        this.screenshotKey = screenshotKey;
    }
    public String getAlarmRecordId() {
        return alarmRecordId;
    }
    public void setAlarmRecordId(String alarmRecordId) {
        this.alarmRecordId = alarmRecordId;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }
    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }
    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAlarmDesc() {
        return alarmDesc;
    }
    public void setAlarmDesc(String alarmDesc) {
        this.alarmDesc = alarmDesc;
    }
    public String getAlarmStatus() {
        return alarmStatus;
    }
    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }
    public Date getHandleTime() {
        return handleTime;
    }
    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }
}