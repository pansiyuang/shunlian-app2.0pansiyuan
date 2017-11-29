package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StorePromotionGoodsListEntity {
    public List<Lable> lable;


    @Override
    public String toString() {
        return "StorePromotionGoodsListEntity{" +
                "lable=" + lable +
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

}
