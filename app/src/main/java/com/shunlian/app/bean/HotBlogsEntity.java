package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotBlogsEntity {
    public Pager pager;
    public List<Blog> list;
    public List<Ad> ad_list;
    public List<String> expert_list;
    public List<RecomandFocus> recomand_focus_list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public int page;
        public int page_size;
        public int count;
        public int total_page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Blog {
        public String avatar;
        public String nickname;
        public int add_v;
        public String id;
        public String member_id;
        public int type;
        public String text;
        public List<String> pics;
        public String video;
        public String video_thumb;
        public String activity_id;
        public String activity_title;
        public String place;
        public List<GoodsDeatilEntity.Goods> related_goods;
        public String praise_num;
        public String down_num;
        public String share_num;
        public int is_focus;
        public String time_desc;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ad {
        public String id;
        public String ad_img;
        public AdLink ad_link;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdLink {
        public String type;
        public String item_id;
        public String item_type;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecomandFocus {
        public String member_id;
        public String avatar;
        public String nickname;
        public String signature;
        public String follow_num;
        public String blog_num;
        public int focus_status;
    }
}
