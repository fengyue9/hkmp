package com.ruoyi.device.domain;

import java.util.HashMap;
import java.util.Map;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备信息管理对象 device
 *
 * @author hongrongjian
 * @date 2023-10-23
 */
public class Device extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //记录在线用户Map  key为设备id，value为用户句柄
    public static Map<Long, Integer> userMap = new HashMap<>(10);

    //记录设备布防状态 key为设备id，value为布防句柄
    public static Map<Long, Integer> alarmMap = new HashMap<>(10);

    // 用于记录手动布防的设备 key为设备id value为是否手动布防,true:手动布防 false:自动布防
    public static Map<String, Boolean> manualAlarmMap = new HashMap<>();

    /** 设备id */
    @Excel(name = "设备id")
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

    /** 状态 0:在线 1:离线 */
    @Excel(name = "状态 0:在线 1:离线")
    private String deviceStatus;
    /** 报警状态 0:已布防 1:未布防 */
    @Excel(name = "报警状态 0:已布防 1:未布防")
    private String alarmStatus;

    /** 用户名 */
    @Excel(name = "用户名")
    private String deviceUsername;

    /** 密码 */
    @Excel(name = "密码")
    private String devicePassword;

    /**手动布防状态 0:手动布防 1:自动布防  */
    private String manualAlarmStatus;
    public String getManualAlarmStatus() {
        return manualAlarmStatus;
    }
    public void setManualAlarmStatus(String manualAlarmStatus) {
        this.manualAlarmStatus = manualAlarmStatus;
    }
    public String getAlarmStatus() {
        return alarmStatus;
    }
    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }
    public Long getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceIp() {
        return deviceIp;
    }
    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }
    public String getDevicePort() {
        return devicePort;
    }
    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }
    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }
    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }
    public String getDeviceStatus() {
        return deviceStatus;
    }
    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
    public String getDeviceUsername() {
        return deviceUsername;
    }
    public void setDeviceUsername(String deviceUsername) {
        this.deviceUsername = deviceUsername;
    }
    public String getDevicePassword() {
        return devicePassword;
    }
    public void setDevicePassword(String devicePassword) {
        this.devicePassword = devicePassword;
    }

}
