package com.ruoyi.web.controller.device;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import javax.annotation.Resource;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.device.domain.Device;
import com.ruoyi.device.service.IMonitorService;
import com.ruoyi.web.vo.DeviceControlVO;
import com.ruoyi.web.vo.SystemUsageVO;

/**
 * 主预览Controller
 *
 * @author hongrongjian
 * @date 2023/12/09
 */
@RestController
@RequestMapping("/camera/monitor")
public class MonitorController {

    @Resource
    private IMonitorService monitorService;

    @GetMapping("/getSystemResourceUsage")
    @Anonymous
    public SystemUsageVO getSystemResourceUsage() {
        return getSystemUsageVO();
    }
    /**
     * 获取系统资源，Cpu使用率，内存使用率，磁盘使用率
     *
     * @return {@link SystemUsageVO}
     */
    @NotNull
    private static SystemUsageVO getSystemUsageVO() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double cpuUsage = 0;
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            cpuUsage = sunOsBean.getProcessCpuLoad() * 100; // 获取当前进程的 CPU 使用率
        } else {
            System.out.println("不支持获取 CPU 使用率");
        }
        long totalMemory = Runtime.getRuntime().totalMemory(); // 总内存
        long freeMemory = Runtime.getRuntime().freeMemory(); // 空闲内存
        long usedMemory = totalMemory - freeMemory; // 已使用内存
        double memoryUsage = ((double) usedMemory / totalMemory) * 100; // 计算内存使用率
        File file = new File("/"); // 获取根目录磁盘空间使用情况
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        double usedSpacePercentage = (double) usedSpace / totalSpace * 100;
        double freeSpacePercentage = (double) freeSpace / totalSpace * 100;
        return new SystemUsageVO(cpuUsage, memoryUsage, usedSpacePercentage, freeSpacePercentage);
    }

    /**
     * 云台控制接口
     *
     * @param vo
     * @return {@link AjaxResult}
     * @throws InterruptedException
     */
    @PostMapping(value = "/control")
    public AjaxResult remoteControl(@RequestBody DeviceControlVO vo) throws InterruptedException {
        Device device = initDevice(vo);
        monitorService.remoteControl(device, vo.getCode());
        return AjaxResult.success("云台操作成功");
    }
    /**
     * 构造Device类对象
     *
     * @param vo
     * @return {@link Device}
     */
    @NotNull
    private static Device initDevice(DeviceControlVO vo) {
        Device device = new Device();
        device.setDeviceIp(vo.getDeviceIp());
        device.setDeviceUsername(vo.getDeviceUsername());
        device.setDevicePassword(vo.getDevicePassword());
        device.setDevicePort(vo.getDevicePort());
        return device;
    }

    /**
     * 抓图接口
     *
     * @param imageFile
     * @param deviceId
     * @return {@link AjaxResult}
     */
    @PostMapping("/saveImage")
    public AjaxResult saveImage(@RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("deviceId") String deviceId) {
        try {
            String screenshotRecordKey = monitorService.saveImage(imageFile, deviceId);
            return AjaxResult.success(screenshotRecordKey);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("保存图片失败");
        }
    }
}
