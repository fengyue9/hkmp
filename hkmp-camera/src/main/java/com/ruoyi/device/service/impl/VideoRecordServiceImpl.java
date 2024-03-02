package com.ruoyi.device.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
        }
        return videoRecordMapper.deleteVideoRecordByRecordingKey(recordingKey);
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
        String filename = file.getOriginalFilename();

        LOGGER.info("webm转成mp4成功");
        try {
            minioConfig.putObject(file);
        } catch (Exception e) {
            LOGGER.error("视频保存到 MinIO 失败");
            throw e;
        }
        //新增视频记录
        VideoRecord videoRecord = new VideoRecord();
        videoRecord.setDeviceId(Long.valueOf(deviceId));
        videoRecord.setRecordingKey(filename);
        videoRecord.setStartTime(startTime);
        videoRecord.setEndTime(endTime);
        int insertNum = videoRecordMapper.insertVideoRecord(videoRecord);
        if (insertNum != 1) {
            throw new ServiceException("新增视频记录失败");
        }
    }
    /**
     * 将 WebM 格式的视频文件转换为 MP4 格式
     *
     * @param webmFile
     * @return {@link File}
     * @throws Exception
     */
    private File convertToMP4(MultipartFile webmFile) throws Exception {
        // 获取上传的文件名（包括后缀）
        String originalFilename = webmFile.getOriginalFilename();
        // 将文件名的 .webm 后缀改成 .mp4
        String mp4Filename = null;
        if (originalFilename != null) {
            mp4Filename = originalFilename.replace(".webm", ".mp4");
        }
        // 获取项目根目录
        String projectRootDirectory = System.getProperty("user.dir");
        // 构建输入文件和输出文件的路径
        String inputFilepath = projectRootDirectory + File.separator + originalFilename;
        String outputFilepath = projectRootDirectory + File.separator + mp4Filename;
        // 将上传的文件写入到项目根目录下
        File inputFilePath = new File(inputFilepath);
        webmFile.transferTo(inputFilePath);

        String ffmpegPath = "D:\\毕设\\FFmpeg\\ffmpeg-6.1.1-full_build\\bin";
        // 构建 FFmpeg 命令
        String ffmpegCommand = "cmd.exe /c ffmpeg -i " + inputFilepath + " -c:v libx264 -preset slow -crf 22 -c:a aac -b:a 128k " + "-movflags +faststart " + outputFilepath;
        // 执行 FFmpeg 命令
        LOGGER.info(ffmpegCommand);
        Process process = Runtime.getRuntime().exec(ffmpegCommand);
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            LOGGER.error("线程退出码" + exitCode);
            throw new Exception("转换视频文件失败");
        }
        System.out.println("exitCode = " + exitCode);
        if (mp4Filename != null) {
            return new File(mp4Filename);
        }
        return null;
    }
    /**
     * 下载视频
     *
     * @param videoRecordKey
     * @param response
     */
    @Override
    public void downloadVideo(String videoRecordKey, HttpServletResponse response) {
        minioConfig.download(videoRecordKey, response);
    }
}
