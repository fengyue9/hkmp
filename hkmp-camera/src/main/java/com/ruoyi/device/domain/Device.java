package com.ruoyi.device.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备信息管理对象 device
 * 
 * @author hongrongjian
 * @date 2023-10-23
 */
public class Device extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备id */
    private Long deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** ip地址 */
    @Excel(name = "ip地址")
    private String deviceIp;

    /** 端口 */
    @Excel(name = "端口")
    private String devicePort;

    /** 设备序列号 */
    @Excel(name = "设备序列号")
    private String deviceSerialNumber;

    /** 状态 */
    @Excel(name = "状态")
    private String deviceStatus;

    /** 用户名 */
    @Excel(name = "用户名")
    private String deviceUsername;

    /** 密码 */
    @Excel(name = "密码")
    private String devicePassword;

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }
    public void setDeviceIp(String deviceIp) 
    {
        this.deviceIp = deviceIp;
    }

    public String getDeviceIp() 
    {
        return deviceIp;
    }
    public void setDevicePort(String devicePort) 
    {
        this.devicePort = devicePort;
    }

    public String getDevicePort() 
    {
        return devicePort;
    }
    public void setDeviceSerialNumber(String deviceSerialNumber) 
    {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getDeviceSerialNumber() 
    {
        return deviceSerialNumber;
    }
    public void setDeviceStatus(String deviceStatus) 
    {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceStatus() 
    {
        return deviceStatus;
    }
    public void setDeviceUsername(String deviceUsername) 
    {
        this.deviceUsername = deviceUsername;
    }

    public String getDeviceUsername() 
    {
        return deviceUsername;
    }
    public void setDevicePassword(String devicePassword) 
    {
        this.devicePassword = devicePassword;
    }

    public String getDevicePassword() 
    {
        return devicePassword;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("deviceIp", getDeviceIp())
            .append("devicePort", getDevicePort())
            .append("deviceSerialNumber", getDeviceSerialNumber())
            .append("deviceStatus", getDeviceStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deviceUsername", getDeviceUsername())
            .append("devicePassword", getDevicePassword())
            .toString();
    }
}
