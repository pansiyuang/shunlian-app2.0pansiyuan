package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoverActivityEntity {
    public Pager pager;
    public List<Activity> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public int page;
        public int page_size;
        public int count;
        public int total_page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Activity {
        public String id;
        public String title;
        public String content;
        public String thumb;
        public String count;
        public String refer_member_num;
        public String refer_num;
        public String add_time;
        public List<Member> members;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Member {
        public String member_id;
        public String avatar;
    }
}
