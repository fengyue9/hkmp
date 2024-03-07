package com.ruoyi.device.utils;
import com.ruoyi.device.domain.Device;
import com.ruoyi.device.sdk.HCNetSDK;

import static com.ruoyi.device.config.SDKConfiguration.*;
/**
 * 调用sdk登录工具类
 *
 * @author Fy
 * @date 2024/02/21
 */

public class LoginUtils {

    /**
     * 登录方法，返回用户句柄
     *
     * @param device 用户句柄
     * @return int
     */
    public static int login(Device device) {
        HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();//设备登录信息
        HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息
        String m_sDeviceIP = device.getDeviceIp();//设备ip地址
        m_strLoginInfo.sDeviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
        System.arraycopy(m_sDeviceIP.getBytes(), 0, m_strLoginInfo.sDeviceAddress, 0, m_sDeviceIP.length());
        String m_sUsername = device.getDeviceUsername();//设备用户名
        m_strLoginInfo.sUserName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
        System.arraycopy(m_sUsername.getBytes(), 0, m_strLoginInfo.sUserName, 0, m_sUsername.length());
        String m_sPassword = device.getDevicePassword();//设备密码
        m_strLoginInfo.sPassword = new byte[HCNetSDK.NET_DVR_LOGIN_PASSWD_MAX_LEN];
        System.arraycopy(m_sPassword.getBytes(), 0, m_strLoginInfo.sPassword, 0, m_sPassword.length());
        m_strLoginInfo.wPort = Short.parseShort(device.getDevicePort());//设备端口
        m_strLoginInfo.bUseAsynLogin = false; //是否异步登录：0- 否，1- 是
        m_strLoginInfo.byLoginMode = 0;  //0- SDK私有协议，1- ISAPI协议
        m_strLoginInfo.write();
        //返回用户句柄
        int userId = hCNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);
        //        HCNetSDK.NET_DVR_DEVICEINFO_V30 struDeviceV30 = m_strDeviceInfo.struDeviceV30;
        //        System.out.println("模拟通道的起始通道号 struDeviceV30.byStartDChan = " + struDeviceV30.byStartDChan);
        //        System.out.println("设备模拟通道个数 struDeviceV30.byChanNum = " + struDeviceV30.byChanNum);
        //        System.out.println("数据通道起始通道号struDeviceV30.byStartDChan = " + struDeviceV30.byStartDChan);
        //        System.out.println("设备类型 struDeviceV30.byDVRType = " + struDeviceV30.byDVRType);
        //        System.out.println("设备类型 struDeviceV30.wDevType = " + struDeviceV30.wDevType);
        //        System.out.println("struDeviceV30.byIPChanNum = " + struDeviceV30.byIPChanNum);
        return userId;
    }
    /**
     * 登出，入参为用户句柄
     *
     * @param userId
     */
    public static void logout(int userId) {
        if (!hCNetSDK.NET_DVR_Logout_V30(userId)) {
            throw new IllegalStateException("登出失败,用户句柄为:" + userId);
        }
    }

    /**
     * 查询某个设备的在线用户id
     *
     * @param deviceId
     * @return int
     */
    public static int getUserIdFromOnlineUserMap(Long deviceId) {
        if (Device.userMap == null || Device.userMap.isEmpty()) {
            return -1;
        } else {
            return Device.userMap.get(deviceId);
        }
    }

}
