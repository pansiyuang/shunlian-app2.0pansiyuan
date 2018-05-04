package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shunlian.app.newchat.websocket.MessageStatus;

/**
 * Created by Administrator on 2017/9/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseMessage {
    public static final int VALUE_RIGHT = 0;
    public static final int VALUE_LEFT = 1;
    public static final int VALUE_SYSTEM = -1;

    public String type;
    public String from_user_id;
    public String from_join_id;
    public String from_type;
    public String from_nickname;
    public String from_headurl;
    public String to_user_id;
    public String to_join_id; //消息接受方关联id 普通用户关联member表 member_id ,平台客服关联admin表id，商家客服关联seller表
    public String seller_id; //（如果是发送对象是商家客服，该字段可不传）
    public String to_shop_id;//如果发送对象是商家客服，需要传店铺id（如果发送对象是商家客服，该字段必须传，其他情况请忽略此字段）
    public String tag_id;
    public String to_type;  //发送对象用户类型 1=平台客服，3=商家客服，0=普通用户
    public String to_nickname;
    public String to_headurl;
    public String msg_type;
    public Extras extras;
    public long sendTime;
    public long read_time;
    public String id;
    public String sid;
    public boolean isFirst;
    public String m_user_id;
    private int sendType;
    private int uReadNum;
    private int status = MessageStatus.SendSucc;

    public int getuReadNum() {
        return uReadNum;
    }

    public void setuReadNum(int uReadNum) {
        this.uReadNum = uReadNum;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }


    @Override
    public String toString() {
        return "type:" + type + " from_user_id:" + from_user_id + " from_join_id:" + from_join_id + " from_type:" + from_type +
                " from_nickname:" + from_nickname + " from_headurl:" + from_headurl +
                " to_user_id:" + to_user_id + " to_join_id:" + to_join_id + " tag_id:" + tag_id +
                " to_type:" + to_type + " to_nickname:" + to_nickname + " to_headurl:" + to_headurl +
                " msg_type:" + msg_type + " sendType:" + sendType + " status:" + status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Extras {

    }
}
