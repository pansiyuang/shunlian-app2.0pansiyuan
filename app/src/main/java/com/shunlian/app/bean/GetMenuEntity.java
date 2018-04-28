package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetMenuEntity {
    public Logo logo;
    @JsonProperty(value = "data")//关键字重名
    public List<MData> datas;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Logo {
        public String bg_pic;
        public String type;
        public String item_id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String id;
        public String channel_name;
        public String type;

    }
}
