package com.shunlian.app.shunlianyoupin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2019/4/3.
// */

//        "message": "",
//        "data": {
//        "pager": {
//        "page": "1",
//        "page_size": "5",
//        "count": "5",
//        "total_page": "1"
//        },
//        "list": [
//        {
//        "id": "1",
//        "pic_url": "http://v20-img.shunliandongli.com/abc.jpg",//图片地址
//        "link": "www.baidu.com",//链接地址
//        "start_time": "2018-05-22 11:10:48",//定时开始时间
//        "end_time": "2018-06-26 04:30:48",//定时结束时间
//        "sort": "1"//排序
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouPingbannerEntity {

        public pages pager;
        public List<banner> list;
        public String agent;
        public String client_type;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class pages{
            public String page;
            public String page_size;
            public String count;
            public String total_page;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class banner{
            public String id;
            public String pic_url;
            public String link;
            public String start_time;
            public String end_time;
            public String sort;
            public jump jump_link;

        }
         @JsonIgnoreProperties(ignoreUnknown = true)
        public static class jump{
            public String type;
            public String item_id;
        }
    }



