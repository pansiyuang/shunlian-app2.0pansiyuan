package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatGoodsEntity {
    public String count;
    public String total_page;
    public String page;
    public String page_size;
    public List<Goods> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods implements Serializable {
        public String id;
        public String goods_id;
        public String price;
        public String title;
        public String thumb;
        public String from;
        public boolean isSelect;
    }
}
