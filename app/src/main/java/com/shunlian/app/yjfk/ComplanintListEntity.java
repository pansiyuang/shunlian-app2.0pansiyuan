package com.shunlian.app.yjfk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * id": "1",
 * "cate_id": "2",//类型id，暂无意义
 * "cate_name": "提供方案不合理", //投诉类型名称
 * "create_time": "2019-04-02 15:54:26",
 * "is_success": true//是否投诉成功，
 * "status_desc": '投诉成功'//状态描述，
 * }
 * ],
 * "page": 1,
 * "allPage": 1,
 * "total": "1",
 * "pageSize": 20
 * Created by Administrator on 2019/4/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplanintListEntity {
    public List<Lists> list;
    public  String page;
    public  String allPage;
    public  String total;
    public  String pageSize;
    @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Lists{
           public String id;
            public String cate_id;
            public String cate_name;
            public String create_time;
            public boolean is_success;
            public String status_desc;
        }



}
