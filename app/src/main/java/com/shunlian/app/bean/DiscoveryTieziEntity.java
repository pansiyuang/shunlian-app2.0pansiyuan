package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoveryTieziEntity {
    public Mdata list;
    public String share_url;
    public GoodsDeatilEntity.UserInfo user_info;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Mdata {
        public TopicDetail topicDetail;
        public List<Hot> hot_inv;
        public List<Hot> new_inv;
        public String total_page;
        public String page_size;
        public String page;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TopicDetail {
            public String content;
            public String img;
            public String title;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Hot {
            public String id;
            public String content;
            public String likes;
            public String comments;
            public List<String> imgs;
            public String nickname;
            public String avatar;
            public String is_likes;
        }
    }
}
