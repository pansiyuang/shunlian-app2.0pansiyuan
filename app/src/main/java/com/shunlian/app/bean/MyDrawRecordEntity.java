package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyDrawRecordEntity {
    public List<DrawRecord> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DrawRecord {
        public String name;
        public String nickname;
        public String create_time;
    }
}
