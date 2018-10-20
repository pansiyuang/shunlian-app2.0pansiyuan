package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zhanghe on 2018/10/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagerEntity {
    public String page;
    public String page_size;
    public String count;
    public String total_page;

    @Override
    public String toString() {
        return "PagerEntity{" +
                "page='" + page + '\'' +
                ", page_size='" + page_size + '\'' +
                ", count='" + count + '\'' +
                ", total_page='" + total_page + '\'' +
                '}';
    }
}
