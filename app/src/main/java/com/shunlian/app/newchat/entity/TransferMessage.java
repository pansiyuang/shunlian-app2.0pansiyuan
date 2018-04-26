package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/4/24.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferMessage extends BaseMessage {
    public TransferMessageBody msg_body;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TransferMessageBody {
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
