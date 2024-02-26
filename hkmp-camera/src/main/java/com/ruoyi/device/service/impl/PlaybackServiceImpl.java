package com.ruoyi.device.service.impl;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ruoyi.device.domain.Device;
import com.ruoyi.device.sdk.HCNetSDK;
import com.ruoyi.device.service.IPlaybackService;
@Service
public class PlaybackServiceImpl implements IPlaybackService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaybackServiceImpl.class);

    @Resource
    private HCNetSDK hcNetSDK;
    /**
     * 按照时间回放   参数时间格式:2022-01-01 00:00:00
     *
     * @param device
     * @param startTime
     * @param endTime
     */
    @Override
    public void playback(Device device, String startTime, String endTime) {
        HCNetSDK.NET_DVR_TIME startTimeStruct = new HCNetSDK.NET_DVR_TIME();
        HCNetSDK.NET_DVR_TIME endTimeStruct = new HCNetSDK.NET_DVR_TIME();
        String[] startTimes = startTime.split(" ");
        String[] endTimes = endTime.split(" ");
        String[] startDateInTimes = startTimes[0].split("-");
        String[] startTimeInTimes = startTimes[1].split(":");
        String[] endDateInTimes = endTimes[0].split("-");
        String[] endTimeInTimes = endTimes[1].split(":");

        startTimeStruct.dwYear = Integer.parseInt(startDateInTimes[0]);
        startTimeStruct.dwMonth = Integer.parseInt(startDateInTimes[1]);
        startTimeStruct.dwDay = Integer.parseInt(startDateInTimes[2]);


    }

}
