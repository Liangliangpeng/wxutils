package com.longdatech.decryptcode.utils;

import lombok.Data;

/**
 * @description 模板消息详细参数封装
 * @author: liyinlong
 * @date 2018/10/8 18:11
 */
@Data
public class TemplateData {
    private String key;
    private String value;
    private String color;

    public TemplateData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public TemplateData(String value) {
        this.value = value;
    }

}
