package com.longdatech.decryptcode.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 微信公众号消息处理工具类
 * @author: liyinlong
 * @date 2019-05-07 11:50
 */

public class WxMessageUtil {

    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {

        Map<String, String> map = new HashMap<String, String>();

        InputStream inputStream = request.getInputStream();

        SAXReader reader = new SAXReader();

        Document document = reader.read(inputStream);

        Element root = document.getRootElement();

        List<Element> elementList = root.elements();

        for (Element element : elementList) {

            map.put(element.getName(), element.getText());

        }

        inputStream.close();

        inputStream = null;

        return map;

    }
}