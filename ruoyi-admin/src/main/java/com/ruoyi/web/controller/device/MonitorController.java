package com.ruoyi.web.controller.device;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.nkk.hikvision.utils.HkUtils;
/**
 * 主预览Controller
 *
 * @author hongrongjian
 * @date 2023/12/09
 */
@RestController
@RequestMapping("/camera/monitor")
public class MonitorController {
    /**
     * rtsp 实时预览
     * @param response
     * @param request
     *
     *
     * @author hongrongjian
     * @date 2024/02/18
     */
    @GetMapping(value = "/video/rtspReal.flv", produces = {"video/x-flv;charset=UTF-8"})
    public void flvRtspReal(HttpServletResponse response, HttpServletRequest request) {

        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(0);

        String rtspUrl = HkUtils.toRtspUrl("ip", "推流端口", "账号", "密码", 1);
        try {
            HkUtils.rtspToFlv(rtspUrl, asyncContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * rtsp回放预览
     * @param response
     * @param request
     *
     *
     * @author hongrongjian
     * @date 2024/02/18
     */
    @GetMapping(value = "/video/rtspBack.flv", produces = {"video/x-flv;charset=UTF-8"})
    public void flvRtspBack(HttpServletResponse response, HttpServletRequest request) {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(0);
        // 获取rtsp回放地址
        String rtspUrl = HkUtils.toRtspUrl("ip", "推流端口", "账号", "密码", 1, "2023-03-10 12:00:00",
                "2023-03-10 13:00:00");
        try {
            HkUtils.rtspToFlv(rtspUrl, asyncContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO 1.开始预览接口

    //TODO 2.停止预览接口

    //TODO 3.调整音量接口

    //TODO 4.云台控制接口

    //TODO 5.高级显示设置(调焦、聚焦、调光圈)接口

    //TODO 6.操作记录？

    //TODO 7.抓图接口(默认保存到本地，路径可配置)

}
