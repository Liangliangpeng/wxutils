package com.longdatech.decryptcode.utils;

/**
 * @description 微信相关常量类
 * @author: liyinlong
 * @date 2019-05-05 11:18
 */
public class WxConstant {

    public static final String MINI_APPID = "";//小程序appid
    public static final String MINI_SECRET = "";//小程序密钥
    private static final String grant_type = "authorization_code";//授权类型，此处只需填写 authorization_code

    public static final String FUWUHAO__APPID = "";//服务号appid
    public static final String FUWUHAO__SECRET = "";//服务号密钥

    //用于微信小程序
    public static String getDesryptCodeUri(String code){
        return "https://api.weixin.qq.com/sns/jscode2session?appid=" + MINI_APPID + "&secret=" + MINI_SECRET + "&js_code=" + code + "&grant_type=authorization_code";
    }

}
