package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditLogEntity {
    public List<CreditLog> list;
    public int count;
    public int page;
    public int total_page;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreditLog {
        public String create_time;
        public String num;
        public String use_road;
    }
}
