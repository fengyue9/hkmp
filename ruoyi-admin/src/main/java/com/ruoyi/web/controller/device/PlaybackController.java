package com.ruoyi.web.controller.device;
import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.device.service.IPlaybackService;
import com.ruoyi.web.vo.PlaybackVO;
/**
 * 回放Controller
 *
 * @author hongrongjian
 * @date 2023/12/09
 */
@RestController
@RequestMapping("/camera/playback")
public class PlaybackController {

    @Resource
    private IPlaybackService playbackService;

    /**
     * 回放
     *
     * @param vo
     */
    @PostMapping("/playback")
    public void playback(@RequestBody PlaybackVO vo) throws IOException, InterruptedException {
        playbackService.playback(vo.getUrl());
    }
}
