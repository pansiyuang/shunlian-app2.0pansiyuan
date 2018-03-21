package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperienceEntity {
    public List<Experience> list;
    public String count;
    public String page;
    public String total_page;
    public String page_size;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Experience {
        public String id;
        public String member_id;
        public String content;
        public List<String> image;
        public String goods_id;
        public String praise_num;
        public String comment_num;
        public String check_comment;
        public String status;
        public String remark;
        public String add_time;
        public GoodsDeatilEntity.Goods goods;
        public String praise;
        public MemberInfo member_info;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MemberInfo {
        public String member_id;
        public String nickname;
        public String avatar;
        public String level;
    }
}