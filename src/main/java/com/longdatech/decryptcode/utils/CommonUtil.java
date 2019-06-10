package com.longdatech.decryptcode.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 通用工具类
 * 
 * @author liufeng
 */
@Slf4j
public class CommonUtil {
    
    // 小程序获取token凭证获取（GET）url
    public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    //公众号获取根据openid获取unionid url
    public final static String unionid_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    //发送小程序模板消息url
    public static final String SEND_MINI_MESSAGE_REQUEST_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";

    // 生成小程序二维码
    public final static String QRCODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";

    //生成含参数的公众号二维码
    public final static String CONTAIN_PARAMS_QRCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";

    //生成的公众号二维码图片前缀
    public final static String MP_QRCODE_PRE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";


    /**
     * 发送 https 请求
     * 
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject（通过 JSONObject.get(key) 的方式获取 JSON 对象的属性值）
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        
        JSONObject jsonObject = null;
        
        try {
            // 创建 SSLContext 对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述 SSLContext 对象中得到 SSLSocketFactory 对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            
            // 当 outputStr 不为 null 时，向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error(" 连接超时：{}", ce);
        } catch (Exception e) {
            log.error("https 请求异常：{}", e);
        }
        
        return jsonObject;
    }

    
    /**
     * URL编码（utf-8）
     * 
     * @param source
     * @return
     */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 根据内容类型判断文件扩展名
     * 
     * @param contentType 内容类型
     * @return
     */
    public static String getFileExt(String contentType) {
        String fileExt = "";
        if ("image/jpeg".equals(contentType)){
            fileExt = ".jpg";
        } else if ("audio/mpeg".equals(contentType)){
            fileExt = ".mp3";
        }else if ("audio/amr".equals(contentType)){
            fileExt = ".amr";
        }else if ("video/mp4".equals(contentType)){
            fileExt = ".mp4";
        } else if ("video/mpeg4".equals(contentType)){
            fileExt = ".mp4";
        }
        return fileExt;
    }
    
    
    
    /**
     * 获取接口访问凭证
     * 
     * @param appid 凭证
     * @param appsecret 密钥
     * @return
     */
    public static Token getToken(String appid, String appsecret) {
        Token token = null;
        String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
        log.info(jsonObject.toString());
        if (null != jsonObject) {
            try {
                token = new Token();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInteger("expires_in"));
            } catch (JSONException e) {
                token = null;
                // 获取token失败
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return token;
    }

    /**
     * @description 发送小程序模板消息
     * @author: liyinlong
     * @date 2019-05-06 10:47
     * @param wxMssVo
     * @return
     */
    public static String sendTemplateMessage(WxMssVo wxMssVo) {
        String info = "";
        try {
            //创建连接
            URL url = new URL(wxMssVo.getRequest_url());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Type", "utf-8");
            connection.connect();
            //POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            if(wxMssVo.getMessageType() == 1){
                //小程序模板消息
                obj.put("access_token", wxMssVo.getAccess_token());
                obj.put("touser", wxMssVo.getTouser());
                obj.put("template_id", wxMssVo.getTemplate_id());
                obj.put("form_id", wxMssVo.getForm_id());
                obj.put("page", wxMssVo.getPage());

                JSONObject jsonObject = new JSONObject();

                for (int i = 0; i < wxMssVo.getParams().size(); i++) {
                    JSONObject dataInfo = new JSONObject();
                    dataInfo.put("value", wxMssVo.getParams().get(i).getValue());
                    if(wxMssVo.getParams().get(i).getColor() == null){
                        dataInfo.put("color", "#ffffff");
                    }
                    jsonObject.put("keyword" + (i + 1), dataInfo);
                }

                obj.put("data", jsonObject);
            }else{
                //公众号模板消息
                obj.put("touser", wxMssVo.getTouser());
                obj.put("template_id", wxMssVo.getTemplate_id());

//                JSONObject miniprogram = new JSONObject();
//                miniprogram.put("appid", "小程序appid");
//                miniprogram.put("pagepath","pages/login/login");
//                obj.put("miniprogram", miniprogram);

                JSONObject jsonObject = new JSONObject();

                for (int i = 0; i < wxMssVo.getParams().size(); i++) {
                    JSONObject dataInfo = new JSONObject();
                    dataInfo.put("value", wxMssVo.getParams().get(i).getValue());
                    if(wxMssVo.getParams().get(i).getColor() == null){
                        dataInfo.put("color", "#000000");
                    }
                    jsonObject.put(wxMssVo.getParams().get(i).getKey(), dataInfo);
                }
                log.info(obj.toString());
                obj.put("data", jsonObject);
            }
            log.info(obj.toString());
            out.write(obj.toString().getBytes());
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            info = sb.toString();
            System.out.println(sb);
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * @description 获取公众号unionid
     * @author: liyinlong
     * @date 2019-05-07 16:22
     * @param access_token
     * @param openid
     * @return
     */
    public static String getUnionid(String access_token,String openid){
        log.info("发起GET请求获取凭证");
        String requestUrl = unionid_url.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
        log.info(jsonObject.toString());
        String unionId = "";
        if (null != jsonObject) {
            try {
                unionId = jsonObject.getString("unionid");
                log.info("获取unionid成功，unionid:" + unionId);
            } catch (JSONException e) {
                unionId = null;
                log.error("获取unionid失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return unionId;
    }

}