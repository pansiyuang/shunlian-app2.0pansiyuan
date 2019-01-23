package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhanghe on 2019/1/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentRejectedEntity {

    public List<RejectedList> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RejectedList {
        public String id;
        public String value;
        public String admin_id;
        public String create_time;
    }
}
