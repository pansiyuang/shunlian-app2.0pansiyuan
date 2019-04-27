package com.shunlian.app.yjfk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2019/4/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplaintTypesEntity {

    public List<Title> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Title {
        public String title;
        public List<Typess> types;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Typess {
            public String id;
            public String desc;
        }
    }
}
