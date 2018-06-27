package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoveryNavEntity {
    public List<Flash> flash_list;
    public List<Nav> nav_list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Flash {
        public String id;
        public String title;
        public String full_title;
        public String thumb;

        @Override
        public String toString() {
            return "Flash{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", full_title='" + full_title + '\'' +
                    ", thumb='" + thumb + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Nav {
        public String code;
        public String name;
    }
}
