package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Created by Administrator on 2017/9/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsgInfo {
    public int uid;
    public String kid;
    public int user_id;
    public String sid;
    public int type;
    public int is_read;
    public String message;
    public long send_time;
    public String id;
    public String m_user_id;
    public String msg_id;
    public boolean isWithdraw;
    public boolean isAddTime;

    public MsgInfo() {
    }

    @Override
    public String toString() {
        return "uid:" + uid + " kid:" + kid + " user_id:" + user_id + " type:" + type +
                " is_read:" + is_read + " message:" + message +
                " send_time:" + send_time + " id:" + id + " msg_id:" + msg_id;
    }
}
