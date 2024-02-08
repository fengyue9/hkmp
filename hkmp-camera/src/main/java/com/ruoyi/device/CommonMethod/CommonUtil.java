package com.ruoyi.device.CommonMethod;

/**
 * 通用工具类
 * @author hongrongjian
 * @create 2022-03-22-11:13
 */
public class CommonUtil {

    //SDK时间解析
    public static String parseTime(int time) {
        int dwYear = (time >> 26) + 2000;
        int dwMonth = (time >> 22) & 15;
        int dwDay = (time >> 17) & 31;
        int dwHour = (time >> 12) & 31;
        int dwMinute = (time >> 6) & 63;
        int dwSecond = (time >> 0) & 63;

        String sTime = String.format("%04d", dwYear) + String.format("%02d", dwMonth) + String.format("%02d",
                dwDay) + String.format("%02d", dwHour) + String.format("%02d", dwMinute) + String.format("%02d",
                dwSecond);
        //        System.out.println(sTime);
        return sTime;
    }

}
