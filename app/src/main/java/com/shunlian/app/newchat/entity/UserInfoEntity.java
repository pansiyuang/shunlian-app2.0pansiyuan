package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by augus on 2017/9/5 0005.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoEntity extends BaseEntity {
    public String message_type;//信息类型
    public String state;//状态 0 成功，其他均为失败
    public Info info;

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "message_type='" + message_type + '\'' +
                ", state='" + state + '\'' +
                ", info=" + info +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Info {
        public List<Friend> friends;//好友列表
        public User user;//当前用户信息
        public Uread uread; //未读消息情况
        public String role_type;
        public Bind bind;
        public BindSeller bind_seller;

        @Override
        public String toString() {
            return "Info{" +
                    "friends=" + friends +
                    ", user=" + user +
                    ", uread=" + uread +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Bind {
            public boolean is_bind;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Friend implements Serializable {
            public String uid;//商家ID 或者系统客服管理员ID
            public String creat_time;//成为好友时间
            public String update_time; //最近沟通时间
            public String nickname;//昵称
            public String headurl;//头像
            //在线状态：1在线，2空闲，3离开，4隐身，5离线
            public String line_status;
            public String user_id;//好友id
            //用户类型 0普通用户，1：客服管理员，2：普通系统客服，3，商家客服管理员，4，商家普通客服
            public String type;
            public int unReadNum;

            @Override
            public String toString() {
                return "Friend{" +
                        "uid='" + uid + '\'' +
                        ", creat_time='" + creat_time + '\'' +
                        ", update_time='" + update_time + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", headurl='" + headurl + '\'' +
                        ", line_status='" + line_status + '\'' +
                        ", user_id='" + user_id + '\'' +
                        ", type='" + type + '\'' +
                        ", unReadNum='" + unReadNum +
                        '}';
            }

        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class User implements Serializable {
            public String user_id;//用户id
            public String type;//用户类型
            public String join_id;
            public String shop_id;
            public String pid;
            public String reception1;
            public String reception2;
            public String line_status;//在线状态
            public String nickname;//昵称
            public String headurl;//头像
            public String member_id; //普通用户对应ma 表id，客服对应members表uid
            public String login_time;//成功建立链接时间
            public String client;// 客户端类型 WEB/APP
            public String off_time;//上次离线时间
            public String create_time;//创建时间

            @Override
            public String toString() {
                return "User{" +
                        "type='" + type + '\'' +
                        ", member_id='" + member_id + '\'' +
                        ", line_status='" + line_status + '\'' +
                        ", login_time='" + login_time + '\'' +
                        ", client='" + client + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", headurl='" + headurl + '\'' +
                        ", off_time='" + off_time + '\'' +
                        ", create_time='" + create_time + '\'' +
                        ", user_id='" + user_id + '\'' +
                        '}';
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Uread {
            public String total;//总未读消息
            public List<UreadList> uread_list;//未读消息情况列表

            @Override
            public String toString() {
                return "Uread{" +
                        "total='" + total + '\'' +
                        ", uread_list=" + uread_list +
                        '}';
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class UreadList {
                public String num;//未读消息数量
                public String uid;//商家id或者系统客服管理员ID

                @Override
                public String toString() {
                    return "UreadList{" +
                            "num='" + num + '\'' +
                            ", uid='" + uid + '\'' +
                            '}';
                }
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BindAdmin {
        public boolean is_bind;
        public boolean is_manage;
        public Info.User user;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BindSeller {
        public boolean is_bind;
        public boolean is_manage;
        public Info.User user;
    }
}
