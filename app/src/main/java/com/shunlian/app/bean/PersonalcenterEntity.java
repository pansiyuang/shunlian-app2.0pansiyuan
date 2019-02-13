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
    public String member_role;
    public String balance;
    public String coupon_num;
    public String all_sl_income;
    public String estimateProfit;
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
    public String salesInfo_url;
    public String next_level_score;
    public String next_level_percent;
    public String next_level_info;
    public String my_rank_label;
    public String my_rank_info;
    public String my_rank_code;
    public String invite_code;
    public String note;
    public String son_manage_url;
    public String son_order_url;
    public List<Game> game_door;
    public String plus_role;
    public String bcm_role;
    public String grow_num;
    public String plus_meg;
    public String diff;
    public String diff_meg;
    public String zhifu;
    public String plus_expire_time;
    public String notice;
    public MyAssets myAssets;
    public MyService myService;
    public OursSchool oursSchool;
    public List<User> sl_user_ranks;
    public List<HelpcenterIndexEntity.ArticleCategory> article;
    public Credit credit;

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
    public static class Credit {
        public String title;
        public String value;
        public Url url;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Url {
            public String type;
            public String item_id;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OursSchool {
        public String title;
        public String item_type;
        public List<Item> items;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Item {
            public String title;
            public String img;
            public String id;
            public String update_title;
            public String update_point;
            public Url url;
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Url {
                public String type;
                public String item_id;
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MyService {
        public String title;
        public String item_type;
        public List<Item> items;
        public List<Banner> banner_list;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Banner {
            public String img;
            public Link link;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Link {
                public String type;
                public String item_id;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Item {
            public String title;
            public String img;
            public String update_point;
            public String update_title;
            public Url url;
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Content {
                public String text;
                public String color;
            }
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Url {
                public String type;
                public String item_id;
            }
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MyAssets {
        public String title;
        public String item_type;
        public Balance balance;
        public List<Item> items;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Balance {
            public String title;
            public Url url;
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Url {
                public String type;
                public String item_id;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Item {
            public String title;
            public String update_title;
            public String update_point;
            public Content content;
            public Url url;
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Content {
                public String text;
                public String color;
            }
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Url {
                public String type;
                public String item_id;
            }
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Game {
        public String thumb;
        public Url url;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Url {
            public String type;
            public String item_id;
            public String msg;
            public String button;
        }
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
