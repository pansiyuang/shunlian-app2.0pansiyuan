package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/11/20.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawEntity {
    public String message_type;
    public int status;
    public String msg;
    public String msg_id;
}
