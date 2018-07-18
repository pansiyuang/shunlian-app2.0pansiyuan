package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhanghe on 2018/7/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditPhoneListEntity {
    public List<ListBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListBean {

        /**
         * id : 6
         * addr : 浙江移动
         * number : 13588151397
         */
        public String id;
        public String addr;
        public String number;
    }
}
