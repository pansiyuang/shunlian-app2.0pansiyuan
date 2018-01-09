package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RankingListEntity {

    public List<Category> category;

    public Goods goods;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Category{
        public String id;
        public String name;
        public String thumb;
        public String g_cid;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods{

        public String count;
        public String page;
        public String page_size;
        public String total_page;
        public List<GoodsDeatilEntity.Goods> goods_list;
    }

}
