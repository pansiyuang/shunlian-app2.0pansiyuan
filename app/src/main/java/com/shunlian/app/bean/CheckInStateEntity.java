package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckInStateEntity {
    public String today_is_singed;
    public String credit1;
    public String rule;
    public String title;
    public GoodsList goodslist;

    @Override
    public String toString() {
        return "CheckInStateEntity{" +
                "today_is_singed='" + today_is_singed + '\'' +
                ", credit1='" + credit1 + '\'' +
                ", rule='" + rule + '\'' +
                ", title='" + title + '\'' +
                ", goodslist=" + goodslist +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsList {
        public String count;
        public int total_page;
        public int page;
        public int page_size;
        public List<MData> list;

        @Override
        public String toString() {
            return "GoodsList{" +
                    "count='" + count + '\'' +
                    ", total_page='" + total_page + '\'' +
                    ", page='" + page + '\'' +
                    ", page_size='" + page_size + '\'' +
                    ", list=" + list +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class MData {
            public String id;
            public String title;
            public String thumb;
            public String price;
            public String from;

            @Override
            public String toString() {
                return "MData{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", thumb='" + thumb + '\'' +
                        ", price='" + price + '\'' +
                        ", from='" + from + '\'' +
                        '}';
            }
        }
    }
}
