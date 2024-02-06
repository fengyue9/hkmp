package com.ruoyi.device.service;
import com.ruoyi.device.domain.DeviceLoginDTO;
/**
 * 海康SDKService接口
 *
 * @author hongrongjian
 * @date 2023/12/10
 */
public interface IHKSDKService {
    /**
     * 摄像头登录方法
     * @param loginDTO
     * @return {@code String }
     *
     * @author hongrongjian
     * @date 2023/12/10
     */
    String login(DeviceLoginDTO loginDTO);
}
