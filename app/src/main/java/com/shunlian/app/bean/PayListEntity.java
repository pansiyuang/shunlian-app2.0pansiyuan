package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayListEntity {

    public List<PayTypes> pay_method;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayTypes{
        public String code;
        public String name;
        public String pic;
    }
}
