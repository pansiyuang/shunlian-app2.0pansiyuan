package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotBlogsEntity {
    public Pager pager;
    public List<BigImgEntity.Blog> list;
    public List<Ad> ad_list;
    public int is_have_focus;
    public List<String> expert_list;
    public List<RecomandFocus> recomand_focus_list;
    public DiscoveryInfo discovery_info;
    public MemberInfo member_info;
    public Detail detail;
    public BaseInfo base_info;
    public int unread;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public int page;
        public int page_size;
        public int count;
        public int total_page;
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
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecomandFocus {
        public String member_id;
        public String avatar;
        public String nickname;
        public String signature;
        public String follow_num;
        public String blog_num;
        public int expert;
        public int add_v;
        public String expert_icon;
        public String v_icon;
        public int focus_status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DiscoveryInfo {
        public String fans_num;
        public String praise_num;
        public String down_num;
        public String focus_num;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MemberInfo {
        public String member_id;
        public String avatar;
        public String nickname;
        public String signature;
        public int is_focus;
        public int is_fans;
        public int expert;
        public int add_v;
        public String expert_icon;
        public String v_icon;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {
        public String id;
        public String title;
        public String content;
        public String thumb;
        public String author_name;
        public String author_user;
        public String count;
        public int status;
        public String refer_member_num;
        public String refer_num;
        public String add_time;
        public String update_time;
        public List<DiscoverActivityEntity.Member> members;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BaseInfo {
        public String member_id;
        public int white_list;
        public String avatar;
        public String nickname;
        public String follow_num;
        public String blog_num;
        public int expert;
        public int is_inner;
        public int add_v;
        public String expert_icon;
        public String v_icon;
    }
}
