package com.ruoyi.device.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.device.config.MinioConfig;
import com.ruoyi.device.domain.ScreenshotRecord;
import com.ruoyi.device.mapper.ScreenshotRecordMapper;
import com.ruoyi.device.service.IScreenshotRecordService;

/**
 * 抓图记录Service业务层处理
 *
 * @author hongrongjian
 * @date 2024-02-08
 */
@Service
public class ScreenshotRecordServiceImpl implements IScreenshotRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotRecordServiceImpl.class);

    @Autowired
    private ScreenshotRecordMapper screenshotRecordMapper;
    @Resource
    private MinioConfig minioConfig;

    /**
     * 查询抓图记录
     *
     * @param deviceId 抓图记录主键
     * @return 抓图记录
     */
    @Override
    public ScreenshotRecord selectScreenshotRecordByDeviceId(Long deviceId) {
        //        return screenshotRecordMapper.selectScreenshotRecordByDeviceId(deviceId);
        return null;
    }

    /**
     * 查询抓图记录列表
     *
     * @param screenshotRecord 抓图记录
     * @return 抓图记录
     */
    @Override
    public List<ScreenshotRecord> selectScreenshotRecordList(ScreenshotRecord screenshotRecord) {
        List<ScreenshotRecord> screenshotRecords = screenshotRecordMapper.selectScreenshotRecordList(screenshotRecord);
        for (ScreenshotRecord record : screenshotRecords) {
            String screenshotKey = record.getScreenshotKey();
            String objectUrl = "";
            try {
                objectUrl = minioConfig.getObjectUrl("hkmp", screenshotKey);
            } catch (Exception e) {
                LOGGER.error("从minio下载出现异常:" + e.getMessage());
            }
            record.setScreenshotURL(objectUrl);
        }
        return screenshotRecords;
    }

    /**
     * 新增抓图记录
     *
     * @param screenshotRecord 抓图记录
     * @return 结果
     */
    @Override
    public int insertScreenshotRecord(ScreenshotRecord screenshotRecord) {
        return screenshotRecordMapper.insertScreenshotRecord(screenshotRecord);
    }

    /**
     * 修改抓图记录
     *
     * @param screenshotRecord 抓图记录
     * @return 结果
     */
    @Override
    public int updateScreenshotRecord(ScreenshotRecord screenshotRecord) {
        return screenshotRecordMapper.updateScreenshotRecord(screenshotRecord);
    }

    /**
     * 批量删除抓图记录
     *
     * @return 结果
     @param screenshotKey
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteScreenshotRecordByScreenshotKey(String screenshotKey) {
        try {
            minioConfig.removeObject("hkmp", screenshotKey);
        } catch (Exception e) {
            LOGGER.error("从minio删除对象失败:" + e.getMessage());
        }
        return screenshotRecordMapper.deleteScreenshotRecordByScreenshotKeys(screenshotKey);
    }

    /**
     * 删除抓图记录信息
     *
     * @param deviceId 抓图记录主键
     * @return 结果
     */
    @Override
    public int deleteScreenshotRecordByDeviceId(Long deviceId) {
        return screenshotRecordMapper.deleteScreenshotRecordByDeviceId(deviceId);
    }
    @Override
    public void download(String screenshotKey, HttpServletResponse response) {
        minioConfig.download(screenshotKey, response);
    }

    /**
     * 每天凌晨1点执行删除过期（7天前）抓图记录的定时任务
     *
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void deleteExpiredScreenshots() {
        // 获取当前日期
        Date currentDate = new Date();
        // 创建一个 Calendar 对象，并设置为当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        // 减去30天，得到过期日期
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date expirationDate = calendar.getTime();
        // 查询30天前的抓图记录
        ScreenshotRecord screenshotRecord = new ScreenshotRecord();
        List<ScreenshotRecord> screenshotRecordList = screenshotRecordMapper.selectScreenshotRecordList(
                screenshotRecord);
        // 遍历抓图记录列表，删除过期记录
        for (ScreenshotRecord record : screenshotRecordList) {
            Date screenshotTime = record.getScreenshotTime();
            String screenshotKey = record.getScreenshotKey();
            // 如果抓图时间早于30天前的时间，执行删除操作
            if (screenshotTime != null && screenshotTime.before(expirationDate)) {
                try {
                    minioConfig.removeObject("hkmp", screenshotKey);
                } catch (Exception e) {
                    LOGGER.error("从minio删除对象失败:" + e.getMessage());
                }
                screenshotRecordMapper.deleteScreenshotRecordByScreenshotKeys(screenshotKey);
            }
        }
    }

}
