package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/25.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticeMsgEntity {
    public int page;
    public int page_size;
    public int total;
    public int total_page;
    public List<Notice> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Notice {
        public int opt;
        public String content;
        public String rejected;
    }
}
