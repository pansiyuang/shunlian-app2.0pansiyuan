package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDrawEntity {
    public int status;
    public int draw_count;
    public DrawPrize prize;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DrawPrize {
        public String id;
        public String name;
        public String image;
        public int type;
        public String link;
        public int egg;
        public String desc;
    }
}
