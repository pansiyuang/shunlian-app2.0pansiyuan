package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/10/12.
 */

public class LinkMessage extends BaseMessage {

    public LinkBody msg_body;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LinkBody {
        public String goodsImage;
        public String title;
        public String price;
        public String goodsId;

        @Override
        public String toString() {
            return "link_body{" +
                    "goodsImage='" + goodsImage + '\'' +
                    ", title='" + title + '\'' +
                    ", price=" + price +
                    ", goodsId=" + goodsId +
                    "}";
        }
    }
}
