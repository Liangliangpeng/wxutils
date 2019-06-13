package com.longdatech.decryptcode.controller;

import com.alibaba.fastjson.JSONObject;
import com.longdatech.decryptcode.service.LdkjWxApiService;
import com.longdatech.decryptcode.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


@Slf4j
@Api(description = "微信接口相关控制器")
@RestController
@RequestMapping("/wxapi")
public class Ldkj_WxApiController {

    @Autowired
    private LdkjWxApiService ldkjWxApiService;

    /**
     * @description 解密code得到openid
     * @author: liyinlong
     * @date 2019-05-05 13:58
     * @param code
     * @return
     */
    @ApiOperation("1.0：微信小程序解密code")
    @GetMapping("/decryptCode")
    public String decryptCode(@RequestParam String code){
        log.info("1.0：微信小程序解密code===>code:" + code);
        String result = MyHttpRequestUtil.sendGet(WxConstant.getDesryptCodeUri(code));
        JSONObject ojb = JSONObject.parseObject(result);
        System.out.println("返回结果:" + ojb);
        Set<String> keys = ojb.keySet();
        keys.forEach(item->{
            System.out.println(item + ":" + ojb.get(item));
        });
        return ojb.getString("openid");
    }

    /**
     * @description 小程序发送模板消息
     * @author: liyinlong
     * @date 2019-05-05 13:58
     * @return
     */
    @ApiOperation("1.1：发送小程序模板消息")
    @GetMapping("/sendMiniTemplateMessage")
    public String sendMiniTemplateMessage(@RequestParam String formId,@RequestParam String openId){
        log.info("1.1：发送小程序模板消息===>formId:" + formId + ",openId:" + openId);
        ldkjWxApiService.sendMiniTemplate(formId,openId);
        return "success";
    }

    /**
     * @description 微信公众号服务器配置校验token
     * @author: liyinlong
     * @date 2019-05-09 9:38
     * @return
     */
    @ApiOperation("1.2：微信公众号服务器配置校验token")
    @RequestMapping("/checkToken")
    public void checkToken(HttpServletRequest request,HttpServletResponse response){
        log.info("1.2：微信公众号服务器配置校验token");
        //token验证代码段

        String tempSignature = request.getParameter("signature");
        if(null != tempSignature){
            try{
                log.info("请求已到达，开始校验token");
                if (StringUtils.isNotBlank(request.getParameter("signature"))) {
                    String signature = request.getParameter("signature");
                    String timestamp = request.getParameter("timestamp");
                    String nonce = request.getParameter("nonce");
                    String echostr = request.getParameter("echostr");
                    log.info("signature[{}], timestamp[{}], nonce[{}], echostr[{}]", signature, timestamp, nonce, echostr);
                    if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                        log.info("数据源为微信后台，将echostr[{}]返回！", echostr);
                        response.getOutputStream().println(echostr);
                    }
                }
            }catch (IOException e){
                log.error("校验出错");
                e.printStackTrace();
            }
        }

    }

    /**
     * @description 该接口url应与服务器配置中填写的URL保持一致，因校验token与处理用户校验事件都只
     * 能用一个URL，所以可以在token验证之后把1.2接口注释掉，把本接口名称改为1.2接口名称
     * @author: liyinlong
     * @date 2019-05-09 12:10
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("1.3：用户与公众号交互事件处理")
    @RequestMapping("/getUserFocus")
    public String handlePubFocus(HttpServletRequest request,HttpServletResponse response){
        log.info("1.3：用户与公众号交互事件处理");
        try{
            Map<String ,String > requestMap = WxMessageUtil.parseXml(request);
            Set<String> keys = requestMap.keySet();
            String messageType = requestMap.get("MsgType");
            String eventType = requestMap.get("Event");
            String openid = requestMap.get("FromUserName");
            if(messageType.equals("event")){
                //判断消息类型是否是事件消息类型
                log.info("公众号====>事件消息");
                log.info("openid:" + openid);
                log.info("Event:" + eventType);
                if(eventType.equals("subscribe")){
                    log.info("公众号====>新用户关注");
                }else if(eventType.equals("unsubscribe")){
                    log.info("公众号====>用户取消关注");
                }else if(eventType.equals("SCAN")){
                    log.info("公众号===>用户扫码动态二维码");
                }else{
                    log.info("公众号===>其他");
                }
            }

            keys.forEach(item->{
                String value = requestMap.get(item);
                log.info(item + "===>" + value);
                if(item.equals("Ticket")){
                    //当前用户进行了扫描动态二维码登录pc管理端操作
                    log.info("==>开始处理扫码登录");
                    log.info("当前用户openid:" + openid);
                    log.info("当前用户Ticket:" + value);
                    ldkjWxApiService.handleScanQrcode(openid,value);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @description 公众号发送模板消息
     * @author: liyinlong
     * @date 2019-05-05 13:58
     * @return
     */
    @ApiOperation("1.4：发送公众号模板消息")
    @GetMapping("/sendPubTemplateMessage")
    public String sendPubTemplateMessage(@RequestParam String openId){
        log.info("1.4：发送公众号模板消息===>openId:" + openId);
        ldkjWxApiService.sendPubTemplateMessage(openId);
        return "success";
    }

    /**
     * @description 生成小程序二维码 官方文档：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/qr-code.html
     * @author: liyinlong
     * @date 2019-05-09 15:00
     * @param strinfo
     * @return
     */
    @ApiOperation("1.5：生成小程序二维码")
    @GetMapping("/createQrCode")
    public String createQrCode(String strinfo){
        log.info("1.5：生成小程序二维码===>strinfo:"+strinfo);
        ldkjWxApiService.createQrCode(strinfo);
        return "success";
    }


    /**
     * 官方文档 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1443433542
     * 注意！！！此接口仅认证的服务号才能使用！！！
     * @description 生成公众号带参数二维码
     * @author: liyinlong
     * @date 2019-06-10 16:14
     * @return
     */
    @ApiOperation("1.6：生成公众号带参二维码")
    @GetMapping("/getPubQrCode")
    public ServerResponse getPubQrCode(){
        log.info("1.6：生成公众号带参二维码");
        return ldkjWxApiService.getPubQrCode();
    }

}
