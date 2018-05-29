package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeekSaleTopEntity {
    public List<Cate> list;
    public String month_banner;
    public String update_time;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cate {
        public String sales;
        public String nickname;
        public String avatar;
        public String role;
        public String plus_name;
    }
}
