package com.ruoyi.device.service;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.device.domain.VideoRecord;

/**
 * 回放Service接口
 *
 * @author hongrongjian
 * @date 2024-02-26
 */
public interface IVideoRecordService {
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
    public int deleteVideoRecordByRecordingKey(String recordingKey);

    void saveVideo(MultipartFile file, String deviceId, Date startTime, Date endTime) throws Exception;
}
