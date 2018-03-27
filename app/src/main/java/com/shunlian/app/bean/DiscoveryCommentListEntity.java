package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoveryCommentListEntity {
    public Mdata list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Mdata {
        public InvInfo inv_info;
        public List<Commentlist> commentlist;
        public String total_page;
        public String page_size;
        public String page;
        public String commentcounts;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class InvInfo {
            public String content;
            public List<String> img;
            public List<String> five_member_likes;
            public String is_likes;
            public String create_time;
            public String member_id;
            public String likes;
            public AuthorInfo author_info;
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class AuthorInfo {
                public String nickname;
                public String avatar;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Commentlist {
            public String id;
            public String content;
            public String likes;
            public String member_id;
            public String add_time;
            public String nickname;
            public String level;
            public String is_likes;
            public String avatar;
        }
    }
}
