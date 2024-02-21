package com.ruoyi.device.utils;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ruoyi.device.sdk.HCNetSDK;

/**
 * 云台控制代码处理类
 * 使用策略模式在不同的情况下分配不同的方法
 * @author Fy
 * @date 2024/02/21
 */
@Component
public class CodeHandler {
    private static Map<Integer, Consumer<Integer>> codeMap;
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeHandler.class);

    public CodeHandler() {
        codeMap = new HashMap<>();
        codeMap.put(1, this::handleCode1);
        codeMap.put(2, this::handleCode2);
        codeMap.put(3, this::handleCode3);
        codeMap.put(4, this::handleCode4);
        codeMap.put(5, this::handleCode5);
        codeMap.put(6, this::handleCode6);
        codeMap.put(7, this::handleCode7);
        codeMap.put(8, this::handleCode8);
        codeMap.put(9, this::handleCode9);
        codeMap.put(10, this::handleCode10);
        codeMap.put(11, this::handleCode11);
        codeMap.put(12, this::handleCode12);
        codeMap.put(13, this::handleCode13);
        codeMap.put(14, this::handleCode14);
        codeMap.put(15, this::handleCode15);
        codeMap.put(16, this::handleCode16);
    }
    @Resource
    private HCNetSDK hcNetSDK;

    /**
     * 根据传入的操作代码来选择并执行不同的策略
     *
     * @param code
     */
    public static void handleCode(int code, int userId) {
        Consumer<Integer> handler = codeMap.get(code);
        if (handler != null) {
            handler.accept(userId);
        } else {
            handleUnknownCode(code);
        }
    }
    /**
     * PTZ云台控制逻辑处理方法
     *
     * @param userId
     * @param direction
     */
    private void PTZControlHandle(int userId, int direction) {
        //开始操作
        if (!hcNetSDK.NET_DVR_PTZControl_Other(userId, 1, direction, 0)) {
            LOGGER.error("开始云台操作失败,错误码为：" + hcNetSDK.NET_DVR_GetLastError());
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            // 可以记录日志或者进行其他适当的处理
            System.err.println("线程被中断：" + e.getMessage());
        }
        //停止左转
        if (!hcNetSDK.NET_DVR_PTZControl_Other(userId, 1, direction, 1)) {
            LOGGER.error("停止云台操作失败,错误码为：" + hcNetSDK.NET_DVR_GetLastError());
        }
    }
    private void handleCode1(int userId) {
        // 处理code为1的情况,左上
        PTZControlHandle(userId, hcNetSDK.UP_LEFT);
        //        PTZControlHandle(userId, hcNetSDK.TILT_UP);
        //        PTZControlHandle(userId, hcNetSDK.PAN_LEFT);
    }

    private void handleCode2(int userId) {
        // 处理code为2的情况,上
        PTZControlHandle(userId, hcNetSDK.TILT_UP);
    }

    private void handleCode3(int userId) {
        // 处理code为3的情况,右上
        PTZControlHandle(userId, hcNetSDK.UP_RIGHT);
        //        PTZControlHandle(userId, hcNetSDK.TILT_UP);
        //        PTZControlHandle(userId, hcNetSDK.PAN_RIGHT);

    }
    private void handleCode4(int userId) {
        // 处理code为4的情况,左转
        PTZControlHandle(userId, hcNetSDK.PAN_LEFT);
    }
    private void handleCode5(int userId) {
        // 处理code为5的情况,左右自动扫描
        PTZControlHandle(userId, hcNetSDK.PAN_AUTO);
    }
    private void handleCode6(int userId) {
        // 处理code为6的情况,右转
        PTZControlHandle(userId, hcNetSDK.PAN_RIGHT);

    }
    private void handleCode7(int userId) {
        // 处理code为7的情况,左下
        PTZControlHandle(userId, hcNetSDK.DOWN_LEFT);
        //        PTZControlHandle(userId, hcNetSDK.PAN_LEFT);
        //        PTZControlHandle(userId, hcNetSDK.TILT_DOWN);
    }
    private void handleCode8(int userId) {
        // 处理code为8的情况,下
        PTZControlHandle(userId, hcNetSDK.TILT_DOWN);
    }
    private void handleCode9(int userId) {
        // 处理code为9的情况,右下
        PTZControlHandle(userId, hcNetSDK.DOWN_RIGHT);
        //        PTZControlHandle(userId, hcNetSDK.PAN_RIGHT);
        //        PTZControlHandle(userId, hcNetSDK.TILT_DOWN);
    }

    private void handleCode10(int userId) {
        //调大焦距
        PTZControlHandle(userId, hcNetSDK.ZOOM_IN);
    }
    private void handleCode11(int userId) {
        //调小焦距
        PTZControlHandle(userId, hcNetSDK.ZOOM_OUT);
    }
    private void handleCode12(int userId) {
        //调大光圈
        PTZControlHandle(userId, hcNetSDK.IRIS_OPEN);
    }
    private void handleCode13(int userId) {
        //调小光圈
        PTZControlHandle(userId, hcNetSDK.IRIS_CLOSE);
    }
    private void handleCode14(int userId) {
        //接通灯光电源
        PTZControlHandle(userId, hcNetSDK.LIGHT_PWRON);
    }
    private void handleCode15(int userId) {
        //焦点前调
        PTZControlHandle(userId, hcNetSDK.FOCUS_NEAR);
    }
    private void handleCode16(int userId) {
        //焦点后调
        PTZControlHandle(userId, hcNetSDK.FOCUS_FAR);
    }

    private static void handleUnknownCode(int code) {
        // 处理未知代码的情况
        throw new IllegalArgumentException("不支持的操作代码:" + code);
    }
}
