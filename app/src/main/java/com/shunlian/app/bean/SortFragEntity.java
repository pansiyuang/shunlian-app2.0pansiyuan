package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/1/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SortFragEntity {

    public List<Toplist> category_list;

    public List<SubList> sub_list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Toplist{
        public String id;
        public String name;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubList{
        public String id;
        public String name;
        public String has_top_page;
        public List<ItemList> item_list;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemList{
        public String id;
        public String name;
        public String thumb;
    }
}
