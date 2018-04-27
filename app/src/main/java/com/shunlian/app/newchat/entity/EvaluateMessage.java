package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/4/13.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EvaluateMessage extends BaseMessage {
    public EvaluateMessageBody msg_body;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EvaluateMessageBody {
        public Evaluate evaluate;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Evaluate {
        public String id;
        public String sid;
        public int score;
        public String creat_time;
        public String evaluation_time;
    }
}
