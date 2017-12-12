package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentListEntity {

    public List<Label> label;
    public ListData list;

    @Override
    public String toString() {
        return "CommentListEntity{" +
                "label=" + label +
                ", list=" + list +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Label{
        public String name;
        public String type;
        public String count;

        @Override
        public String toString() {
            return "Label{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", count='" + count + '\'' +
                    '}';
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListData{
        public String page;
        public String allPage;
        public String total;
        public String pageSize;

        public List<Data> data;

        @Override
        public String toString() {
            return "ListData{" +
                    "page='" + page + '\'' +
                    ", allPage='" + allPage + '\'' +
                    ", total='" + total + '\'' +
                    ", pageSize='" + pageSize + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data implements Serializable{
        public String comment_id;
        public String star_level;
        public String praise_total;
        public String goods_option;
        public String content;
        public String append;
        public String reply;
        public String reply_time;
        public String status;
        public String avatar;
        public String nickname;
        public String level;
        public String member_role;
        public String add_time;
        public String append_time;
        public String buy_time;
        public boolean is_praise;
        public List<String> pics;
        public List<String> append_pics;

        public String is_change;//是否可改为好评  0不能改好评    1可以改好评
        public String is_append;//追评状态  0不能追评   1可以追评 2已经追评
        public String append_note;//追评于多长时间前
        public String thumb;//商品封面图
        public String title;//商品名称
        public String price;//商品价格

        @Override
        public String toString() {
            return "Data{" +
                    "comment_id='" + comment_id + '\'' +
                    ", star_level='" + star_level + '\'' +
                    ", praise_total='" + praise_total + '\'' +
                    ", goods_option='" + goods_option + '\'' +
                    ", content='" + content + '\'' +
                    ", append='" + append + '\'' +
                    ", reply='" + reply + '\'' +
                    ", reply_time='" + reply_time + '\'' +
                    ", status='" + status + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", level='" + level + '\'' +
                    ", member_role='" + member_role + '\'' +
                    ", add_time='" + add_time + '\'' +
                    ", is_praise='" + is_praise + '\'' +
                    ", pics=" + pics +
                    ", append_pics=" + append_pics +
                    '}';
        }
    }
}
