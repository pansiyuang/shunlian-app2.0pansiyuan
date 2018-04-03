package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Administrator on 2017/9/20.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    public String from_user_id;
    public String from_id;
    public String from_type;
    public String from_nickname;
    public String from_headurl;
    public String to_user_id;
    public String to_id;
    public String tag_id;
    public String to_type;
    public String to_nickname;
    public String to_headurl;
    public String msg_type;
    public String msg_body;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSelf() {
//        BaseApplication myApp = (BaseApplication) BaseApplication.getContext();
//        String userId = myApp.spUserInfo.getString("user_id", "");
//        LogUtil.httpLogW("id:" + userId);
//        if (userId.equals(from_user_id)) {
//            return true;
//        }
        return false;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from_user_id='" + from_user_id + '\'' +
                ", from_id='" + from_id + '\'' +
                ", from_type='" + from_type + '\'' +
                ", from_nickname='" + from_nickname + '\'' +
                ", from_headurl='" + from_headurl + '\'' +
                ", to_user_id='" + to_user_id + '\'' +
                ", to_id='" + to_id + '\'' +
                ", tag_id=" + tag_id + '\'' +
                ", to_type='" + to_type + '\'' +
                ", to_nickname='" + to_nickname + '\'' +
                ", to_headurl='" + to_headurl + '\'' +
                ", msg_type='" + msg_type + '\'' +
                ", msg_body='" + msg_body + '\'' +
                '}';
    }

    public static Message str2Msg(String msg) {
        Message message = null;
        try {
            message = new ObjectMapper().readValue(msg, Message.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static String msg2Str(Message message) {
        String str = null;
        try {
            str = new ObjectMapper().writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}