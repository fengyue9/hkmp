package com.ruoyi.device.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.device.config.MinioConfig;
import com.ruoyi.device.domain.VideoRecord;
import com.ruoyi.device.mapper.VideoRecordMapper;
import com.ruoyi.device.service.IVideoRecordService;

/**
 * 回放Service业务层处理
 *
 * @author hongrongjian
 * @date 2024-02-26
 */
@Service
public class VideoRecordServiceImpl implements IVideoRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoRecordServiceImpl.class);

    @Resource
    private VideoRecordMapper videoRecordMapper;
    @Resource
    private MinioConfig minioConfig;

    /**
     * 查询回放
     *
     * @param deviceId 回放主键
     * @return 回放
     */
    @Override
    public VideoRecord selectVideoRecordByDeviceId(Long deviceId) {
        return videoRecordMapper.selectVideoRecordByDeviceId(deviceId);
    }

    /**
     * 查询回放列表
     *
     * @param videoRecord 回放
     * @return 回放
     */
    @Override
    public List<VideoRecord> selectVideoRecordList(VideoRecord videoRecord) {
        return videoRecordMapper.selectVideoRecordList(videoRecord);
    }

    /**
     * 新增回放
     *
     * @param videoRecord 回放
     * @return 结果
     */
    @Override
    public int insertVideoRecord(VideoRecord videoRecord) {
        return videoRecordMapper.insertVideoRecord(videoRecord);
    }

    /**
     * 修改回放
     *
     * @param videoRecord 回放
     * @return 结果
     */
    @Override
    public int updateVideoRecord(VideoRecord videoRecord) {
        return videoRecordMapper.updateVideoRecord(videoRecord);
    }

    /**
     * 批量删除回放
     *
     * @return 结果
     @param recordingKey
     */
    @Override
    public int deleteVideoRecordByRecordingKey(String recordingKey) {
        try {
            minioConfig.removeObject("hkmp", recordingKey);
        } catch (Exception e) {
            LOGGER.error("从minio删除对象失败:" + e.getMessage());
        } return videoRecordMapper.deleteVideoRecordByRecordingKey(recordingKey);
    }

    /**
     *
     * 保存视频
     * @param file
     * @param deviceId
     * @param startTime
     * @param endTime
     * @throws Exception
     */
    @Override
    public void saveVideo(MultipartFile file, String deviceId, Date startTime, Date endTime) throws Exception {
        try {
            minioConfig.putObject(file);
        } catch (Exception e) {
            LOGGER.error("视频保存到minio失败");
            throw e;
        }
        //新增视频记录
        VideoRecord videoRecord = new VideoRecord();
        videoRecord.setDeviceId(Long.valueOf(deviceId));
        videoRecord.setRecordingKey(file.getOriginalFilename());
        videoRecord.setStartTime(startTime);
        videoRecord.setEndTime(endTime);
        int insertNum = videoRecordMapper.insertVideoRecord(videoRecord);
        if (insertNum != 1) {
            throw new ServiceException("新增视频记录失败");
        }
    }
}
