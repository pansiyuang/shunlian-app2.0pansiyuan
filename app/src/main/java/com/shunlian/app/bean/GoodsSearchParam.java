package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
public class GoodsSearchParam implements Serializable {
    public String keyword;
    public String min_price;
    public String max_price;
    public String is_free_ship;
    public String brand_ids;
    public String cid;
    public String send_area;
    public List<Attr> attr_data;
    //    public String attr_data;
    public String sort_type;

    @Override
    public String toString() {
        return "GoodsSearchParam{" +
                "keyword='" + keyword + '\'' +
                ", min_price='" + min_price + '\'' +
                ", max_price='" + max_price + '\'' +
                ", is_free_ship='" + is_free_ship + '\'' +
                ", brand_ids='" + brand_ids + '\'' +
                ", cid='" + cid + '\'' +
                ", send_area='" + send_area + '\'' +
                ", attr_data=" + attr_data +
                ", sort_type='" + sort_type + '\'' +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attr implements Serializable {
        public String attr_name;
        public List<String> attr_vals;

        @Override
        public String toString() {
            return "Attr{" +
                    "attr_name='" + attr_name + '\'' +
                    ", attr_vals=" + attr_vals +
                    '}';
        }
    }
}
