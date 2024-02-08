package com.ruoyi.device.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 抓图记录对象 screenshot_record
 * 
 * @author hongrongjian
 * @date 2024-02-08
 */
public class ScreenshotRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备id */
    private Long deviceId;

    /** 抓图关键字 */
    @Excel(name = "抓图关键字")
    private String screenshotKey;

    /** 抓图时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "抓图时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date screenshotTime;

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setScreenshotKey(String screenshotKey) 
    {
        this.screenshotKey = screenshotKey;
    }

    public String getScreenshotKey() 
    {
        return screenshotKey;
    }
    public void setScreenshotTime(Date screenshotTime) 
    {
        this.screenshotTime = screenshotTime;
    }

    public Date getScreenshotTime() 
    {
        return screenshotTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("screenshotKey", getScreenshotKey())
            .append("screenshotTime", getScreenshotTime())
            .toString();
    }
}
