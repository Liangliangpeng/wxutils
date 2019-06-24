package com.longdatech.decryptcode;

import com.longdatech.decryptcode.mailutils.MailUtil;
import org.junit.Test;

public class WxUtilsTests {

    @Test
    public void testSendMessage() {
        try{
            MailUtil.sendSimpleMessage("2370775541@qq.com","测试发邮件。");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
