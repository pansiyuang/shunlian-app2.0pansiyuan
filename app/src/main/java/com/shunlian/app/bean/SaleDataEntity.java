package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/4/12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleDataEntity {

    public UserInfoBean user_info;
    public SalesInfoBean sales_info;
    public MasterInfo master_info;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfoBean {
        public String avatar;
        public String nickname;
        public String grow_num;
        public String invite_code;
        public String plus_role;
        public String plus_role_code;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SalesInfoBean {
        public String month_sales;
        public String today_members;
        public String today_orders;
        public String today_visits;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MasterInfo{
        public String total_num;
        public String continue_active;
        public String level1_active_num;
        public String level2_active_num;
    }
}
