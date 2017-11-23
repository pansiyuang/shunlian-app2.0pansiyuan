package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StorePromotionGoodsListEntity {
    public List<Lable> lable;
    public Lists list;

    @Override
    public String toString() {
        return "StorePromotionGoodsListEntity{" +
                "lable=" + lable +
                ", list=" + list +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Lable {
        public String promotionId;
        public String name;
        public String type;

        @Override
        public String toString() {
            return "Lable{" +
                    "promotionId='" + promotionId + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Lists {
        public List<String> remark;
        public Good goods;

        @Override
        public String toString() {
            return "Lists{" +
                    "remark=" + remark +
                    ", goods=" + goods +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Good {
            public String page;
            public String allPage;
            public String total;
            public String pageSize;
            public List<Data> data;

            @Override
            public String toString() {
                return "Good{" +
                        "page='" + page + '\'' +
                        ", allPage='" + allPage + '\'' +
                        ", total='" + total + '\'' +
                        ", pageSize='" + pageSize + '\'' +
                        ", data=" + data +
                        '}';
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Data {
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
                    return "Data{" +
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
    }

}
