package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmountDetailEntity {
    public List<Content> list;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        public String name;
        public String value;
    }

}
