package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CorePingEntity {
    public List<MData> brand_list;
    public List<MData> banner;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String start_time;
        public String end_time;
        public String type;
        public String item_id;
        public String img;
        public String id;
        public String bg_img;
        public String title;
        public String slogan;
        public String content;
        public String logo;
        public String promotion_type;
        public String promotion_id;
        public String count_down;
    }
}
