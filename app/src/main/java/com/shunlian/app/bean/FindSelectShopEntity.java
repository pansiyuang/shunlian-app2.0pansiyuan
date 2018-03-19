package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindSelectShopEntity {

    public List<StoreList> store_list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StoreList{
        public String id;
        public String name;
        public String logo;
        public String desc;
        public boolean isSelect;
    }
}
