package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/24.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpertEntity {
    public Pager pager;
    public List<Expert> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public int page;
        public int page_size;
        public int count;
        public int total_page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Expert {
        public String member_id;
        public String hot_val;
        public String hot;
        public String avatar;
        public String nickname;
        public int expert;
        public int add_v;
        public String expert_icon;
        public String v_icon;
        public int focus_status;
    }
}
