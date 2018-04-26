package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/9/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextMessage extends BaseMessage {
    public TextMessageBody msg_body;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TextMessageBody {
        public String text;
    }
}
