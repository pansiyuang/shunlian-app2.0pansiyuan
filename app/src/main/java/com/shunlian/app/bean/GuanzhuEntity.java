package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GuanzhuEntity {

    public String count;
    public String total_page;
    public String page;
    public String page_size;
    public List<DynamicListBean> dynamic_list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DynamicListBean {
        public String store_id;
        public String store_logo;
        public String store_name;
        public String has_follow;
        public String has_like;
        public String is_recommend;
        public String id;
        public String title;
        public String full_title;
        public String thumb;
        public String likes;
        public String comments;
        public String share_url;
        public String add_time;
        public String forwards;
        public String type;
        public List<TagsBean> tags;
        public List<TagsBean> goods_list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TagsBean {
        public String id;
        public String name;
        public String thumb;
    }
}
