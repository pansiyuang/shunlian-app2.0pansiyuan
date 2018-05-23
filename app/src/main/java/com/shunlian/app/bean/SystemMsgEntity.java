package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemMsgEntity {
    public String page;
    public String page_size;
    public String total;
    public String total_page;
    public List<MsgType> list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MsgType {
        public String type;
        public String jump;
        public String member_id;
        public String is_read;
        public String is_delete;
        public String create_time;
        public String id;
        public ContentBean body;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentBean {
        public String id;
        public String title;
        public String thumb;
        public String order_id;
        public String content;
        public String remark;
        public String targetId;
        public String url;
        public String target;
        public String time;
        public String money;
        public String expire;
        public String condition;
        public String opt;
    }
}
