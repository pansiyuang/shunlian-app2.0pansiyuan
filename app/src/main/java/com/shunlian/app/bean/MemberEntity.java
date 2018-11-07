package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/23.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberEntity {

    public Pager pager;
    public List<Member> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public int page;
        public int page_size;
        public int count;
        public int total_page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Member {
        public String id;
        public String member_id;
        public String nickname;
        public String avatar;
        public int focus_status;
        public int expert;
        public int add_v;
        public String expert_icon;
        public String v_icon;
    }
}
