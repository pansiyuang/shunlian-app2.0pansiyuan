package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoreHotEntity {
    public List<MData> banner_list;
    public Hot hot_goods;
    public String member_id;
    public List<MData> cate_name;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hot {
        public String count;
        public String total_page;
        public String page;
        public String page_size;
        public List<Goods> goods_list;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Goods {
            public String id;
            public String title;
            public String thumb;
            public String price;
            public String sales;
            public String views;
            public String content;
        }
    }
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
