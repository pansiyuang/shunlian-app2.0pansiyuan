package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/10/24.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberInfoEntity implements Serializable {
    public Pager pager;

    public String total_income;//总收益;

    public Followfrom follow_from;//推荐人信息，

    public String today_add_num; //今日新增 会员数

    public List<MemberList> list;//会员成员

    public String nickname; //"用户昵称",

    public String avatar; //"用户头像地址",

    public String level; //用户等级数字，0-6，其它不显示

    public String   weixin;//微信号

    public String  total_person_count;//总人数

    public String   role;//PLUS店主等级，小于等于0非plus，大于0为plus以上等级，1PLUS店主，2主管，>=3经理

    public String  invite_code;//邀请码
    public String  reg_time;//注册时间
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager implements Serializable {
        public String page;
        public String page_size;
        public String count;
        public String total_page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Followfrom implements Serializable {
        public String code;//推荐码
        public String avatar; //头像
        public String nickname;//昵称
        public String weixin;//微信
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MemberList implements Serializable {
        public String reg_time; //注册时间
        public String is_today; //是否今天新增，1是，0否
        public String nickname; //昵称
        public String avatar;// //头像
        public String son_num;//会员数
        public String total_commission; //总动力

        public String order_count;//订单数量
        public String member_id;// 用户id
        public String income;// 收入
        public String code;// 邀请码
        public String role;//角色
        public String level;//等级
        public String mobile;//手机号
        public boolean isShow = false;
    }
}
