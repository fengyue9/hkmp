package com.ruoyi.device.domain;
/**
 * 设备登录DTO
 *
 * @author hongrongjian
 * @date 2023/12/10
 */
public class DeviceLoginDTO {
    /**
     * 登录方法返回的用户句柄
     * */
    private String loginId;

    /**
     * 设备用户名
     * */
    private String username;

    /**
     * 设备密码
     * */
    private String password;

    /**
     * 设备IP
     * */
    private String ip;

    /**
     * SDK端口，默认设备的8000端口
     * */
    private String port;
    public DeviceLoginDTO(String loginId, String username, String password, String ip, String port) {
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.port = port;
    }
    public DeviceLoginDTO() {
    }
    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DeviceLoginDTO{");
        sb.append("loginId='").append(loginId).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", ip='").append(ip).append('\'');
        sb.append(", port='").append(port).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
