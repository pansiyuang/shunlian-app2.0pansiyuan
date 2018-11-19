package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagEntity {
    public List<String> keyword;
    public List<Tag> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Tag {
        public String id;
        public String key_word;
        public String hot;
    }
}
