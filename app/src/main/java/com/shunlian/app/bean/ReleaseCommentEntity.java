package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 * 发布评论实体类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseCommentEntity implements Serializable {

    public String order;
    public String pic;
    public String title;
    public String order_sn;
    public String price;
    public String goodsId;
    public String goods_id;
    public String thumb;
    public String comment_id;
    public String is_append;
    public String content;
    public String starLevel;
    public List<ImageEntity> imgs;
    public String picString;
    public int anonymous;

    public ReleaseCommentEntity() {
    }

    public ReleaseCommentEntity(String pic, String title, String price, String comment_id) {
        this.pic = pic;
        this.title = title;
        this.price = price;
        this.comment_id = comment_id;
    }

    public ReleaseCommentEntity(String order, String pic, String title, String price, String goodsId) {
        this.order = order;
        this.pic = pic;
        this.title = title;
        this.price = price;
        this.goodsId = goodsId;
    }
}
