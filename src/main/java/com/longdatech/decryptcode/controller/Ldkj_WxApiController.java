package com.longdatech.decryptcode.controller;

import com.alibaba.fastjson.JSONObject;
import com.longdatech.decryptcode.utils.MyHttpRequestUtil;
import com.longdatech.decryptcode.utils.SignUtil;
import com.longdatech.decryptcode.utils.WxConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


@Slf4j
@Api(description = "微信接口相关控制器")
@RestController
@RequestMapping("/wxapi")
public class Ldkj_WxApiController {

    /**
     * @description 解密code得到openid
     * @author: liyinlong
     * @date 2019-05-05 13:58
     * @param code
     * @return
     */
    @ApiOperation("解密opendid")
    @GetMapping("/decryptCode")
    public String decryptCode(@RequestParam String code){
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
    @ApiOperation("发送模板消息")
    @GetMapping("/sendTemplateMessage")
    public String sendTemplateMessage(@RequestParam String formId,@RequestParam String openId){


        return "";
    }

    /**
     * @description 微信公众号服务器配置校验token
     * @author: liyinlong
     * @date 2019-05-09 9:38
     * @return
     */
    @ApiOperation("微信公众号服务器配置校验token")
    @RequestMapping("/checkToken")
    public void checkToken(HttpServletRequest request,HttpServletResponse response){
        //token验证代码段
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