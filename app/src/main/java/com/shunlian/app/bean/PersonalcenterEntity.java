package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalcenterEntity {
    public String nickname;
    public String avatar;
    public String level;
    public String role;
    public String member_role;
    public String balance;
    public String coupon_num;
    public String all_sl_income;
    public String team_sales;
    public String order_un_pay_num;
    public String order_un_receive_num;
    public String order_un_send_num;
    public String un_comment_num;
    public String new_refund_num;
    public String goods_fav_num;
    public String store_fav_num;
    public String article_fav_num;
    public String footermark_fav_num;
    public String team_member_num;
    public String team_order_num;
    public String next_level_score;
    public String next_level_percent;
    public String next_level_info;
    public String my_rank_label;
    public String my_rank_info;
    public String my_rank_code;
    public String invite_code;
    public String son_manage_url;
    public String son_order_url;
    public String plus_role;
    public List<User> sl_user_ranks;
    public List<HelpcenterIndexEntity.ArticleCategory> article;


    @Override
    public String toString() {
        return "PersonalcenterEntity{" +
                "nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", level='" + level + '\'' +
                ", member_role='" + member_role + '\'' +
                ", balance='" + balance + '\'' +
                ", coupon_num='" + coupon_num + '\'' +
                ", all_sl_income='" + all_sl_income + '\'' +
                ", team_sales='" + team_sales + '\'' +
                ", order_un_pay_num='" + order_un_pay_num + '\'' +
                ", order_un_receive_num='" + order_un_receive_num + '\'' +
                ", order_un_send_num='" + order_un_send_num + '\'' +
                ", un_comment_num='" + un_comment_num + '\'' +
                ", new_refund_num='" + new_refund_num + '\'' +
                ", goods_fav_num='" + goods_fav_num + '\'' +
                ", store_fav_num='" + store_fav_num + '\'' +
                ", article_fav_num='" + article_fav_num + '\'' +
                ", footermark_fav_num='" + footermark_fav_num + '\'' +
                ", team_member_num='" + team_member_num + '\'' +
                ", team_order_num='" + team_order_num + '\'' +
                ", my_rank_info='" + my_rank_info + '\'' +
                ", my_rank_code='" + my_rank_code + '\'' +
                ", sl_user_ranks=" + sl_user_ranks +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        public String nickname;
        public String avatar;
        public String sale;
        public String is_mine;

        @Override
        public String toString() {
            return "User{" +
                    "nickname='" + nickname + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", number='" + sale + '\'' +
                    '}';
        }
    }
}
