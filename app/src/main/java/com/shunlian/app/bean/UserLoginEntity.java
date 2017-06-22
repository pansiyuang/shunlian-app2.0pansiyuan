package com.shunlian.app.bean;

/**
 * Created by MBENBEN on 2016/10/11 17 : 03.
 */

public class UserLoginEntity {

    public CookieInfo cookieInfo;

    @Override
    public String toString() {
        return "Data{" +
                "cookieInfo=" + cookieInfo +
                '}';
    }

    public static class MemberInfo {
        public String member_id;
        public String shareid;
        public String realname;
        public String mobile;
        public String from_user;
        public String commission;
        public String zhifu;
        public String dzdflag;
        public String flagtime;
        public String dzdtitle;
        public String credit2;
        public String credit2_freeze;
        public String member_gold_count;
        public String group_gold_count;
        public String spending;
        public String agent_level;

        @Override
        public String toString() {
            return "MemberInfo{" +
                    "member_id='" + member_id + '\'' +
                    ", shareid='" + shareid + '\'' +
                    ", realname='" + realname + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", from_user='" + from_user + '\'' +
                    ", commission='" + commission + '\'' +
                    ", zhifu='" + zhifu + '\'' +
                    ", dzdflag='" + dzdflag + '\'' +
                    ", flagtime='" + flagtime + '\'' +
                    ", dzdtitle='" + dzdtitle + '\'' +
                    ", credit2='" + credit2 + '\'' +
                    ", credit2_freeze='" + credit2_freeze + '\'' +
                    ", member_gold_count='" + member_gold_count + '\'' +
                    ", group_gold_count='" + group_gold_count + '\'' +
                    ", spending='" + spending + '\'' +
                    ", agent_level='" + agent_level + '\'' +
                    '}';
        }


    }

    public static class CookieInfo {
        public String pin;
        public String wskey;

        @Override
        public String toString() {
            return "CookieInfo{" +
                    "pin='" + pin + '\'' +
                    ", wskey='" + wskey + '\'' +
                    '}';
        }


    }
}

