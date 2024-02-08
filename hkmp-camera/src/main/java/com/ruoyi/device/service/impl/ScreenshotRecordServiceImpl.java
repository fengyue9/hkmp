package com.ruoyi.device.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.mapper.ScreenshotRecordMapper;
import com.ruoyi.device.domain.ScreenshotRecord;
import com.ruoyi.device.service.IScreenshotRecordService;

/**
 * 抓图记录Service业务层处理
 * 
 * @author hongrongjian
 * @date 2024-02-08
 */
@Service
public class ScreenshotRecordServiceImpl implements IScreenshotRecordService 
{
    @Autowired
    private ScreenshotRecordMapper screenshotRecordMapper;

    /**
     * 查询抓图记录
     * 
     * @param deviceId 抓图记录主键
     * @return 抓图记录
     */
    @Override
    public ScreenshotRecord selectScreenshotRecordByDeviceId(Long deviceId)
    {
        return screenshotRecordMapper.selectScreenshotRecordByDeviceId(deviceId);
    }

    /**
     * 查询抓图记录列表
     * 
     * @param screenshotRecord 抓图记录
     * @return 抓图记录
     */
    @Override
    public List<ScreenshotRecord> selectScreenshotRecordList(ScreenshotRecord screenshotRecord)
    {
        return screenshotRecordMapper.selectScreenshotRecordList(screenshotRecord);
    }

    /**
     * 新增抓图记录
     * 
     * @param screenshotRecord 抓图记录
     * @return 结果
     */
    @Override
    public int insertScreenshotRecord(ScreenshotRecord screenshotRecord)
    {
        return screenshotRecordMapper.insertScreenshotRecord(screenshotRecord);
    }

    /**
     * 修改抓图记录
     * 
     * @param screenshotRecord 抓图记录
     * @return 结果
     */
    @Override
    public int updateScreenshotRecord(ScreenshotRecord screenshotRecord)
    {
        return screenshotRecordMapper.updateScreenshotRecord(screenshotRecord);
    }

    /**
     * 批量删除抓图记录
     * 
     * @param deviceIds 需要删除的抓图记录主键
     * @return 结果
     */
    @Override
    public int deleteScreenshotRecordByDeviceIds(Long[] deviceIds)
    {
        return screenshotRecordMapper.deleteScreenshotRecordByDeviceIds(deviceIds);
    }

    /**
     * 删除抓图记录信息
     * 
     * @param deviceId 抓图记录主键
     * @return 结果
     */
    @Override
    public int deleteScreenshotRecordByDeviceId(Long deviceId)
    {
        return screenshotRecordMapper.deleteScreenshotRecordByDeviceId(deviceId);
    }
}
