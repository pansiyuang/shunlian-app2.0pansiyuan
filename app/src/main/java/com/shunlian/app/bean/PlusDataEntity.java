package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlusDataEntity {
    public BaseInfo base_info;
    public Achievement achievement;
    public List<Chart> chart;
    public String max_sale;
    public String max_num;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BaseInfo {
        public String avatar;
        public String nickname;
        public int role;
        public String role_desc;
        public String expire_time;
        public int upgrade_process;
        public String invite_reward;
        public String invite_strategy;
        public ShareInfo share_info;
        public int plus_num;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Achievement {
        public String total_sales;
        public String plus_num;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Chart {
        public String date;
        public String total_sales;
        public String plus_num;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShareInfo{
        public String title;
        public String content;
        public String pic;
        public String invite_middle_page;
    }
}
