package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreIntroduceEntity {
    public String store_id;
    public String store_name;
    public String store_type;
    public String seller_id;
    public String seller_name;
    public String store_label;
    public String store_phone;
    public String store_wx;
    public String store_qq;
    public String store_time;
    public String paying_amount;
    public String store_collect;
    public Evaluate evaluate;
    public String baseurl;

    @Override
    public String toString() {
        return "StoreIntroduceEntity{" +
                "store_id='" + store_id + '\'' +
                ", store_name='" + store_name + '\'' +
                ", store_type='" + store_type + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", store_label='" + store_label + '\'' +
                ", store_phone='" + store_phone + '\'' +
                ", store_wx='" + store_wx + '\'' +
                ", store_qq='" + store_qq + '\'' +
                ", store_time='" + store_time + '\'' +
                ", paying_amount='" + paying_amount + '\'' +
                ", store_collect='" + store_collect + '\'' +
                ", evaluate=" + evaluate +
                ", baseurl='" + baseurl + '\'' +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Evaluate {
        public String praise_rate;
        public List<Pj> pj;

        @Override
        public String toString() {
            return "Evaluate{" +
                    "praise_rate='" + praise_rate + '\'' +
                    ", pj=" + pj +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Pj {
            public String name;
            public String score;
            public String explain;

            @Override
            public String toString() {
                return "Pj{" +
                        "name='" + name + '\'' +
                        ", score='" + score + '\'' +
                        ", explain='" + explain + '\'' +
                        '}';
            }
        }
    }
}
