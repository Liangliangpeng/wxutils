package com.longdatech.decryptcode.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 模板消息请求参数封装
 * @author: liyinlong
 * @date 2018/10/8 16:11
 */
@Data
public class WxMssVo {
   private String touser;
   private String template_id;
   private String page;
   private String form_id;
   private String access_token;
   private String request_url;
   private List<TemplateData> params = new ArrayList<>();
   private int messageType = 1;//1：小程序模板消息  2：公众号模板消息
}