package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/9/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageMessage extends BaseMessage {

    public ImageBody msg_body;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageBody{
        public String img_small;
        public String img_original;
        public int img_height;
        public int img_width;
        public String img_host;
        public String localUrl;

        @Override
        public String toString() {
            return "ImageBody{" +
                    "img_small='" + img_small + '\'' +
                    ", img_original='" + img_original + '\'' +
                    ", img_height=" + img_height +
                    ", img_width=" + img_width +
                    ", img_host='" + img_host + '\'' +
                    ", localUrl='" + localUrl + '\'' +
                    '}';
        }
    }
}
