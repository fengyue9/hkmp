package com.ruoyi.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.device.domain.VideoRecord;

/**
 * 回放Mapper接口
 *
 * @author hongrongjian
 * @date 2024-02-26
 */
public interface VideoRecordMapper {
    /**
     * 查询回放
     *
     * @param deviceId 回放主键
     * @return 回放
     */
    public VideoRecord selectVideoRecordByDeviceId(Long deviceId);

    /**
     * 查询回放列表
     *
     * @param videoRecord 回放
     * @return 回放集合
     */
    public List<VideoRecord> selectVideoRecordList(VideoRecord videoRecord);

    /**
     * 新增回放
     *
     * @param videoRecord 回放
     * @return 结果
     */
    public int insertVideoRecord(VideoRecord videoRecord);

    /**
     * 修改回放
     *
     * @param videoRecord 回放
     * @return 结果
     */
    public int updateVideoRecord(VideoRecord videoRecord);

    /**
     * 批量删除回放
     *
     * @return 结果


     @param recordingKey
     */
    public int deleteVideoRecordByRecordingKey(@Param("recordingKey") String recordingKey);
}
