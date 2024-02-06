package com.ruoyi.device.mapper;

import java.util.List;
import com.ruoyi.device.domain.Device;

/**
 * 设备信息管理Mapper接口
 * 
 * @author hongrongjian
 * @date 2023-10-23
 */
public interface DeviceMapper 
{
    /**
     * 查询设备信息管理
     * 
     * @param deviceId 设备信息管理主键
     * @return 设备信息管理
     */
    public Device selectDeviceByDeviceId(Long deviceId);

    /**
     * 查询设备信息管理列表
     * 
     * @param device 设备信息管理
     * @return 设备信息管理集合
     */
    public List<Device> selectDeviceList(Device device);

    /**
     * 新增设备信息管理
     * 
     * @param device 设备信息管理
     * @return 结果
     */
    public int insertDevice(Device device);

    /**
     * 修改设备信息管理
     * 
     * @param device 设备信息管理
     * @return 结果
     */
    public int updateDevice(Device device);

    /**
     * 删除设备信息管理
     * 
     * @param deviceId 设备信息管理主键
     * @return 结果
     */
    public int deleteDeviceByDeviceId(Long deviceId);

    /**
     * 批量删除设备信息管理
     * 
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceByDeviceIds(Long[] deviceIds);
}