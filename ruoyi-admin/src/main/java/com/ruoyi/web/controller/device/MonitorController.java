package com.ruoyi.web.controller.device;
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

    @GetMapping(value = "/system/resource")
    public SystemUsageVO getSystemResourceUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double cpuUsage = osBean.getSystemLoadAverage() * 100; // 获取 CPU 使用率
        long totalMemory = Runtime.getRuntime().totalMemory(); // 总内存
        long freeMemory = Runtime.getRuntime().freeMemory(); // 空闲内存
        long usedMemory = totalMemory - freeMemory; // 已使用内存
        double memoryUsage = ((double) usedMemory / totalMemory) * 100; // 计算内存使用率
        // 还可以获取其他系统资源的使用情况，比如网络占用情况等
        return new SystemUsageVO(cpuUsage, memoryUsage);
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
