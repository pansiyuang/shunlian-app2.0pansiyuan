package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/5/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferOtherEntity extends BaseEntity {
    public TransferInfo info;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TransferInfo {
        public String sid;
        public String kidz;
        public String z_nickname;
        public String z_headurl;
        public String user_id;
        public String code;
        public String u_nickname;
        public String u_headurl;
        public String kidj;
        public String j_nickname;
        public String j_headurl;
        public String item;
        public String create_time;
        public String id;
    }
}
