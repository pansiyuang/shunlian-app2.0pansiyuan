package com.shunlian.app.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaturdayDrawRecordEntity {
    public List<SaturdayDrawRecord> list;
    public int count;
    public int page;
    public int total_page;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SaturdayDrawRecord {
        public String create_time;
        public String trophy_name;
        public String express_sn;
    }
}
