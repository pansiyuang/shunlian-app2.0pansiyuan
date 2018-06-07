package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangDetailEntity {

    public String count;
    public String page;
    public String total_page;
    public String page_size;
    public ExperienceInfo experience_info;
    public List<FindCommentListEntity.ItemComment> comment_list;
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExperienceInfo{
        public String id;
        public String content;
        public String goods_id;
        public String praise_num;
        public String comment_num;
        public String check_comment;
        public String status;
        public String remark;
        public MemberInfo member_info;
        public String had_like;
        public String add_time;
        public GoodsBean goods;
        public List<String> image;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsBean {
        public String id;
        public String thumb;
        public String title;
        public String price;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MemberInfo {
        public String member_id;
        public String nickname;
        public String avatar;
        public String level;
    }
}
