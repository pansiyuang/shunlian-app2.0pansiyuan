package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoveryCircleEntity {
    public Mdata list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Mdata {
        public List<Banner> banner;
        public List<Content> list;
        public String total_page;
        public String page_size;
        public String page;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Banner {
            public String id;
            public String img;
            public String title;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Content {
            public String id;
            public String img;
            public String title;
            public String likes;
            public String comments;
            public String is_likes;
        }
    }
}
