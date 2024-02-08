package com.ruoyi.device.mapper;

import java.util.List;
import com.ruoyi.device.domain.ScreenshotRecord;

/**
 * 抓图记录Mapper接口
 * 
 * @author hongrongjian
 * @date 2024-02-08
 */
public interface ScreenshotRecordMapper 
{
    /**
     * 查询抓图记录
     * 
     * @param deviceId 抓图记录主键
     * @return 抓图记录
     */
    public ScreenshotRecord selectScreenshotRecordByDeviceId(Long deviceId);

    /**
     * 查询抓图记录列表
     * 
     * @param screenshotRecord 抓图记录
     * @return 抓图记录集合
     */
    public List<ScreenshotRecord> selectScreenshotRecordList(ScreenshotRecord screenshotRecord);

    /**
     * 新增抓图记录
     * 
     * @param screenshotRecord 抓图记录
     * @return 结果
     */
    public int insertScreenshotRecord(ScreenshotRecord screenshotRecord);

    /**
     * 修改抓图记录
     * 
     * @param screenshotRecord 抓图记录
     * @return 结果
     */
    public int updateScreenshotRecord(ScreenshotRecord screenshotRecord);

    /**
     * 删除抓图记录
     * 
     * @param deviceId 抓图记录主键
     * @return 结果
     */
    public int deleteScreenshotRecordByDeviceId(Long deviceId);

    /**
     * 批量删除抓图记录
     * 
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScreenshotRecordByDeviceIds(Long[] deviceIds);
}
