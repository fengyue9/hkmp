package com.ruoyi.web.controller.device;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.device.domain.AlarmRecord;
import com.ruoyi.device.service.IAlarmRecordService;

/**
 * 报警记录Controller
 *
 * @author hongrongjian
 * @date 2024-03-02
 */
@RestController
@RequestMapping("/device/record")
public class AlarmRecordController extends BaseController {
    @Autowired
    private IAlarmRecordService alarmRecordService;

    /**
     * 查询报警记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AlarmRecord alarmRecord) {
        startPage();
        List<AlarmRecord> list = alarmRecordService.selectAlarmRecordList(alarmRecord);
        return getDataTable(list);
    }

    /**
     * 导出报警记录列表
     */
    @Log(title = "报警记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlarmRecord alarmRecord) {
        List<AlarmRecord> list = alarmRecordService.selectAlarmRecordList(alarmRecord);
        ExcelUtil<AlarmRecord> util = new ExcelUtil<AlarmRecord>(AlarmRecord.class);
        util.exportExcel(response, list, "报警记录数据");
    }

    /**
     * 获取报警记录详细信息
     */
    @GetMapping(value = "/{alarmRecordId}")
    public AjaxResult getInfo(@PathVariable("deviceId") String alarmRecordId) {
        return success(alarmRecordService.selectAlarmRecordById(alarmRecordId));
    }

    /**
     * 新增报警记录
     */
    @Log(title = "报警记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlarmRecord alarmRecord) {
        return toAjax(alarmRecordService.insertAlarmRecord(alarmRecord));
    }

    /**
     * 修改报警记录
     */
    @Log(title = "报警记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlarmRecord alarmRecord) {
        return toAjax(alarmRecordService.updateAlarmRecord(alarmRecord));
    }

    /**
     * 删除报警记录
     */
    @Log(title = "报警记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds) {
        return toAjax(alarmRecordService.deleteAlarmRecordByDeviceIds(deviceIds));
    }

    /**
     * 下载报警详情
     *
     *
     @param alarmRecordId
     @param response
     */
    @GetMapping(value = "/download/{alarmRecordId}")
    public void downloadVideo(@PathVariable String alarmRecordId, HttpServletResponse response) {
        alarmRecordService.downloadVideo(alarmRecordId, response);
    }

    /**
     * 处理报警
     *
     *
     @param alarmRecord
     */
    @PostMapping(value = "/handle")
    public void handleAlarm(@RequestBody AlarmRecord alarmRecord) {
        String alarmDesc = alarmRecord.getAlarmDesc();
        if (alarmDesc == null || alarmDesc.isEmpty()) {
            throw new IllegalStateException("处理描述不能为空");
        }
        alarmRecordService.handleAlarm(alarmRecord);
    }
}