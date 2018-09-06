package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/8/31.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TurnTableEntity {
    public List<String> prizeScroll;
    public TurnTable turnTable;
    public List<MyPrize> myPrize;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TurnTable {
        public String title;
        public List<Trophy> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Trophy {
        public String id;
        public int trophy_type;
        public String trophy_id;
        public String trophy_name;
        public String thumb;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MyPrize {
        public String create_time;
        public String trophy_name;
        public String express_sn;
    }
}
