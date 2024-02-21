package com.ruoyi.device.service;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.device.domain.Device;
/**
 * 主预览服务接口
 *
 * @author Fy
 * @date 2024/02/21
 */
public interface IMonitorService {

    /**
     * 云台控制
     *
     * @param device
     * @param code
     * @throws InterruptedException
     */
    void remoteControl(Device device, Integer code) throws InterruptedException;

    String saveImage(MultipartFile imageFile, String deviceId) throws Exception;
}
