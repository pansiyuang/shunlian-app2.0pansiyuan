package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotsaleEntity {
    public List<Suspension> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Suspension  {
        public String cate_id;
        public String cate_name;
        public List<Link> list;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Link{
            public String id;
            public String title;
            public String thumb;
            public String praise;
            public String star_rate;
        }
    }
}
