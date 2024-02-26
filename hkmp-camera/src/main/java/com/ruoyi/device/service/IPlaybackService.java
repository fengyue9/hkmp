package com.ruoyi.device.service;
import com.ruoyi.device.domain.Device;
public interface IPlaybackService {

    void playback(Device device, String startTime, String endTime);
}
