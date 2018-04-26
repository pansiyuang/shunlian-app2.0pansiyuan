package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/4/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwitchStatusEntity {
    public String message_type;
    public String status;
    public String msg;
    public String to_role;
}
