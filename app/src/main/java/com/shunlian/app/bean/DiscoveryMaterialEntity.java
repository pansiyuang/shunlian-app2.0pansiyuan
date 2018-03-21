package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoveryMaterialEntity {
    public List<Content> list;
    public String count;
    public String total_page;
    public String page_size;
    public String page;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        public String id;
        public String content;
        public String praise_num;
        public String share_num;
        public String admin_id;
        public String praise;
        public String add_time;
        public List<String> image;
    }

}
