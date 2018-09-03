package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignEggEntity {
    public String gold_num;
    public String gold_egg;//总金蛋数
    public String sign_continue_num;//总签到天数
    public String ad_pic_url;
    public Url url;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Url {
        public String type;
        public String item_id;
    }
}

