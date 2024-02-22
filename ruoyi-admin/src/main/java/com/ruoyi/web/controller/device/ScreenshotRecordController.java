package com.ruoyi.device.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.device.domain.ScreenshotRecord;
import com.ruoyi.device.service.IScreenshotRecordService;

/**
 * 抓图记录Controller
 *
 * @author hongrongjian
 * @date 2024-02-08
 */
@RestController
@RequestMapping("/screenshot/screenshot_record")
public class ScreenshotRecordController extends BaseController {
    @Autowired
    private IScreenshotRecordService screenshotRecordService;

    /**
     * 查询抓图记录列表
     */
    @PreAuthorize("@ss.hasPermi('screenshot:screenshot_record:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScreenshotRecord screenshotRecord) {
        startPage();
        List<ScreenshotRecord> list = screenshotRecordService.selectScreenshotRecordList(screenshotRecord);
        return getDataTable(list);
    }

    /**
     * 导出抓图记录列表
     */
    @PreAuthorize("@ss.hasPermi('screenshot:screenshot_record:export')")
    @Log(title = "抓图记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScreenshotRecord screenshotRecord) {
        List<ScreenshotRecord> list = screenshotRecordService.selectScreenshotRecordList(screenshotRecord);
        ExcelUtil<ScreenshotRecord> util = new ExcelUtil<ScreenshotRecord>(ScreenshotRecord.class);
        util.exportExcel(response, list, "抓图记录数据");
    }

    /**
     * 获取抓图记录详细信息 TODO 应该传screenshotKey
     */
    @PreAuthorize("@ss.hasPermi('screenshot:screenshot_record:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId) {
        return success(screenshotRecordService.selectScreenshotRecordByDeviceId(deviceId));
    }

    /**
     * 删除抓图记录
     */
    @PreAuthorize("@ss.hasPermi('screenshot:screenshot_record:remove')")
    @Log(title = "抓图记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{screenshotKey}")
    public AjaxResult remove(@PathVariable String screenshotKey) {
        return toAjax(screenshotRecordService.deleteScreenshotRecordByScreenshotKey(screenshotKey));
    }

    /**
     * 下载图片
     *
     * @param screenshotKey
     * @param response
     * @return {@link AjaxResult}
     */
    @GetMapping("/download/{screenshotKey}")
    public void download(@PathVariable String screenshotKey, HttpServletResponse response) {
        screenshotRecordService.download(screenshotKey, response);
    }


}
