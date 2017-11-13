package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsDeatilEntity {

    public String id;
    public String title;
    public String detail_url;
    public String free_shipping;
    public String area;
    public String market_price;
    public String price;
    public String max_price;
    public String stock;
    public String thumb;
    public ArrayList<String> pics;

    public ArrayList<Sku> sku;//属性组合列表
    public ArrayList<Specs> specs;
    public GoodsData goods_data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsData {
        //销量
        public String sales;
        //浏览量
        public String views;
        //好评率
        public String star_rate;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sku {
        public String id;
        public String price;
        public String market_price;
        public String weight;
        public String stock;
        public String specs;
        public String name;

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Specs {
        public String id;
        public String name;
        public String show_type;
        public ArrayList<Values> values;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Values {
            public String id;
            public String name;
            public String memo;
            public String thumb;
            private boolean isSelect;

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }
        }
    }


}
