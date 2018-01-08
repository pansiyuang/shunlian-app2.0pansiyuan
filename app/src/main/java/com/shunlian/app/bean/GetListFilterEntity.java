package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetListFilterEntity {
    public List<Recommend> recommend_brand_list;
    public List<String> first_letter_list;
    public List<Brand> brand_list;
    public List<Attr> attr_list;

    @Override
    public String toString() {
        return "GetListFilterEntity{" +
                "recommend_brand_list=" + recommend_brand_list +
                ", first_letter_list=" + first_letter_list +
                ", brand_list=" + brand_list +
                ", attr_list=" + attr_list +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recommend{
        public String id;
        public String brand_name;

        @Override
        public String toString() {
            return "Recommend{" +
                    "id='" + id + '\'' +
                    ", brand_name='" + brand_name + '\'' +
                    '}';
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Brand{
        public String first_letter;
        public List<Item> item_list;

        @Override
        public String toString() {
            return "Brand{" +
                    "first_letter='" + first_letter + '\'' +
                    ", item_list=" + item_list +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Item{
            public String id;
            public String brand_name;
            public String first_letter;
            public String spell;

            @Override
            public String toString() {
                return "Item{" +
                        "id='" + id + '\'' +
                        ", brand_name='" + brand_name + '\'' +
                        ", first_letter='" + first_letter + '\'' +
                        ", spell='" + spell + '\'' +
                        '}';
            }
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attr{
        public String name;
        public List<String> val_list;

        @Override
        public String toString() {
            return "Attr{" +
                    "name='" + name + '\'' +
                    ", val_list=" + val_list +
                    '}';
        }
    }
}
