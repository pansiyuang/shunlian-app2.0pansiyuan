package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/29.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlusMemberEntity {

    public List<PlusMember> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlusMember {
        public String avatar;
        public String sentence;
    }
}
