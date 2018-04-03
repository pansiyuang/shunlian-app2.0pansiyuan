package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by JacksonGenerator on 17-9-18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgEntity extends BaseEntity {
    private Data data;

    public Data getData() {
        return data;
    }

    public ImgEntity setData(Data data) {
        this.data = data;
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String img_small;
        private String img_original;
        private int img_height;
        private int img_width;
        private String img_host;
        private String localUrl;

        public String getImg_original() {
            return img_original;
        }

        public Data setImg_original(String img_original) {
            this.img_original = img_original;
            return this;
        }

        public int getImg_height() {
            return img_height;
        }

        public Data setImg_height(int img_height) {
            this.img_height = img_height;
            return this;
        }

        public int getImg_width() {
            return img_width;
        }

        public Data setImg_width(int img_width) {
            this.img_width = img_width;
            return this;
        }

        public String getImg_host() {
            return img_host;
        }

        public Data setImg_host(String img_host) {
            this.img_host = img_host;
            return this;
        }

        public String getImg_small() {
            return img_small;
        }

        public Data setImg_small(String img_small) {
            this.img_small = img_small;
            return this;
        }

        public String getLocalUrl() {
            return localUrl;
        }

        public Data setLocalUrl(String localUrl) {
            this.localUrl = localUrl;
            return this;
        }
    }
}