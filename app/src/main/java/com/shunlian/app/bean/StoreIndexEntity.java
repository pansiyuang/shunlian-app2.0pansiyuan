package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreIndexEntity {
    public String decoration_id;
    public String block_id;
    public Head head;
    public List<GoodsDeatilEntity.Voucher> voucher;
    public List<Body> body;
    public String baseUrl;

    @Override
    public String toString() {
        return "Data{" +
                "decoration_id='" + decoration_id + '\'' +
                ", block_id='" + block_id + '\'' +
                ", head=" + head +
                ", voucher=" + voucher +
                ", body=" + body +
                ", baseUrl='" + baseUrl + '\'' +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Head {
        public String decoration_id;
        public String decoration_name;
        public String store_id;
        public String decoration_banner;
        public String decoration_logo;
        public String star;
        public String mark_count;
        public String goods_count;
        public String promotion_count;
        public String new_count;
        public String is_mark;

        @Override
        public String toString() {
            return "Head{" +
                    "decoration_id='" + decoration_id + '\'' +
                    ", decoration_name='" + decoration_name + '\'' +
                    ", store_id='" + store_id + '\'' +
                    ", decoration_banner='" + decoration_banner + '\'' +
                    ", decoration_logo='" + decoration_logo + '\'' +
                    ", mark_count='" + mark_count + '\'' +
                    ", goods_count='" + goods_count + '\'' +
                    ", promotion_count='" + promotion_count + '\'' +
                    ", new_count='" + new_count + '\'' +
                    ", is_mark='" + is_mark + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Voucher {
        public String id;
        public String title;
        public String denomination;
        public String use_condition;
        public String start_time;
        public String end_time;
        public String is_get;

        @Override
        public String toString() {
            return "Voucher{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", denomination='" + denomination + '\'' +
                    ", use_condition='" + use_condition + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", is_get='" + is_get + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        public String block_module_type;
        public String name;
        public String title;
        public String module_type;
        public String goods_ids;
        public Rule rule;
        public List<Datas> data;
        public Datas ldata;
        public Datas rdata;
        public String textare;

        @Override
        public String toString() {
            return "Body{" +
                    "block_module_type='" + block_module_type + '\'' +
                    ", name='" + name + '\'' +
                    ", title='" + title + '\'' +
                    ", module_type='" + module_type + '\'' +
                    ", goods_ids='" + goods_ids + '\'' +
                    ", rule=" + rule +
                    ", data=" + data +
                    ", ldata=" + ldata +
                    ", rdata=" + rdata +
                    ", textare='" + textare + '\'' +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Rule {
            public String goods_count;
            public String min_price;
            public String max_price;
            public String order_by;
            public String category_id;

            @Override
            public String toString() {
                return "Rule{" +
                        "goods_count='" + goods_count + '\'' +
                        ", min_price='" + min_price + '\'' +
                        ", max_price='" + max_price + '\'' +
                        ", order_by='" + order_by + '\'' +
                        ", category_id='" + category_id + '\'' +
                        '}';
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Datas {
            public String id;
            public String title;
            public String thumb;
            public String price;
            public String type;
            public String sales;
            public List<String> label;
            public String store_id;
            public String item_id;
            public String url;
            public String whole_thumb;
            public String description;

            @Override
            public String toString() {
                return "Datas{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", thumb='" + thumb + '\'' +
                        ", price='" + price + '\'' +
                        ", type='" + type + '\'' +
                        ", store_id='" + store_id + '\'' +
                        ", item_id='" + item_id + '\'' +
                        ", url='" + url + '\'' +
                        ", whole_thumb='" + whole_thumb + '\'' +
                        ", description='" + description + '\'' +
                        '}';
            }
        }
    }
}
