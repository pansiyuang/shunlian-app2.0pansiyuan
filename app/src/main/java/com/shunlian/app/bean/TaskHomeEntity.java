package com.shunlian.app.bean;

import com.airbnb.lottie.L;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhanghe on 2018/8/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskHomeEntity {

    public String gold_egg;
    public String miss_eggs;
    public String is_remind;

    public String gold_egg_second;
    public String gold_egg_total_second;

    public String sign_continue_num;

    public String ad_pic_url;

    public AdUrlBean ad_url;

    public List<AdUrlRollBean> ad_roll;

    public String faq_url;
    public String task_status;//0表示当前可以领取  1表示当前不能领取

    public String rule_url;
    public String got_eggs;
    public String account_eggs;

    public List<SignDaysBean> sign_days;
    public List<GoldEgg> get_gold_egg;
    public String share_pic_url;
    public String pop_ad_pic_url;
    public AdUrlBean pop_ad_url;

    @Override
    public String toString() {
        return "TaskHomeEntity{" +
                "gold_egg='" + gold_egg + '\'' +
                ", gold_egg_second='" + gold_egg_second + '\'' +
                ", gold_egg_total_second='" + gold_egg_total_second + '\'' +
                ", sign_continue_num='" + sign_continue_num + '\'' +
                ", ad_pic_url='" + ad_pic_url + '\'' +
                ", ad_url=" + ad_url +
                ", faq_url='" + faq_url + '\'' +
                ", task_status='" + task_status + '\'' +
                ", rule_url='" + rule_url + '\'' +
                ", got_eggs='" + got_eggs + '\'' +
                ", account_eggs='" + account_eggs + '\'' +
                ", sign_days=" + sign_days +
                ", share_pic_url='" + share_pic_url + '\'' +
                '}';
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdUrlBean {
        public String type;
        public String item_id;

        @Override
        public String toString() {
            return "AdUrlBean{" +
                    "type='" + type + '\'' +
                    ", item_id='" + item_id + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoldEgg {
        public String title;
        public String icon_url;
        public String content;
        public String status;
        public String is_remind;
        public String all_task;
        public String over_task;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SignDaysBean {
        public String date;
        public String sign_status;
        public String gold_num;

        @Override
        public String toString() {
            return "SignDaysBean{" +
                    "date='" + date + '\'' +
                    ", sign_status='" + sign_status + '\'' +
                    ", gold_num='" + gold_num + '\'' +
                    '}';
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdUrlRollBean {
        public AdUrlBean ad_url;
        public String ad_pic_url; //没有字段或空字符串，不显示

        @Override
        public String toString() {
            return ad_url+"ad_url"+"/ad_pic_url"+ad_pic_url;
        }
    }
}
