package com.longdatech.decryptcode.service;

public interface LdkjWxApiService {
    void sendMiniTemplate(String formid,String openid);
    void sendPubTemplateMessage(String pubOpenid);
    void createQrCode(String strInfo);
    String getPubQrCode();
}
