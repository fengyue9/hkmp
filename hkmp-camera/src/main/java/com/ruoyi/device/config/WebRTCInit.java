package com.ruoyi.device.config;
import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
@Configuration
public class WebRTCInit {

    /**
     * 项目启动时初始化webrtc-streamer
     *
     *
     * @throws Exception
     * @author hongrongjian
     * @date 2024/02/20
     */
    @PostConstruct
    public void init() {
        startWebRtcStreamer();
    }

    /**
     * 启动
     *
     *
     *
     * @author hongrongjian
     * @date 2024/02/20
     */
    public void startWebRtcStreamer() {
//        String command = "webrtc-streamer.exe";
//        // 指定命令执行的工作目录
//        String workingDirectory = "D:\\毕设\\源码\\hkmp\\webrtc-streamer";
//        try {
//            // 创建ProcessBuilder对象，并设置命令和工作目录
//            ProcessBuilder pb = new ProcessBuilder(command);
//            pb.directory(new File(workingDirectory));
//            // 设置继承父进程的环境
//            pb.inheritIO();
//            // 启动进程
//            Process process = pb.start();
//            // 等待进程执行完成
//            process.waitFor();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
