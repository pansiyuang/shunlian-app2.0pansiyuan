package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpcenterQuestionEntity {
    public List<Question> list;
    public String count;
    public String total_page;
    public String page;
    public String page_size;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Question {
        public String id;
        public String name;
        public String icon;
        public String title;
    }

}
