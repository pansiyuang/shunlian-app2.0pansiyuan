package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by augus on 2017/9/6 0006.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageEntity extends BaseEntity {
    public MsgInfo msg_info;

    @Override
    public String toString() {
        return "MessageEntity{" +
                "msg_info=" + msg_info +
                '}';
    }
}
