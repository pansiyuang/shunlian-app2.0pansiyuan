package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/1/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SortFragEntity {

    public List<Toplist> categoryList;



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Toplist{
        public String id;
        public String name;
        public String thumb;
        public String pid;
        public String level;
        public String on_ranking;
        public List<SubList> children;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubList{
        public String id;
        public String name;
        public String thumb;
        public String pid;
        public String level;
        public String on_ranking;
        public List<ItemList> children;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemList{
        public String id;
        public String name;
        public String thumb;
        public String op_cid;
        public String g_cid;
        public String is_attr;
        public List<Attrs> attrs;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attrs{

        public String attr_name;
        public List<String> attr_vals;
    }
}
