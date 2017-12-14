package com.shunlian.app.bean;

/**
 * Created by Administrator on 2017/12/13.
 * 发布评论实体类
 */

public class ReleaseCommentEntity {

    public String order;
    public String pic;
    public String title;
    public String price;
    public String goodsId;
    public String comment_id;

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
