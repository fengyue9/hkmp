package com.ruoyi.device.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 抓图记录对象 screenshot_record
 *
 * @author hongrongjian
 * @date 2024-02-08
 */
public class ScreenshotRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 设备id */
    @Excel(name = "设备id")
    private Long deviceId;

    /** 抓图关键字 */
    @Excel(name = "抓图关键字")
    private String screenshotKey;

    /** 抓图时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "抓图时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date screenshotTime;

    /** 抓图URL  */
    private String screenshotURL;

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }
    public void setScreenshotKey(String screenshotKey) {
        this.screenshotKey = screenshotKey;
    }

    public String getScreenshotKey() {
        return screenshotKey;
    }
    public void setScreenshotTime(Date screenshotTime) {
        this.screenshotTime = screenshotTime;
    }

    public Date getScreenshotTime() {
        return screenshotTime;
    }

    public String getScreenshotURL() {
        return screenshotURL;
    }
    public void setScreenshotURL(String screenshotURL) {
        this.screenshotURL = screenshotURL;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScreenshotRecord{");
        sb.append("deviceId=").append(deviceId);
        sb.append(", screenshotKey='").append(screenshotKey).append('\'');
        sb.append(", screenshotTime=").append(screenshotTime);
        sb.append(", screenshotURL='").append(screenshotURL).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
