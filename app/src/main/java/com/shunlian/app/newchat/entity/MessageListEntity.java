package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageListEntity {
    public List<Msg> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Msg {
        public String title;
        public String date;
        public String type;
        public String is_del;
    }
}
