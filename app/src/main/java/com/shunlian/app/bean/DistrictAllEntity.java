package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6 0006.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistrictAllEntity {
    public List<Province> district_all;

    @Override
    public String toString() {
        return "DistrictAllEntity{" +
                "district_all=" + district_all +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Province {
        public String name;
        public String id;
        public String parent_id;
        public List<City> children;

        @Override
        public String toString() {
            return "Province{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    ", parent_id='" + parent_id + '\'' +
                    ", children=" + children +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class City {
            public String name;
            public String id;
            public String parent_id;
            public List<County> children;

            @Override
            public String toString() {
                return "City{" +
                        "name='" + name + '\'' +
                        ", id='" + id + '\'' +
                        ", parent_id='" + parent_id + '\'' +
                        ", children=" + children +
                        '}';
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class County {
                public String name;
                public String id;
                public String parent_id;

                @Override
                public String toString() {
                    return "County{" +
                            "name='" + name + '\'' +
                            ", id='" + id + '\'' +
                            ", parent_id='" + parent_id + '\'' +
                            '}';
                }
            }
        }
    }
}
