package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhanghe on 2018/8/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskHomeEntity {

    public String gold_egg;

    public String gold_egg_second;
    public String gold_egg_total_second;

    public String sign_continue_num;

    public String ad_pic_url;

    public AdUrlBean ad_url;

    public String faq_url;
    public String task_status;//0表示当前可以领取  1表示当前不能领取

    public String rule_url;
    public String got_eggs;
    public String account_eggs;

    public List<SignDaysBean> sign_days;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdUrlBean {
        public String type;
        public String item_id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SignDaysBean {

        public String date;
        public String sign_status;
        public String gold_num;

    }
}
