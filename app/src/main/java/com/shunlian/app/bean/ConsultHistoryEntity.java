package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultHistoryEntity {

    public List<HistoryList> history_list;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HistoryList{

        public String user_id;
        public String user_type;
        public String status;
        public String time;
        public String progress;
        public String username;
        public String user_thumb;
        public String status_msg;
        public List<String> images;
        public List<Content> content;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content{
        public String label;
        public String title;
        public String description;
    }
}
