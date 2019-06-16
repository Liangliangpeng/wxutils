package com.longdatech.decryptcode.controller;


import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Api(description = "腾讯云相关接口")
@RestController
@RequestMapping("/txcloudapi")
public class Ldkj_TxCloudApiController {


    @ApiOperation("调用腾讯云验证码接口")
    @GetMapping("/sendMessage")
    public void sendMessage(@RequestParam String tel, HttpServletRequest request, HttpServletResponse response){
        // 短信应用SDK AppID
        int appid = 1400104159; // 1400开头
        // 短信应用SDK AppKey
        String appkey = "";
        // 需要发送短信的手机号码
        String phoneNumber = tel;
        // 短信模板ID，需要在短信应用中申请
        int templateId = 142398; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
        // 签名
        String smsSign = "龙达科技"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
        try {
            int randNumber = (int)((Math.random()*9+1)*100000);
            String[] params = {String.valueOf(randNumber),"30"};
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber, templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            String verificationCode = String.valueOf(randNumber);
            Date date = new Date();
            long v_endTime = date.getTime() + 1000*60*3;
            request.getSession().setAttribute("v_endTime",v_endTime);
            request.getSession().setAttribute("v_verificationCode",verificationCode);
        } catch (Exception e) {
            // 错误
            e.printStackTrace();
        }
    }
}
