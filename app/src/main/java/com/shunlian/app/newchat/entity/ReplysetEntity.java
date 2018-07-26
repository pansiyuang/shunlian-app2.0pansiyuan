package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplysetEntity {
    public List<Replyset> list;
    public int count;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Replyset {
        public String id;
        public String item;
    }
}
