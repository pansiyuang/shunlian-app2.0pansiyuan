package com.shunlian.app.ui.new3_login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zhanghe on 2018/11/20.
 * 登录界面提示信息
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class New3LoginInfoTipEntity {

    public AdBean ad;
    public String incite_code_title;
    public String incite_code_url;
    public InciteCodeRuleBean incite_code_rule;
    public String voucher;
    public String login_title;
    public V2 v2;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class V2{
        public String incite_code_title;
        public String incite_code_rule;
        public String voucher;
        public String register_title;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdBean {
        public String image;
        public LinkBean link;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LinkBean {
        public String type;
        public String item_id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InciteCodeRuleBean {
        public String title;
        public String content;
    }
}
