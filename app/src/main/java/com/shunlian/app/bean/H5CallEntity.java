package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class H5CallEntity {
    public String istates;
    public String type;
    public String scene;
    public String title;
    public String description;
    public String thumb;
    public String contentUrl;
    public String origin;

    @Override
    public String toString() {
        return "H5CallEntity{" +
                "istates='" + istates + '\'' +
                ", type='" + type + '\'' +
                ", origin='" + origin + '\'' +
                ", scene='" + scene + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumb='" + thumb + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                '}';
    }
}
