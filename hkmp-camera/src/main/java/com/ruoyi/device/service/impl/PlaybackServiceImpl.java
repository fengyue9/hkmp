package com.ruoyi.device.service.impl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.device.sdk.HCNetSDK;
import com.ruoyi.device.service.IPlaybackService;
/**
 * 回放服务实现类
 *
 * @author Fy
 * @date 2024/02/26
 */
@Service
public class PlaybackServiceImpl implements IPlaybackService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaybackServiceImpl.class);

    @Resource
    private HCNetSDK hcNetSDK;

    /**
     * 回放
     *
     *
     @param url
     */
    @Override
    public void playback(String url) throws IOException, InterruptedException {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("rtsp流地址不能为空");
        }
        // 构造 cmd 命令
        String command = "ffmpeg -i \"" + url + "\" output_" + UUID.randomUUID() + ".mp4";
        System.out.println("执行cmd命令：" + command);
        // 执行 FFmpeg 命令
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            LOGGER.error("cmd执行FFmepg命令失败");
            throw new RuntimeException(e);
        }
        // 获取 FFmpeg 命令的输出流和错误流
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line); // 打印 FFmpeg 输出信息
        }
        reader.close();
        // 等待 FFmpeg 命令执行完成
        int exitVal = process.waitFor();
        process.destroy();
        System.out.println("FFmpeg process exited with status " + exitVal);

    }

}
