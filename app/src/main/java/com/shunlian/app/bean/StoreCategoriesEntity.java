package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreCategoriesEntity {
    public List<MData> data;

    @Override
    public String toString() {
        return "StoreCategoriesEntity{" +
                "data=" + data +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String id;
        public String name;
        public String url;
        public List<Children> children;

        @Override
        public String toString() {
            return "MData{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", children=" + children +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Children {
            public String id;
            public String name;
            public String url;
            public String parent_id;

            @Override
            public String toString() {
                return "Children{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", url='" + url + '\'' +
                        ", parent_id='" + parent_id + '\'' +
                        '}';
            }
        }
    }
}
