package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/6/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanCodeEntity {

    public String share_code;
    public Url url;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Url{
        public String type;
        public List<String> item_id_list;
    }
}
