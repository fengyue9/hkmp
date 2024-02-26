package com.ruoyi.web.controller.device;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.device.domain.VideoRecord;
import com.ruoyi.device.service.IVideoRecordService;

/**
 * 回放Controller
 *
 * @author hongrongjian
 * @date 2024-02-26
 */
@RestController
@RequestMapping("/system/record")
public class VideoRecordController extends BaseController {
    @Autowired
    private IVideoRecordService videoRecordService;

    /**
     * 查询回放列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(VideoRecord videoRecord) {
        startPage();
        List<VideoRecord> list = videoRecordService.selectVideoRecordList(videoRecord);
        return getDataTable(list);
    }

    /**
     * 导出回放列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "回放", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VideoRecord videoRecord) {
        List<VideoRecord> list = videoRecordService.selectVideoRecordList(videoRecord);
        ExcelUtil<VideoRecord> util = new ExcelUtil<VideoRecord>(VideoRecord.class);
        util.exportExcel(response, list, "回放数据");
    }

    /**
     * 获取回放详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId) {
        return success(videoRecordService.selectVideoRecordByDeviceId(deviceId));
    }

    /**
     * 新增回放
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "回放", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VideoRecord videoRecord) {
        return toAjax(videoRecordService.insertVideoRecord(videoRecord));
    }

    /**
     * 修改回放
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "回放", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VideoRecord videoRecord) {
        return toAjax(videoRecordService.updateVideoRecord(videoRecord));
    }

    /**
     * 删除回放
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "回放", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordingKey}")
    public AjaxResult remove(@PathVariable String recordingKey) {
        return toAjax(videoRecordService.deleteVideoRecordByRecordingKey(recordingKey));
    }

    /**
     * 保存视频
     *
     * @param file
     * @param deviceId
     * @param startTime
     * @param endTime
     * @return {@link AjaxResult}
     */
    @PostMapping(value = "/saveVideo")
    public AjaxResult saveVideo(@RequestParam("file") MultipartFile file, @RequestParam("deviceId") String deviceId,
            @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime) {
        try {
            videoRecordService.saveVideo(file, deviceId, startTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("保存视频失败");
        }
        return AjaxResult.success();
    }
}
