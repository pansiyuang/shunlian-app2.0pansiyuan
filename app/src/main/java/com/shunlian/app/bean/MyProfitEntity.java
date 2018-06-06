package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyProfitEntity {

    public UserInfo user_info;
    public ProfitInfo profit_info;
    public String tip;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo{
        public String avatar;
        public String nickname;
        public String grow_num;
        public String invite_code;
        public String member_role;
        public String member_role_code;
        public String plus_role_code;
        public String plus_role;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProfitInfo{
        public String week_reward;
        public String month_reward;
        public String estimate_profit;
        public String today_profit;
        public String month_profit;
        public String order_profit;
        public String meritocrat_profit;
        public String available_profit;
        public String withdrawed_profit;
        public String profit_help_url;

    }

}
