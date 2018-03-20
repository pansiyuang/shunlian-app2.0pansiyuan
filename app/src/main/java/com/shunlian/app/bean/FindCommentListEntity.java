package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindCommentListEntity {

    public String count;
    public String total_page;
    public String page;
    public String page_size;
    public List<ItemComment> top_list;
    public List<ItemComment> comment_list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemComment{
        public String avatar;
        public String nickname;
        public String level;
        public String content;
        public String likes;
        public String had_like;
        public String add_time;
        public String has_more_reply;
        public String reply_count;
        public String at;
        public String item_id;
        public String delete_enable;//是否能删除，1是，0否
        public List<ReplyList> reply_list;
        public List<LastLikesBean> last_likes;

        @Override
        public String toString() {
            return "ItemComment{" +
                    "avatar='" + avatar + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", level='" + level + '\'' +
                    ", content='" + content + '\'' +
                    ", likes='" + likes + '\'' +
                    ", had_like='" + had_like + '\'' +
                    ", add_time='" + add_time + '\'' +
                    ", has_more_reply='" + has_more_reply + '\'' +
                    ", reply_count='" + reply_count + '\'' +
                    ", delete_enable='" + delete_enable + '\'' +
                    ", reply_list=" + reply_list +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReplyList{
        public String reply_id;
        public String reply_avatar;
        public String reply_by;
        public String reply;
        public String reply_time;
        public String at;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LastLikesBean {
        public String avatar;
    }
}
