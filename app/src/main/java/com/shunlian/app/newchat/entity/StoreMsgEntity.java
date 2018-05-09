package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreMsgEntity {
    public String read_del_type;
    public int page;
    public int page_size;
    public int total;
    public int max_page;
    public List<StoreMsg> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StoreMsg {
        public int type;
        public String url;
        public String msg_id;
        public int is_read;
        public String article_id;//是否已读 0 未读  1 已读
        public Content content;
        public int is_del;  //是否可删除 0 否 1可以
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        public String title;
        public String thumb;
        public String username;
        public String sl_id;
        public String create_time;
    }
}
