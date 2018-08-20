package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreMsgEntity {
    public int read_del_type;
    public int page;
    public int page_size;
    public int total;
    public int total_page;
    public List<StoreMsg> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StoreMsg {
        public int type;
        public String member_id;
        public Body body;
        public String jump;
        public int is_read;//是否已读 0 未读  1 已读
        public int is_del;  //是否可删除 0 否 1可以
        public String create_time;
        public String id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        public String id;
        public String title;
        public String thumb;
        public String content;
        public int expire;
        public String comment_uid;
        public String member_id;
        public int role;
        public int anonymous;
        public String target;
        public String target_id;
        public String comment_time;
        public String nickname;
        public String avatar;
        public String money;
    }
}
