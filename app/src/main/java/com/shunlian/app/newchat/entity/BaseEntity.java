package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseEntity {
    public String message_type;//信息类型
    public int state;//初始化状态
    public int status;//初始化状态
    public String msg;//错误提示信息

    @Override
    public String toString() {
        return "BaseEntity{" +
                "message_type='" + message_type + '\'' +
                ", state='" + state + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}