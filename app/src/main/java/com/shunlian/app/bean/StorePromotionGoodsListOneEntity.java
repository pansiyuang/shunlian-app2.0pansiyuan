package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StorePromotionGoodsListOneEntity {
    public Lists list;
    public List<MData> mData;

    @Override
    public String toString() {
        return "StorePromotionGoodsListOneEntity{" +
                "list=" + list +
                ", mData=" + mData +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String page;
        public String mRemark;
        public String type;
        public String allPage;
        public String total;
        public String pageSize;
        public Lists.Good.Data ldata;
        public Lists.Good.Data rdata;

        @Override
        public String toString() {
            return "MData{" +
                    "page='" + page + '\'' +
                    ", mRemark='" + mRemark + '\'' +
                    ", type='" + type + '\'' +
                    ", allPage='" + allPage + '\'' +
                    ", total='" + total + '\'' +
                    ", pageSize='" + pageSize + '\'' +
                    ", ldata=" + ldata +
                    ", rdata=" + rdata +
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
            public Data ldata;
            public Data rdata;
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
                public String giftGoodsName;

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
                            ", giftGoodsName='" + giftGoodsName + '\'' +
                            '}';
                }
            }
        }
    }

}
