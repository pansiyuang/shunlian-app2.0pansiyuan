package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagEntity {
    public List<String> keyword;
}
