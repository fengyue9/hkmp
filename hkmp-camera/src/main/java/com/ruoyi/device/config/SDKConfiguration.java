package com.ruoyi.device.config;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruoyi.device.CommonMethod.osSelect;
import com.ruoyi.device.sdk.HCNetSDK;
import com.ruoyi.device.sdk.PlayCtrl;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
/**
 * 海康SDK配置类，用于项目启动初始化以及项目关闭资源释放
 *
 * @author hongrongjian
 * @date 2023/12/10
 */
@Configuration
public class SDKConfiguration {
    public static HCNetSDK hCNetSDK;
    public static PlayCtrl playControl;
    public static FExceptionCallBack_Imp fExceptionCallBack;
    public static int FlowHandle;
    public static int lUserID = -1;//用户句柄
    public static int lPlay = -1;  //预览句柄
    public static int iPlayBack; //回放句柄
    public static int lDChannel;  //预览通道号
    public static boolean bSaveHandle = false;
    public static File file;
    public static String resultFileName = "..\\Download" + new String("returnFile" + ".h264");
    public static FileOutputStream fileOutputStream = null;
    public static int fileLength = 0;
    public int iErr = 0;
    public Timer Playbacktimer;//回放用定时器
    int m_lLoadHandle;
    /**
     *
     动态库加载
     *
     * @return boolean
     *
     * @author hongrongjian
     * @date 2023/12/10
     */
    private static boolean createSDKInstance() {
        if (hCNetSDK == null) {
            synchronized (HCNetSDK.class) {
                String strDllPath = "";
                try {
                    if (osSelect.isWindows())
                        //win系统加载库路径
                        strDllPath = System.getProperty("user.dir") + "\\lib\\HCNetSDK.dll";

                    else if (osSelect.isLinux())
                        //Linux系统加载库路径
                        strDllPath = System.getProperty("user.dir") + "/lib/libhcnetsdk.so";
                    hCNetSDK = (HCNetSDK) Native.loadLibrary(strDllPath, HCNetSDK.class);
                } catch (Exception ex) {
                    System.out.println("加载库失败：loadLibrary: " + strDllPath + " Error: " + ex.getMessage());
                    return false;
                }
            }
        }
        return true;
    }
    /**
     *
     播放库加载
     *
     * @return boolean
     *
     * @author hongrongjian
     * @date 2023/12/10
     */
    private static boolean createPlayInstance() {
        if (playControl == null) {
            synchronized (PlayCtrl.class) {
                String strPlayPath = "";
                try {
                    if (osSelect.isWindows())
                    //win系统加载库路径
                    {
                        strPlayPath = System.getProperty("user.dir") + "\\lib\\PlayCtrl.dll";
                    } else if (osSelect.isLinux())
                    //Linux系统加载库路径
                    {
                        strPlayPath = System.getProperty("user.dir") + "/lib/libPlayCtrl.so";
                    }
                    playControl = (PlayCtrl) Native.loadLibrary(strPlayPath, PlayCtrl.class);

                } catch (Exception ex) {
                    System.out.println("加载库失败：loadLibrary: " + strPlayPath + " Error: " + ex.getMessage());
                    return false;
                }
            }
        }
        return true;
    }
    /**
     *
     *
     * 当在Linux系统下运行时加载SDK组件
     *
     * @author hongrongjian
     * @date 2023/12/09
     */
    private static void loadSDKInLinux() {
        HCNetSDK.BYTE_ARRAY ptrByteArray1 = new HCNetSDK.BYTE_ARRAY(256);
        HCNetSDK.BYTE_ARRAY ptrByteArray2 = new HCNetSDK.BYTE_ARRAY(256);
        //这里是库的绝对路径，请根据实际情况修改，注意改路径必须有访问权限
        String strPath1 = System.getProperty("user.dir") + "/lib/libcrypto.so.1.1";
        String strPath2 = System.getProperty("user.dir") + "/lib/libssl.so.1.1";
        System.arraycopy(strPath1.getBytes(), 0, ptrByteArray1.byValue, 0, strPath1.length());
        ptrByteArray1.write();
        hCNetSDK.NET_DVR_SetSDKInitCfg(3, ptrByteArray1.getPointer());
        System.arraycopy(strPath2.getBytes(), 0, ptrByteArray2.byValue, 0, strPath2.length());
        ptrByteArray2.write();
        hCNetSDK.NET_DVR_SetSDKInitCfg(4, ptrByteArray2.getPointer());
        String strPathCom = System.getProperty("user.dir") + "/lib/";
        HCNetSDK.NET_DVR_LOCAL_SDK_PATH struComPath = new HCNetSDK.NET_DVR_LOCAL_SDK_PATH();
        System.arraycopy(strPathCom.getBytes(), 0, struComPath.sPath, 0, strPathCom.length());
        struComPath.write();
        hCNetSDK.NET_DVR_SetSDKInitCfg(2, struComPath.getPointer());
    }
    /**
     * 设备登录V40 与V30功能一致
     * @param ip   设备IP
     * @param port SDK端口，默认设备的8000端口
     * @param user 设备用户名
     * @param psw  设备密码
     */
    public static void login_V40(String ip, short port, String user, String psw) {
        //注册
        HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();//设备登录信息
        HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息
        String m_sDeviceIP = ip;//设备ip地址
        m_strLoginInfo.sDeviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
        System.arraycopy(m_sDeviceIP.getBytes(), 0, m_strLoginInfo.sDeviceAddress, 0, m_sDeviceIP.length());
        String m_sUsername = user;//设备用户名
        m_strLoginInfo.sUserName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
        System.arraycopy(m_sUsername.getBytes(), 0, m_strLoginInfo.sUserName, 0, m_sUsername.length());
        String m_sPassword = psw;//设备密码
        m_strLoginInfo.sPassword = new byte[HCNetSDK.NET_DVR_LOGIN_PASSWD_MAX_LEN];
        System.arraycopy(m_sPassword.getBytes(), 0, m_strLoginInfo.sPassword, 0, m_sPassword.length());
        m_strLoginInfo.wPort = port;
        m_strLoginInfo.bUseAsynLogin = false; //是否异步登录：0- 否，1- 是
        m_strLoginInfo.byLoginMode = 0;  //0- SDK私有协议，1- ISAPI协议
        m_strLoginInfo.write();
        lUserID = hCNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);
        if (lUserID == -1) {
            System.out.println("登录失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println(ip + ":设备登录成功！");
            //相机一般只有一个通道号，热成像相机有2个通道号，通道号为1或1,2
            //byStartDChan为IP通道起始通道号, 预览回放NVR的IP通道时需要根据起始通道号进行取值
            if ((int) m_strDeviceInfo.struDeviceV30.byStartDChan == 1 && (int) m_strDeviceInfo.struDeviceV30.byStartDChan == 33) {
                //byStartDChan为IP通道起始通道号, 预览回放NVR的IP通道时需要根据起始通道号进行取值,NVR起始通道号一般是33或者1开始
                lDChannel = m_strDeviceInfo.struDeviceV30.byStartDChan;
                System.out.println("预览起始通道号：" + lDChannel);
            }
        }
    }
    @Bean
    public HCNetSDK hCNetSDK() {
        //1.加载sdk和播放库
        if (hCNetSDK == null && playControl == null) {
            if (!createSDKInstance()) {
                System.out.println("Load SDK fail");
                return null;
            }
            if (!createPlayInstance()) {
                System.out.println("Load PlayCtrl fail");
                return null;
            }
        }
        if (osSelect.isLinux()) {
            //Linux下的加载方法
            loadSDKInLinux();
        }
        //2.SDK初始化，一个程序只需要调用一次
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        System.out.println("SDK初始化完成");
        //3.设置异常消息回调
        if (fExceptionCallBack == null) {
            fExceptionCallBack = new FExceptionCallBack_Imp();
        }
        Pointer pUser = null;
        if (!hCNetSDK.NET_DVR_SetExceptionCallBack_V30(0, 0, fExceptionCallBack, pUser)) {
            return null;
        }
        System.out.println("设置异常消息回调成功");
        hCNetSDK.NET_DVR_SetLogToFile(3, "./sdkLog", false);
        return hCNetSDK;

    }
    /**
     *
     * SDK资源释放，程序退出时调用
     *
     *
     * @author hongrongjian
     * @date 2023/12/10
     */
    @PreDestroy
    public void cleanup() {
        if (hCNetSDK != null) {
            //SDK反初始化，释放资源，只需要退出时调用一次
            hCNetSDK.NET_DVR_Cleanup();
            System.out.println("SDK cleanup complete");
        }
    }
    public static class FExceptionCallBack_Imp implements HCNetSDK.FExceptionCallBack {
        public void invoke(int dwType, int lUserID, int lHandle, Pointer pUser) {
            System.out.println("异常事件类型:" + dwType);
        }
    }

}
