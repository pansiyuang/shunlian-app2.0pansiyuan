package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhanghe on 2018/10/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicEntity {


    public PagerEntity pager;
    public List<ItemBean> list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemBean {
        public String id;
        public String title;
        public String content;
        public String thumb;
        public String count;
        public String refer_member_num;
        public String refer_num;
        public String add_time;
    }

}
