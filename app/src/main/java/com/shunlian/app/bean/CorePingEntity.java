package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CorePingEntity {
    public String count;
    public String page;
    public String page_size;
    public String total_page;
    public List<MData> brand_list;
    public List<MData> banner;
    public MData brand;
    public List<MData> goods_list;
    public Share share;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String start_time;
        public String end_time;
        public String type;
        public String item_id;
        public String img;
        public String id;
        public String bg_img;
        public String thumb;
        public String market_price;
        public String price;
        public String sales;
        public String title;
        public String slogan;
        public String content;
        public String logo;
        public String promotion_type;
        public String promotion_id;
        public String count_down;
        public Share share;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Share {
        public String share_url;
        public String title;
        public String content;
        public String logo;

    }
}
