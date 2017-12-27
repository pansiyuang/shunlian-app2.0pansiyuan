package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/12/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageEntity {

    public String imgPath;
    public String imgUrl;
    public int progress;

    public ImageEntity(String path) {
        this.imgPath = path;
    }
}
