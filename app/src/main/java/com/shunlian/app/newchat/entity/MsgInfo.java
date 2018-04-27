package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Created by Administrator on 2017/9/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsgInfo {
    public int uid;
    public int kid;
    public int user_id;
    public int type;
    public int is_read;
    public String message;
    public long send_time;
    public String id;
    public String msg_id;


    public MsgInfo() {
    }

    @Override
    public String toString() {
        return "uid:" + uid + " kid:" + kid + " user_id:" + user_id + " type:" + type +
                " is_read:" + is_read + " message:" + message +
                " send_time:" + send_time + " id:" + id + " msg_id:" + msg_id;
    }
}
