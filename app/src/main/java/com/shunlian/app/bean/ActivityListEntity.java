package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityListEntity {
    public List<Menu> menu;
    @JsonProperty(value = "data")//关键字重名
    public MData datas;

    @Override
    public String toString() {
        return "ActivityListEntity{" +
                "menu=" + menu +
                ", datas=" + datas +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Menu {
        public String id;
        public String time;
        public String content;

        @Override
        public String toString() {
            return "Menu{" +
                    "id='" + id + '\'' +
                    ", time='" + time + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String title;
        public String time;
        public String content;
        public String sale;
        public Good goods;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Good {
            public String allPage;
            public String page;
            public String total;
            public String pageSize;
            public List<MList> list;

            @Override
            public String toString() {
                return "Good{" +
                        "allPage='" + allPage + '\'' +
                        ", page='" + page + '\'' +
                        ", total='" + total + '\'' +
                        ", pageSize='" + pageSize + '\'' +
                        ", list=" + list +
                        '}';
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class MList {
                public String id;
                public String goods_id;
                public String title;
                public String market_price;
                public String store_id;
                public String act_price;
                public String goods_pic;
                public String session_status;
                public String stock;
                public String surplus_stock;
                public String remind_status;
                public String remind_count;
                public int percent;
                public String str_surplus_stock;
                public String sale;

                @Override
                public String toString() {
                    return "MList{" +
                            "id='" + id + '\'' +
                            ", goods_id='" + goods_id + '\'' +
                            ", title='" + title + '\'' +
                            ", market_price='" + market_price + '\'' +
                            ", store_id='" + store_id + '\'' +
                            ", act_price='" + act_price + '\'' +
                            ", goods_pic='" + goods_pic + '\'' +
                            ", session_status='" + session_status + '\'' +
                            ", stock='" + stock + '\'' +
                            ", surplus_stock='" + surplus_stock + '\'' +
                            ", remind_status='" + remind_status + '\'' +
                            ", remind_count='" + remind_count + '\'' +
                            '}';
                }
            }
        }
    }
}
