package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhanghe on 2018/10/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NearAddrEntity {

    public List<NearAddr> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NearAddr {
        public String name;
        public String addr;
    }
}
