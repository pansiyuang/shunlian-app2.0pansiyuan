package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;

/**
 * Created by Administrator on 2017/12/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageEntity {

    public String imgPath;
    public String imgUrl;
    public File file;
    public int progress;

    public ImageEntity(String path) {
        this.imgPath = path;
    }

    public ImageEntity() {
    }
}
