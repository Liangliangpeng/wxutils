package com.longdatech.decryptcode.service;

import com.longdatech.decryptcode.utils.ServerResponse;

public interface LdkjWxApiService {
    void sendMiniTemplate(String formid,String openid);
    void sendPubTemplateMessage(String pubOpenid);
    void createQrCode(String strInfo);
    ServerResponse getPubQrCode();
    public void handleScanQrcode(String pubOpenid, String ticket);
}
