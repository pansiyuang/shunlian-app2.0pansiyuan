package com.shunlian.app.shunlianyoupin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2019/4/4.
 */

public class YouPingListEntity {


        public pages pager;
        public List<lists> list;
        public String agent;
        public String client_type;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class pages{
            public String page;
            public String page_size;
            public String count;
            public String total_page;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class lists{
            public String product_id;
            public String title;
            public String thumb;
            public String price;
            public String pic;
            public String market_price;
            public String stock;
            public String sell_out;
            public String hot;
            @JsonProperty(value = "new")//关键字重名
            public String mNew;
            public String characteristic;
            public String create_time;
        }


    }

