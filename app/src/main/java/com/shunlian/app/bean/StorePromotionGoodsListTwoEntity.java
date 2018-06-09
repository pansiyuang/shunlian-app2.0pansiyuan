package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StorePromotionGoodsListTwoEntity {
    public Lists list;

    @Override
    public String toString() {
        return "StorePromotionGoodsListTwoEntity{" +
                "list=" + list +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Lists {
        public List<Good> goods;

        @Override
        public String toString() {
            return "Lists{" +
                    "goods=" + goods +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Good {
            public String id;
            public String title;
            public String price;
            public String old_price;
            public List<Data> data;

            @Override
            public String toString() {
                return "Good{" +
                        "title='" + title + '\'' +
                        ", price='" + price + '\'' +
                        ", old_price='" + old_price + '\'' +
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
