package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghe on 2018/10/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogDraftEntity {
    
    public String id;
    public String type;
    public String text;
    public ArrayList<String> pics;
    public String video;
    public String video_thumb;
    public String activity_id;
    public String activity_title;
    public String lng;
    public String lat;
    public String place;
    public String related_goods;
    public String status;
    public String create_time;
    public List<GoodsDeatilEntity.Goods> goods_infos;
}
