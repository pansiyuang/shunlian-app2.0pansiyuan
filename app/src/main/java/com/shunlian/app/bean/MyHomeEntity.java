package com.shunlian.app.bean;


/**
 * Created by MBENBEN on 2016/10/11 14 : 46.
 */

public class MyHomeEntity {

    public MemberInfo memberInfo;
    public OrderCount orderCount;
    public FavoriteCount favoriteCount;
    public NeedDo needDo;

    @Override
    public String toString() {
        return "Data{" +
                "memberInfo=" + memberInfo +
                ", orderCount=" + orderCount +
                ", favoriteCount=" + favoriteCount +
                ", needDo=" + needDo +
                '}';
    }


    public static class OrderCount {
        public String pending;
        public String waitsend;
        public String receipt;
        public String refund;

        @Override
        public String toString() {
            return "OrderCount{" +
                    "pending='" + pending + '\'' +
                    ", waitsend='" + waitsend + '\'' +
                    ", receipt='" + receipt + '\'' +
                    ", refund='" + refund + '\'' +
                    '}';
        }


    }

    public static class FavoriteCount {
        public String goods;
        public String shop;

        @Override
        public String toString() {
            return "FavoriteCount{" +
                    "goods='" + goods + '\'' +
                    ", shop='" + shop + '\'' +
                    '}';
        }


    }

    public static class NeedDo {
        public String setUsername;
        public String setNickname;
        public String setPassword;
        public String setMobile;
        public String setAvatar;

        @Override
        public String toString() {
            return "NeedDo{" +
                    "setUsername='" + setUsername + '\'' +
                    ", setNickname='" + setNickname + '\'' +
                    ", setPassword='" + setPassword + '\'' +
                    ", setMobile='" + setMobile + '\'' +
                    ", setAvatar='" + setAvatar + '\'' +
                    '}';
        }


    }

    public static class MemberInfo {
        public String nickname;
        public String commission;
        public String is_agent;
        public String reg_time;
        public String agent_time;
        public String credit2;
        public String credit2_freeze;
        public String member_gold_count;
        public String group_gold_count;
        public String spending;
        public String agent_level;
        public String avatar;
        public String uid;
        public String has_shareid;
        public String agent_level_name;

        @Override
        public String toString() {
            return "MemberInfo{" +
                    "nickname='" + nickname + '\'' +
                    ", commission='" + commission + '\'' +
                    ", is_agent='" + is_agent + '\'' +
                    ", reg_time='" + reg_time + '\'' +
                    ", agent_time='" + agent_time + '\'' +
                    ", credit2='" + credit2 + '\'' +
                    ", credit2_freeze='" + credit2_freeze + '\'' +
                    ", member_gold_count='" + member_gold_count + '\'' +
                    ", group_gold_count='" + group_gold_count + '\'' +
                    ", spending='" + spending + '\'' +
                    ", agent_level='" + agent_level + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", uid='" + uid + '\'' +
                    ", has_shareid='" + has_shareid + '\'' +
                    ", agent_level_name='" + agent_level_name + '\'' +
                    '}';
        }

    }
}
