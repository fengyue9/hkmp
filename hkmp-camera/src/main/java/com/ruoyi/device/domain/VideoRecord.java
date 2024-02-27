package com.ruoyi.device.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 回放对象 video_record
 *
 * @author hongrongjian
 * @date 2024-02-26
 */
public class VideoRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 设备id */
    @Excel(name = "设备id")
    private Long deviceId;

    /** 回放视频关键字 */
    @Excel(name = "回放视频关键字")
    private String recordingKey;

    /** 开始回放时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始回放时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束回放时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束回放时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 设备名称  */
    @Excel(name = "设备名称")
    private String deviceName;

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }
    public void setRecordingKey(String recordingKey) {
        this.recordingKey = recordingKey;
    }

    public String getRecordingKey() {
        return recordingKey;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

}
