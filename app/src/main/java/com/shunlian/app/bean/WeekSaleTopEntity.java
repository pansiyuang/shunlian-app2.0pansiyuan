package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeekSaleTopEntity {
    public List<Cate> top_list;
    public String rule_url_for_app;
    public String top_date;
    public String last_update_time;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cate {
        public String nickname;
        public String avatar;
        public String sale;
        public String plus_role;
    }
}
