package com.longdatech.decryptcode.service;

import com.longdatech.decryptcode.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@Service
public class LdkjWxApiServiceImpl implements LdkjWxApiService{

    @Override
    public void sendMiniTemplate(String formid, String openid) {
        Token minitoken = CommonUtil.getToken(WxConstant.MINI_APPID, WxConstant.MINI_SECRET);

        log.info("开始发送服务通知");
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTemplate_id("模板id");
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
        Token pubtoken = CommonUtil.getToken(WxConstant.FUWUHAO__APPID, WxConstant.FUWUHAO__SECRET);
        WxMssVo wxMssVo = new WxMssVo();

        wxMssVo.setTemplate_id("模板id");
        wxMssVo.setTouser(pubOpenid);
        wxMssVo.setMessageType(2);
        wxMssVo.setRequest_url("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + pubtoken.getAccessToken());
        List<TemplateData> list = new ArrayList<>();

        TemplateData td0 = new TemplateData("first","xxx");
        TemplateData td1 = new TemplateData("keyword1","xxx");
        TemplateData td2 = new TemplateData("keyword2","xxx");
        TemplateData td3 = new TemplateData("remark","xxx");

        list.add(td0);
        list.add(td1);
        list.add(td2);
        list.add(td3);

        wxMssVo.setParams(list);
        CommonUtil.sendTemplateMessage(wxMssVo);
    }

    @Override
    public void createQrCode(String strInfo) {
        Token minitoken = CommonUtil.getToken(WxConstant.MINI_APPID, WxConstant.MINI_SECRET);
        String accessToken = minitoken.getAccessToken();
        log.info("token: " + accessToken);
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String url = CommonUtil.QRCODE_URL + accessToken;
            Map<String, Object> param = new HashMap<>();
            param.put("scene", strInfo);//不可超过32位
//            param.put("page", "pages/login/login");
            param.put("width", 800);
            param.put("auto_color", false);
            Map<String, Object> line_color = new HashMap<>();
            line_color.put("r", 0);
            line_color.put("g", 0);
            line_color.put("b", 0);
            param.put("line_color", line_color);
            log.info("调用生成微信URL接口传参:" + param);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class,
                    new Object[0]);
            log.info(entity.toString());
            byte[] result = entity.getBody();
            inputStream = new ByteArrayInputStream(result);

            File file = new File("E://file/images/" + "demo" + ".jpg");

            if (!file.getParentFile().exists() && !file.isDirectory()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } else {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
            log.info("upload url:"+file.getAbsolutePath());
            String path = file.getAbsolutePath();
            log.info("file: "+ path.substring(0,path.lastIndexOf(File.separator)));
        } catch (Exception e) {
            log.error("调用小程序生成微信永久小程序码URL接口异常", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
