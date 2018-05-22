package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotRdEntity {
    @JsonProperty(value = "data")
    public List<MData> datas;
    public String page;
    public String allPage;
    public String total;
    public String pageSize;
    public String pic;
    public String name;
    public String count_down;
    public String content;
    public Share share;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Share {
        public String share_url;
        public String title;
        public String content;
        public String logo;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String thumb;
        public String price;
        public String market_price;
        public String description;
        public Url url;
        public String is_show;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Url {
            public String type;
            public String item_id;
        }
    }
}
