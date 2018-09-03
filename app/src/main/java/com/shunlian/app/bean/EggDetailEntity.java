package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EggDetailEntity {
    public List<Out> list;
    public String page;
    public String total;
    public String page_size;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Out  {
        public String date;
        public String total;
        public List<In> list;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class In {
            public String hour;
            public String handle;
            public String change_num;
        }
    }

}
