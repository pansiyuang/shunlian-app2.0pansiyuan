package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtTagEntity {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public List<Item> list;

    public static class Item {
        public String id;
        public String name;
        public String create_time;
        public String sort;
        public boolean isSelect;
    }

}
