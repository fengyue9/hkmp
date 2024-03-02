package com.ruoyi.web.controller.device;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.device.service.IAlarmRecordService;
/**
 * 报警Controller
 *
 * @author hongrongjian
 * @date 2023/12/09
 */
@RestController
@RequestMapping("/camera/alarm")
public class AlarmController {

    @Resource
    private IAlarmRecordService alarmService;
    //TODO 1.处理报警
    @GetMapping("/test")
    @Anonymous
    public void test() {
        alarmService.test();
    }

    @GetMapping("/close")
    @Anonymous
    public void close() {
        alarmService.closeAlarmChan();
    }
    //TODO 2.升级报警

    //TODO 3.获取未处理报警记录列表

    //TODO 4.获取历史报警记录列表

}
