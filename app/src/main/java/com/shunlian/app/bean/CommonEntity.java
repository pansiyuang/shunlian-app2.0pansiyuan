package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonEntity {
    public String fid;//商品收藏id
    public String message;
    public String status;
    public String share_status;
    public String share_show_status;
    public String praise_total;//总赞数
    public List<String> suggest_list; // 搜索关键字提示接口
    public List<CalendarEntity> calendar; //获取足迹日历形式信息接口
    public String num;  //可添加的商品数量接口
    public List<RefundDetailEntity.RefundDetail.Edit.Reason> reason_list;  //获取用户可选原因列表

    public String likes;  //已点赞的人数
    public List<String> five_member_likes;  //最新点赞头像列表

    public String new_likes;//点赞数量
    public List<FindCommentListEntity.LastLikesBean> last_likes;//点赞人的头像
    public List<FootprintEntity.DateInfo> date_info;//足迹时间列表
    public int reception; //客服获取工作状态 1表示工作中，0不在工作中

    public String telephone;//帮助中心电话

    public String img; //大转盘获取分享图

    //获取提现账户
    public String account_name;
    public String account_number;
    public String account_type;
    public String amount;
    public String account;
    public String rate;
    public String free_amount;
    public String free_amount_des;
    public String key;
    public String error;
    public String dialog_text;

    public String mobile;//手机号
    public String qr_img_for_app_down;//下载app
    public String push_on;//接收推送，1是，0否
    public String about_url;//关于的h5介绍页面（APP）
    public String if_pwd_set;//是否设置密码

    //IM
    public String user_id;//通过店铺Id获取客服管理的userId
    //分享
    public String title;
    public String pic;
    public String share_url;
    public String content;

    //plus入口
    public String is_open;
    public String url_index;
    public String url;
    public String is_plus;
    public String ducha_email;

    //发现未读数
    public int circle;
    public int material;
    public int experience;
    public int nice;
    public int focus;
    public int total;

    public String gold_num;//金蛋数量
    public String available_profit;//我的收益，可提现金额

    //验证新人
    public String show;
    public String prize;
    public String type;
    public String item_id;
    public String name;



    //发现未读消息统计
    public int praise_share;
    public int attention;
    public int notice;
    public int download;

    //发现评论
    public String comment_id;
    public String reply_parent_comment_id;
    public List<FindCommentListEntity.ItemComment> reply_result;
    public int reply_count;

    @Override
    public String toString() {
        return "CommonEntity{" +
                ", circle=" + circle +
                ", material=" + material +
                ", experience=" + experience +
                ", nice=" + nice +
                ", focus=" + focus +
                ", total=" + total +
                '}';
    }
}
