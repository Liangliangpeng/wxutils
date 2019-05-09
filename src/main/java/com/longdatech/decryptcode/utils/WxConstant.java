package com.longdatech.decryptcode.utils;

/**
 * @description 微信相关常量类
 * @author: liyinlong
 * @date 2019-05-05 11:18
 */
public class WxConstant {

    private static final String appid = "wx2f1ee596ff9062b7";//小程序appid
    private static final String secret = "9e25135c13668e106aac50064c09892e";//小程序密钥
    private static final String grant_type = "authorization_code";//授权类型，此处只需填写 authorization_code

    //参考：https://developers.weixin.qq.com/miniprogram/dev/api-backend/auth.code2Session.html

    public static String getDesryptCodeUri(String code){
        return "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
    }

}
