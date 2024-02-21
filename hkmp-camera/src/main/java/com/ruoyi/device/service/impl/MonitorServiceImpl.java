package com.ruoyi.device.service.impl;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.device.config.MinioConfig;
import com.ruoyi.device.domain.Device;
import com.ruoyi.device.domain.ScreenshotRecord;
import com.ruoyi.device.mapper.ScreenshotRecordMapper;
import com.ruoyi.device.sdk.HCNetSDK;
import com.ruoyi.device.service.IMonitorService;
import com.ruoyi.device.utils.CodeHandler;
import com.ruoyi.device.utils.LoginUtils;

/**
 * 主预览服务实现类
 *
 * @author Fy
 * @date 2024/02/21
 */
@Service
public class MonitorServiceImpl implements IMonitorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorServiceImpl.class);

    @Resource
    private HCNetSDK hcNetSDK;
    @Resource
    private MinioConfig minioConfig;
    @Resource
    private ScreenshotRecordMapper screenshotRecordMapper;
    /**
     * 云台控制
     *
     * @param device 设备类
     * @param code 操作代码
     */
    @Override
    public void remoteControl(Device device, Integer code) {
        //登录
        int userId = LoginUtils.login(device);
        LOGGER.info("登录成功，用户句柄为：" + userId);
        if (userId == -1) {
            int errorCode = hcNetSDK.NET_DVR_GetLastError();
            throw new IllegalStateException("登录失败,错误码为：" + errorCode);
        }
        //根据传入的code调用不同的code处理方法
        CodeHandler.handleCode(code, userId);
        //退出
        LoginUtils.logout(userId);
        LOGGER.info("退出成功，用户句柄为：" + userId);
    }

    /**
     *  保存图片
     *
     * @param imageFile
     * @param deviceId
     * @return {@link String}
     * @throws Exception
     */
    @Override
    public String saveImage(MultipartFile imageFile, String deviceId) throws Exception {
        try {
            minioConfig.putObject(imageFile);
        } catch (Exception e) {
            LOGGER.error("图片保存到minio失败");
            throw e;
        }
        //新增抓图记录
        ScreenshotRecord screenshotRecord = new ScreenshotRecord();
        screenshotRecord.setDeviceId(Long.valueOf(deviceId));
        screenshotRecord.setScreenshotKey(imageFile.getOriginalFilename());
        screenshotRecord.setScreenshotTime(new Date());
        int insertNum = screenshotRecordMapper.insertScreenshotRecord(screenshotRecord);
        if (insertNum != 1) {
            throw new ServiceException("新增抓图记录失败");
        }
        return screenshotRecord.getScreenshotKey();
    }

}
