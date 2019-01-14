package com.shunlian.app.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoldEggPrizeEntity {
    public List<Prize> list;
    public String rule_url;
    public int consume;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Prize {
        public String id;
        public String name;
        public String image;
        public int type;
    }
}
