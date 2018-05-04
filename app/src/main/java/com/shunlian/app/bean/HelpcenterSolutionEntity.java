package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpcenterSolutionEntity {
    public String id;
    public String question;
    public String answer;
    public String share_url;
    public List<Question> about;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Question {
        public String id;
        public String title;
    }

}
