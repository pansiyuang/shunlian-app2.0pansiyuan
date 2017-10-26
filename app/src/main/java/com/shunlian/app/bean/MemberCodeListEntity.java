package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberCodeListEntity {

    public List<ListBean> list;

    @Override
    public String toString() {
        return "DataBean{" +
                "list=" + list +
                '}';
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListBean {
        /**
         * member_id : 1
         * nickname : 成熟小青年
         * avatar : /vagrant_data/wwwroot/sldl2.0/20170703093320_348.png
         * level : V0
         * member_role : 创客
         * heat : 666
         * regtime : 2016-04-30 23:48:18
         * code : 2572672505
         */

        public String member_id;
        public String nickname;
        public String avatar;
        public String level;
        public String member_role;
        public String member_role_msg;
        public String heat;
        public String regtime;
        public String code;

        @Override
        public String toString() {
            return "ListBean{" +
                    "member_id='" + member_id + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", level='" + level + '\'' +
                    ", member_role='" + member_role + '\'' +
                    ", heat=" + heat +
                    ", regtime='" + regtime + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }
    }
}
