package com.ruoyi.web.vo;
public class SystemUsageVO {
    private double cpuUsage;
    private double memoryUsage;
    public SystemUsageVO(double cpuUsage, double memoryUsage) {
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
    }
    public SystemUsageVO() {
    }
    public double getCpuUsage() {
        return cpuUsage;
    }
    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }
    public double getMemoryUsage() {
        return memoryUsage;
    }
    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SystemUsageVO{");
        sb.append("cpuUsage=").append(cpuUsage);
        sb.append(", memoryUsage=").append(memoryUsage);
        sb.append('}');
        return sb.toString();
    }
}
