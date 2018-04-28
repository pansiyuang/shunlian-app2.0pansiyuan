package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoreNewEntity {
    public List<MData> banner_list;
    public List<MData> hot_goods;
    public List<MData> cate_name;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String start_time;
        public String end_time;
        public String type;
        public String item_id;
        public String img;
        public String id;
        public String title;
        public String thumb;
        public String price;
        public String cate_id;
        public String cate_name;
    }
}
