package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpcenterIndexEntity {
    public List<QuestionCategory> questionCategory;
    public List<QuestionCommon> questionCommon;
    public List<ArticleCategory> articleCategory;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QuestionCategory {
        public String id;
        public String name;
        public String keyword;
        public String icon;
        public String direct;
        public String is_show;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QuestionCommon {
        public String id;
        public String title;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ArticleCategory {
        public String id;
        public String title;
        public String image;
    }
}
