package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDrawEntity {
    public int status;
    public String id;
    public String name;
    public String image;
    public int type;
    public String link;
    public int egg;
    public String desc;
    public String draw_id;
    public int draw_count;
    public Voucher voucher;
    public String credit;
    public long gold_egg;
    public int draw_type;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Voucher {
        public String limit_time;
        public String title;
        public String denomination;
        public String use_condition;
        public String start_time;
        public String end_time;
        public String desc;
        public Url url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Url {
        public String type;
        public String item_id;
    }
}
