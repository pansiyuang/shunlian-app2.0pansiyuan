package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetRealInfoEntity {
    public String account_name;
    public String account_number;
    public String account_type;
    public String mobile;
    public String memo;
    public List<Content> limit_memo;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        public String name;
        public String value;
    }

}
