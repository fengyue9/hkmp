package com.ruoyi.web.controller.device;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
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
import com.ruoyi.device.domain.Device;
import com.ruoyi.device.service.IDeviceService;

/**
 * 设备信息管理Controller
 *
 * @author hongrongjian
 * @date 2023-10-23
 */
@RestController
@RequestMapping("/camera/device")
public class DeviceController extends BaseController {
    @Resource
    private IDeviceService deviceService;

    /**
     * 查询设备信息管理列表
     */
    @PreAuthorize("@ss.hasPermi('camera:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(Device device) {
        startPage();
        List<Device> list = deviceService.selectDeviceList(device);
        return getDataTable(list);
    }

    /**
     * 导出设备信息管理列表
     */
    @PreAuthorize("@ss.hasPermi('camera:device:export')")
    @Log(title = "设备信息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Device device) {
        List<Device> list = deviceService.selectDeviceList(device);
        ExcelUtil<Device> util = new ExcelUtil<Device>(Device.class);
        util.exportExcel(response, list, "设备信息管理数据");
    }

    /**
     * 获取设备信息管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('camera:device:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId) {
        return success(deviceService.selectDeviceByDeviceId(deviceId));
    }

    /**
     * 新增设备信息管理
     */
    @PreAuthorize("@ss.hasPermi('camera:device:add')")
    @Log(title = "设备信息管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Device device) {
        return toAjax(deviceService.insertDevice(device));
    }

    /**
     * 修改设备信息管理
     */
    @PreAuthorize("@ss.hasPermi('camera:device:edit')")
    @Log(title = "设备信息管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Device device) {
        return toAjax(deviceService.updateDevice(device));
    }

    /**
     * 删除设备信息管理
     */
    @PreAuthorize("@ss.hasPermi('camera:device:remove')")
    @Log(title = "设备信息管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds) {
        return toAjax(deviceService.deleteDeviceByDeviceIds(deviceIds));
    }

    @PostMapping("/setUpAlarm")
    public AjaxResult setUpAlarm(@RequestBody Device device) {
        deviceService.setUpAlarm(device);
        return AjaxResult.success();
    }
    @PostMapping("/closeAlarm")
    public AjaxResult closeAlarm(@RequestBody Device device) {
        deviceService.setUpAlarm(device);
        return AjaxResult.success();
    }
}
