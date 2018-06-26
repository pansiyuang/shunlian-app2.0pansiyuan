package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/5/22.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreMessageEntity {
    public MemberAddMsg memberAdd;
    public ShoppingMsg shopping;
    public String memberListH5Url;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MemberAddMsg {
        public String title;
        public String type;
        public String is_read;
        public String date;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShoppingMsg {
        public String title;
        public String type;
        public String is_read;
        public String date;
    }
}
