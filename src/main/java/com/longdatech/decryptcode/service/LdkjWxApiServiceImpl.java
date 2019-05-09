package com.longdatech.decryptcode.service;

import com.longdatech.decryptcode.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Service
public class LdkjWxApiServiceImpl implements LdkjWxApiService{

    @Override
    public void sendMiniTemplate(String formid, String openid) {
        Token minitoken = CommonUtil.getToken(WxConstant.MINI_APPID, WxConstant.MINI_SECRET);

        log.info("开始发送服务通知");
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTemplate_id("模板消息id");
        wxMssVo.setTouser(openid);
        wxMssVo.setForm_id(formid);
        wxMssVo.setPage("pages/login/login");
        wxMssVo.setAccess_token(minitoken.getAccessToken());
        wxMssVo.setRequest_url(CommonUtil.SEND_MINI_MESSAGE_REQUEST_URL + minitoken.getAccessToken());
        List<TemplateData> list = new ArrayList<>();

        TemplateData td0 = new TemplateData("内容1");
        TemplateData td1 = new TemplateData("内容2");
        TemplateData td2 = new TemplateData("内容3" );

        list.add(td0);
        list.add(td1);
        list.add(td2);

        wxMssVo.setParams(list);
        CommonUtil.sendTemplateMessage(wxMssVo);
    }

    @Override
    public void sendPubTemplateMessage(String pubOpenid) {


    }
}
