package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/9/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextMessage extends BaseMessage {
    public String msg_body;
}
