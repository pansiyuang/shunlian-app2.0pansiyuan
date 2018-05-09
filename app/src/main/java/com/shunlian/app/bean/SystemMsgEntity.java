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
    public List<MsgType> list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MsgType {
        public String type;
        public String url;
        public String msg_id;
        public String is_read;
        public String is_del;
        public ContentBean content;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentBean {
        public String create_time;
        public String title;
        public String active;
        public String thumb;
        public String content;
        public String time;
        public String money;
        public String condition;
        public String explain;
        public String expire;
        public String remark;
    }
}
