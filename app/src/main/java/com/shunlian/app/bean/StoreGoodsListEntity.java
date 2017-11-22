package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreGoodsListEntity {
    @JsonProperty(value = "data")
    public List<MData> datas;
    public String page;
    public String allPage;
    public String total;
    public String pageSize;

    @Override
    public String toString() {
        return "StoreGoodsListEntity{" +
                "datas=" + datas +
                ", page='" + page + '\'' +
                ", allPage='" + allPage + '\'' +
                ", total='" + total + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String id;
        public String title;
        public String thumb;
        public String price;
        public String market_price;
        public String whole_thumb;
        public String type;
        public String item_id;
        public String store_id;

        @Override
        public String toString() {
            return "MData{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", price='" + price + '\'' +
                    ", market_price='" + market_price + '\'' +
                    ", whole_thumb='" + whole_thumb + '\'' +
                    ", type='" + type + '\'' +
                    ", item_id='" + item_id + '\'' +
                    ", store_id='" + store_id + '\'' +
                    '}';
        }
    }
}
