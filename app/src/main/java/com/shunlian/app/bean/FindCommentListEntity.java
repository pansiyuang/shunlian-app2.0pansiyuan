package com.shunlian.app.bean;

import com.airbnb.lottie.LottieAnimationView;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindCommentListEntity {

    public String total;
    public String comment_type;//精选评论selection、所有all
    public Pager pager;//精选评论selection、所有all
    public List<ItemComment> top_list;
    public List<ItemComment> list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemComment {
        public String id;
        public String discovery_id;
        public String member_id;
        public String reply_parent_comment_id;
        public String reply_comment_id;
        public String reply_member;
        public String avatar;
        public String nickname;
        public String content;
        public int like_count;
        public int reply_count;
        public int status;
        public String level;
        public String create_time;
        public String has_more_reply;
        public int check_is_show;
        public int is_self;
        public String like_status;
        public String at;
        public int expert;
        public String expert_icon;
        public String v_icon;
        public String plus_role;
        public String delete_enable;//是否能删除，1是，0否
        public List<ItemComment> reply_list;
        public List<LastLikesBean> last_likes;

        public int reply_status;
        public List<ItemComment> reply_result;
        public List<ItemComment> comment_list;
        public String comment_id;
        public int comment_count;

        public boolean isPlay;

        @Override
        public String toString() {
            return "ItemComment{" +
                    "avatar='" + avatar + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", level='" + level + '\'' +
                    ", content='" + content + '\'' +
                    ", likes='" + like_count + '\'' +
                    ", had_like='" + like_status + '\'' +
                    ", add_time='" + create_time + '\'' +
                    ", has_more_reply='" + has_more_reply + '\'' +
                    ", reply_count='" + reply_count + '\'' +
                    ", delete_enable='" + delete_enable + '\'' +
                    ", reply_list=" + reply_list +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReplyList {
        public String reply_id;
        public String reply_avatar;
        public String reply_by;
        public String reply;
        public String reply_time;
        public String at;

        @Override
        public String toString() {
            return "ReplyList{" +
                    "reply_id='" + reply_id + '\'' +
                    ", reply_avatar='" + reply_avatar + '\'' +
                    ", reply_by='" + reply_by + '\'' +
                    ", reply='" + reply + '\'' +
                    ", reply_time='" + reply_time + '\'' +
                    ", at='" + at + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LastLikesBean {
        public String avatar;

        @Override
        public String toString() {
            return "LastLikesBean{" +
                    "avatar='" + avatar + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public String page;
        public String allPage;
        public String total;
        public String pageSize;
    }
}
