package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/1/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotSearchEntity {
    public List<String> hot_keywords;
    public List<HotKeywords> new_hot_keywords;
    public List<String> history_list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HotKeywords {
        public String type;
        public String label;
        public int high_light;
        public String item_id;
    }
}
