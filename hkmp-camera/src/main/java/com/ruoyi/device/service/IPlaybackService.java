package com.ruoyi.device.service;
import java.io.IOException;
public interface IPlaybackService {

    void playback(String url) throws IOException, InterruptedException;
}
