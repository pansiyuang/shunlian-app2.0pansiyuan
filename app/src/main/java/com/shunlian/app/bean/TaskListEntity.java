package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhanghe on 2018/8/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskListEntity {

    public List<ItemTask> new_user_tasks;
    public List<ItemTask> daily_tasks;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemTask {

        public String code;
        public String title;
        public String info;
        public String gold_num;
        public String task_status;//0 未完成；1已完成
        public String icon_url;
        public String share_pic_url;
        public String video_url;
        public Url url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Url{
        public String type;
        public String item_id;
    }
}
