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
    public Long id;

    public Long msg_id;

    public MsgInfo(int uid, int kid, int user_id, int type, int is_read, String message, long send_time, Long id, Long msg_id) {
        this.uid = uid;
        this.kid = kid;
        this.user_id = user_id;
        this.type = type;
        this.is_read = is_read;
        this.message = message;
        this.send_time = send_time;
        this.id = id;
        this.msg_id = id;
    }

    public MsgInfo() {
    }


    @Override
    public String toString() {
        return "uid:" + uid + " kid:" + kid + " user_id:" + user_id + " type:" + type +
                " is_read:" + is_read + " message:" + message +
                " send_time:" + send_time + " id:" + id + " msg_id:" + msg_id;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getKid() {
        return this.kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIs_read() {
        return this.is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSend_time() {
        return this.send_time;
    }

    public void setSend_time(long send_time) {
        this.send_time = send_time;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMsg_id() {
        return id;
    }

    public void setMsg_id(Long msg_id) {
        this.msg_id = msg_id;
    }
}
