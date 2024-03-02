package com.ruoyi.web.vo;
public class SystemUsageVO {
    private double cpuUsage;
    private double memoryUsage;

    private double usedSpacePercentage;
    private double freeSpacePercentage;

    public SystemUsageVO() {
    }
    public SystemUsageVO(double cpuUsage, double memoryUsage, double usedSpacePercentage, double freeSpacePercentage) {
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.usedSpacePercentage = usedSpacePercentage;
        this.freeSpacePercentage = freeSpacePercentage;
    }
    public double getFreeSpacePercentage() {
        return freeSpacePercentage;
    }
    public void setFreeSpacePercentage(double freeSpacePercentage) {
        this.freeSpacePercentage = freeSpacePercentage;
    }
    public double getUsedSpacePercentage() {
        return usedSpacePercentage;
    }
    public void setUsedSpacePercentage(double usedSpacePercentage) {
        this.usedSpacePercentage = usedSpacePercentage;
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
