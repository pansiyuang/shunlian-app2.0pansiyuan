package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreNewGoodsListEntity {
    public List<Data> data;

    @Override
    public String toString() {
        return "StoreNewGoodsListEntity{" +
                "data=" + data +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public String day;
        public MData mDatal;
        public MData mDatar;
        public List<MData> data;

        @Override
        public String toString() {
            return "Data{" +
                    "day='" + day + '\'' +
                    ", mDatal=" + mDatal +
                    ", mDatar=" + mDatar +
                    ", data=" + data +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class MData {
            public String id;
            public String market_price;
            public String title;
            public String thumb;
            public String update_time;
            public String price;
            public String whole_thumb;
            public String type;
            public String item_id;
            public String store_id;
            public String self_buy_earn;
            public String share_buy_earn;

            @Override
            public String toString() {
                return "MData{" +
                        "id='" + id + '\'' +
                        ", market_price='" + market_price + '\'' +
                        ", title='" + title + '\'' +
                        ", thumb='" + thumb + '\'' +
                        ", update_time='" + update_time + '\'' +
                        ", price='" + price + '\'' +
                        ", whole_thumb='" + whole_thumb + '\'' +
                        ", type='" + type + '\'' +
                        ", item_id='" + item_id + '\'' +
                        ", store_id='" + store_id + '\'' +
                        '}';
            }
        }
    }
}
