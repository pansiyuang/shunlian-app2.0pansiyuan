package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogisticsNameEntity {

    public List<String> first_letter_list;

    public List<LogisticsName> express_list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LogisticsName{
        public String first_letter;
        public List<String> item_list;
    }
}
