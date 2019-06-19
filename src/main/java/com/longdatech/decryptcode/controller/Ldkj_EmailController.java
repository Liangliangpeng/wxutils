package com.longdatech.decryptcode.controller;

import com.longdatech.decryptcode.mailutils.MailUtil;
import com.longdatech.decryptcode.utils.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description 发送邮件控制器
 * @author: liyinlong
 * @date 2019-06-18 11:11
 */

@Slf4j
@Api(description = "4.邮件控制器")
@RestController
@RequestMapping("/email")
public class Ldkj_EmailController {

    /**
     * @description
     * @author: liyinlong
     * @date 2019-06-18 11:24
     * @param receiverEmail 邮件接收者邮箱
     * @param message 文件内容
     * @return
     * @throws Exception
     */
    @ApiOperation("4.1发送简单文本信息")
    @PostMapping("/sendSimpleMessage")
    public ServerResponse sendSimpleMessage(@RequestParam String receiverEmail,@RequestParam String message) throws Exception{
        log.info("4.1发送简单文本信息");
        MailUtil.sendSimpleMessage(receiverEmail,message);
        return ServerResponse.createBySuccess();
    }


}
