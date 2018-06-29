package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMemberEntity {
    public List<ChatMember> list;
    public int unread_total;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatMember implements Serializable {
        public String user_id;//商家ID 或者系统客服管理员ID
        public String m_user_id;
        public String friend_user_id;
        public String join_id;
        public String shop_id;
        public String creat_time;//成为好友时间
        public String update_time; //最近沟通时间
        public String nickname;//昵称
        public String headurl;//头像
        public String line_status; //在线状态：1在线，2空闲，3离开，4隐身，5离线
        //用户类型 0普通用户，1：客服管理员，2：普通系统客服，3，商家客服管理员，4，商家普通客服
        public String type;
        public String sid;
        public int unread_count;
        public String status_msg;
        public int service_user_count;
        public boolean isSelect;
    }
}
