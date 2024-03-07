package com.ruoyi.device.config;
import java.io.File;
import java.io.FileOutputStream;
import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.device.CommonMethod.osSelect;
import com.ruoyi.device.domain.Device;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.sdk.HCNetSDK;
import com.ruoyi.device.sdk.PlayCtrl;
import com.ruoyi.device.service.IAlarmRecordService;
import com.ruoyi.device.utils.LoginUtils;
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
    static FMSGCallBack_V31 fMSFCallBack_V31 = null;

    public static int lListenHandle = -1;//报警监听句柄
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

    @Resource
    private RedisCache redisCache;
    @Resource
    private IAlarmRecordService alarmService;
    @Resource
    private DeviceMapper deviceMapper;

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

    @Bean
    public HCNetSDK hCNetSDK() throws UnknownHostException {
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
        //设置报警异常信息回调
        if (fMSFCallBack_V31 == null) {
            fMSFCallBack_V31 = new FMSGCallBack_V31();
            if (!hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fMSFCallBack_V31, null)) {
                System.out.println("设置回调函数失败!");
            } else {
                System.out.println("设置回调函数成功!");
            }
        }
        /*
        * 设备上传的报警信息是COMM_VCA_ALARM(0x4993)类型，
          在SDK初始化之后增加调用NET_DVR_SetSDKLocalCfg(enumType为NET_DVR_LOCAL_CFG_TYPE_GENERAL)设置通用参数NET_DVR_LOCAL_GENERAL_CFG的byAlarmJsonPictureSeparate为1，
          将Json数据和图片数据分离上传，这样设置之后，报警布防回调函数里面接收到的报警信息类型为COMM_ISAPI_ALARM(0x6009)，
          报警信息结构体为NET_DVR_ALARM_ISAPI_INFO（与设备无关，SDK封装的数据结构），更便于解析。
        * */
        HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG struNET_DVR_LOCAL_GENERAL_CFG = new HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG();
        struNET_DVR_LOCAL_GENERAL_CFG.byAlarmJsonPictureSeparate = 1;   //设置JSON透传报警数据和图片分离
        struNET_DVR_LOCAL_GENERAL_CFG.write();
        Pointer pStrNET_DVR_LOCAL_GENERAL_CFG = struNET_DVR_LOCAL_GENERAL_CFG.getPointer();
        hCNetSDK.NET_DVR_SetSDKLocalCfg(17, pStrNET_DVR_LOCAL_GENERAL_CFG);
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
        List<Device> deviceList = deviceMapper.selectDeviceList(new Device());
        //退出登录
        for (Device device : deviceList) {
            LoginUtils.logout(Device.userMap.get(device.getDeviceId()));
        }
        //撤防
        alarmService.closeAlarmChan();
        if (hCNetSDK != null) {
            //SDK反初始化，释放资源，只需要退出时调用一次
            hCNetSDK.NET_DVR_Cleanup();
            System.out.println("SDK cleanup complete");
        }

    }

}
