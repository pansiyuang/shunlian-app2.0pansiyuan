package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/23.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZanShareEntity {

    public int page;
    public int page_size;
    public int total;
    public int total_page;
    public List<Msg> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Msg {
        public int type;
        public String member_id;
        public String nickname;
        public String avatar;
        public int add_v;
        public int expert;
        public String v_icon;
        public String expert_icon;
        public Blog blog;
        public String create_time;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Blog {
        public String id;
        public String title;
        public int media;
        public String pic;
    }
}
