package com.ruoyi.device.config;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.ruoyi.device.sdk.HCNetSDK;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
@Service
public class FRealDataCallBack_V30 implements HCNetSDK.FRealDataCallBack_V30 {

    @Override
    public void invoke(int lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, Pointer pUser)
            throws IOException {
    }
}
