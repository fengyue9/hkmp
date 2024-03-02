//
//
//package com.ruoyi.device.alarm;
//
//import com.ruoyi.device.CommonMethod.osSelect;
//
//import com.ruoyi.device.NetSDKDemo.FMSGCallBack_V31;
//import com.ruoyi.device.NetSDKDemo.HCNetSDK;
//import com.ruoyi.device.sdk.HCNetSDK;
//import com.sun.jna.Native;
//import com.sun.jna.Pointer;
//
//import java.util.Scanner;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Component;
//@Component
//public class Alarm {
//    @Resource
//    private HCNetSDK hcNetSDK;
//    static int[] lUserID = new int[]{0, 0, 0, 0, 0};//用户句柄 实现对设备登录
//    static int[] lAlarmHandle = new int[]{-1, -1, -1, -1, -1};//报警布防句柄
//    static int[] lAlarmHandle_V50 = new int[]{-1, -1, -1, -1, -1}; //v50报警布防句柄
//    static int lListenHandle = -1;//报警监听句柄
//    static FMSGCallBack_V31 fMSFCallBack_V31 = null;
//
//    /**
//     * @param args
//     */
//    public static void main(String[] args) throws InterruptedException {
//
//        //设置报警回调函数
//        if (fMSFCallBack_V31 == null) {
//            fMSFCallBack_V31 = new FMSGCallBack_V31();
//            Pointer pUser = null;
//            if (!hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fMSFCallBack_V31, pUser)) {
//                System.out.println("设置回调函数失败!");
//                return;
//            } else {
//                System.out.println("设置回调函数成功!");
//            }
//        }
//        /** 设备上传的报警信息是COMM_VCA_ALARM(0x4993)类型，
//         在SDK初始化之后增加调用NET_DVR_SetSDKLocalCfg(enumType为NET_DVR_LOCAL_CFG_TYPE_GENERAL)设置通用参数NET_DVR_LOCAL_GENERAL_CFG的byAlarmJsonPictureSeparate为1，
//         将Json数据和图片数据分离上传，这样设置之后，报警布防回调函数里面接收到的报警信息类型为COMM_ISAPI_ALARM(0x6009)，
//         报警信息结构体为NET_DVR_ALARM_ISAPI_INFO（与设备无关，SDK封装的数据结构），更便于解析。*/
//        HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG struNET_DVR_LOCAL_GENERAL_CFG = new HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG();
//        struNET_DVR_LOCAL_GENERAL_CFG.byAlarmJsonPictureSeparate = 1;   //设置JSON透传报警数据和图片分离
//        struNET_DVR_LOCAL_GENERAL_CFG.write();
//        Pointer pStrNET_DVR_LOCAL_GENERAL_CFG = struNET_DVR_LOCAL_GENERAL_CFG.getPointer();
//        hCNetSDK.NET_DVR_SetSDKLocalCfg(17, pStrNET_DVR_LOCAL_GENERAL_CFG);
//        Alarm.Login_V40(0, "10.17.35.41", (short) 8000, "admin", "abcd1234");
//        Alarm.SetAlarm(0);
//        while (true) {
//            //这里加入控制台输入控制，是为了保持连接状态，当输入Y表示布防结束
//            System.out.print("请选择是否撤出布防(Y/N)：");
//            Scanner input = new Scanner(System.in);
//            String str = input.next();
//            if (str.equals("Y")) {
//                break;
//            }
//        }
//        Alarm.Logout(0);
//    }
//
//    /**
//     * 设备登录V40 与V30功能一致
//     *
//     * @param i    登录设备编号
//     * @param ip   设备IP
//     * @param port SDK端口，默认设备的8000端口
//     * @param user 设备用户名
//     * @param psw  设备密码
//     */
//    public static void Login_V40(int i, String ip, short port, String user, String psw) {
//        //注册
//        HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();//设备登录信息
//        HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息
//
//        String m_sDeviceIP = ip;//设备ip地址
//        m_strLoginInfo.sDeviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
//        System.arraycopy(m_sDeviceIP.getBytes(), 0, m_strLoginInfo.sDeviceAddress, 0, m_sDeviceIP.length());
//
//        String m_sUsername = user;//设备用户名
//        m_strLoginInfo.sUserName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
//        System.arraycopy(m_sUsername.getBytes(), 0, m_strLoginInfo.sUserName, 0, m_sUsername.length());
//
//        String m_sPassword = psw;//设备密码
//        m_strLoginInfo.sPassword = new byte[HCNetSDK.NET_DVR_LOGIN_PASSWD_MAX_LEN];
//        System.arraycopy(m_sPassword.getBytes(), 0, m_strLoginInfo.sPassword, 0, m_sPassword.length());
//
//        m_strLoginInfo.wPort = port;
//        m_strLoginInfo.bUseAsynLogin = false; //是否异步登录：0- 否，1- 是
//        //        m_strLoginInfo.byLoginMode=1;  //ISAPI登录
//        m_strLoginInfo.write();
//
//        lUserID[i] = hCNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);
//        if (lUserID[i] == -1) {
//            System.out.println("登录失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
//            return;
//        } else {
//            System.out.println(ip + ":设备登录成功！");
//            return;
//        }
//    }
//
//    /**
//     * 设备登录V30
//     *
//     * @param i    登录设备编号
//     * @param ip   设备IP
//     * @param port SDK端口，默认设备的8000端口
//     * @param user 设备用户名
//     * @param psw  设备密码
//     */
//    public static void Login_V30(int i, String ip, short port, String user, String psw) {
//        HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
//        lUserID[i] = hCNetSDK.NET_DVR_Login_V30(ip, port, user, psw, m_strDeviceInfo);
//        System.out.println("UsID:" + lUserID[i]);
//        if ((lUserID[i] == -1) || (lUserID[i] == 0xFFFFFFFF)) {
//            System.out.println("登录失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
//            return;
//        } else {
//            System.out.println(ip + ":设备登录成功！");
//            return;
//        }
//    }
//
//    /**
//     * 报警布防接口
//     *
//     * @param i
//     */
//    public static void SetAlarm(int i) {
//        if (lAlarmHandle[i] < 0)//尚未布防,需要布防
//        {
//            //报警布防参数设置
//            HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
//            m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
//            m_strAlarmInfo.byLevel = 0;  //布防等级
//            m_strAlarmInfo.byAlarmInfoType = 1;   // 智能交通报警信息上传类型：0- 老报警信息（NET_DVR_PLATE_RESULT），1- 新报警信息(NET_ITS_PLATE_RESULT)
//            m_strAlarmInfo.byDeployType = 0;   //布防类型：0-客户端布防，1-实时布防
//            m_strAlarmInfo.write();
//            lAlarmHandle[i] = hCNetSDK.NET_DVR_SetupAlarmChan_V41(lUserID[i], m_strAlarmInfo);
//            System.out.println("lAlarmHandle: " + lAlarmHandle[i]);
//            if (lAlarmHandle[i] == -1) {
//                System.out.println("布防失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
//                return;
//            } else {
//                System.out.println("布防成功");
//
//            }
//        } else {
//
//            System.out.println("设备已经布防，请先撤防！");
//        }
//        return;
//
//    }
//
//    /**
//     * 报警布防V50接口，功能和V41一致
//     *
//     * @param i
//     */
//    public static void setAlarm_V50(int i) {
//
//        if (lAlarmHandle_V50[i] < 0)//尚未布防,需要布防
//        {
//            //报警布防参数设置
//            HCNetSDK.NET_DVR_SETUPALARM_PARAM_V50 m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM_V50();
//            m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
//            m_strAlarmInfo.byLevel = 1;  //布防等级
//            m_strAlarmInfo.byAlarmInfoType = 1;   // 智能交通报警信息上传类型：0- 老报警信息（NET_DVR_PLATE_RESULT），1- 新报警信息(NET_ITS_PLATE_RESULT)
//            m_strAlarmInfo.byDeployType = 1;   //布防类型 0：客户端布防 1：实时布防
//            m_strAlarmInfo.write();
//            lAlarmHandle[i] = hCNetSDK.NET_DVR_SetupAlarmChan_V50(lUserID[i], m_strAlarmInfo, Pointer.NULL, 0);
//            System.out.println("lAlarmHandle: " + lAlarmHandle[i]);
//            if (lAlarmHandle[i] == -1) {
//                System.out.println("布防失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
//                return;
//            } else {
//                System.out.println("布防成功");
//
//            }
//
//        } else {
//
//            System.out.println("设备已经布防，请先撤防！");
//        }
//        return;
//
//    }
//
//    /**
//     * 开启监听
//     * @param ip  监听IP
//     * @param port 监听端口
//     */
//    public static void StartListen(String ip, short port) {
//        if (fMSFCallBack_V31 == null) {
//            fMSFCallBack_V31 = new FMSGCallBack_V31();
//        }
//        lListenHandle = hCNetSDK.NET_DVR_StartListen_V30("10.17.35.111", (short) 8200, fMSFCallBack_V31, null);
//        if (lListenHandle == -1) {
//            System.out.println("监听失败" + hCNetSDK.NET_DVR_GetLastError());
//            return;
//        } else {
//            System.out.println("监听成功");
//        }
//    }
//
//    /**
//     * 设备注销
//     * @param i
//     */
//    public static void Logout(int i) {
//
//        if (lAlarmHandle[i] > -1) {
//            if (!hCNetSDK.NET_DVR_CloseAlarmChan(lAlarmHandle[i])) {
//                System.out.println("撤防成功");
//            }
//        }
//        if (lListenHandle > -1) {
//            if (!hCNetSDK.NET_DVR_StopListen_V30(lListenHandle)) {
//                System.out.println("停止监听成功");
//            }
//        }
//        if (hCNetSDK.NET_DVR_Logout(lUserID[i])) {
//            System.out.println("注销成功");
//        }
//        hCNetSDK.NET_DVR_Cleanup();
//        return;
//    }
//
//    /**
//     * 动态库加载
//     *
//     * @return
//     */
//    private static boolean CreateSDKInstance() {
//        if (hCNetSDK == null) {
//            synchronized (HCNetSDK.class) {
//                String strDllPath = "";
//                try {
//                    if (osSelect.isWindows())
//                        //win系统加载库路径
//                        strDllPath = System.getProperty("user.dir") + "\\lib\\HCNetSDK.dll";
//                    else if (osSelect.isLinux())
//                        //Linux系统加载库路径
//                        strDllPath = System.getProperty("user.dir") + "/lib/libhcnetsdk.so";
//                    hCNetSDK = (HCNetSDK) Native.loadLibrary(strDllPath, HCNetSDK.class);
//                } catch (Exception ex) {
//                    System.out.println("loadLibrary: " + strDllPath + " Error: " + ex.getMessage());
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//}
