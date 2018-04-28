package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/4/13.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsMessage extends BaseMessage {
    public GoodsBody msg_body ;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsBody {
        public Goods goods ;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods {
        public String goodsImage;
        public String title;
        public String price;
        public String goodsId;
    }
}
