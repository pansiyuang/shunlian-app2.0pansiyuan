package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FootprintEntity {
    public List<MarkData> mark_data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MarkData{
        public String id;
        public String goods_id;
        public String title;
        public String thumb;
        public String price;
        public String view_number;
        public String create_time;
    }
}
