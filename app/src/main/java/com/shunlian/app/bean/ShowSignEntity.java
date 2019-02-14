package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowSignEntity {
    public String is_show;
    public String pic;
    public AD url;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AD {
        public String type;
        public String item_id;
    }
}
