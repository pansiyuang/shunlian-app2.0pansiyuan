package com.shunlian.app.newchat.entity;

import android.text.TextUtils;

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
    public long sendTime;
    private int sendType;
    private int uReadNum;
    private int status = MessageStatus.SendSucc;

    public int getSendType() {
        if (TextUtils.isEmpty(from_user_id) || "-1".equals(from_user_id)) {
            return VALUE_SYSTEM;
        }
        if (isSelf()) {
            return VALUE_RIGHT;
        } else {
            return VALUE_LEFT;
        }
    }

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

    public boolean isSelf() {//当前号的id为810
//        BaseApplication myApp = (BaseApplication) BaseApplication.getContext();
//        String userId = myApp.spUserInfo.getString("user_id", "");
//        if (userId.equals(from_user_id)) {
//            return true;
//        }
        return false;
    }


    @Override
    public String toString() {
        return "type:" + type + " from_user_id:" + from_user_id + " from_id:" + from_id + " from_type:" + from_type +
                " from_nickname:" + from_nickname + " from_headurl:" + from_headurl +
                " to_user_id:" + to_user_id + " to_id:" + to_id + " tag_id:" + tag_id +
                " to_type:" + to_type + " to_nickname:" + to_nickname + " to_headurl:" + to_headurl +
                " msg_type:" + msg_type + " sendType:" + sendType + " status:" + status;
    }
}
