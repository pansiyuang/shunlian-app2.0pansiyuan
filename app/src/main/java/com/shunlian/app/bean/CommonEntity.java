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
    public String praise_total;//总赞数
    public List<String> suggest_list; // 搜索关键字提示接口
    public List<CalendarEntity> calendar; //获取足迹日历形式信息接口
    public String num;  //可添加的商品数量接口

    public String likes;  //已点赞的人数
    public List<String> five_member_likes;  //最新点赞头像列表

    public String new_likes;//点赞数量
    public List<FindCommentListEntity.LastLikesBean> last_likes;//点赞人的头像
    public List<FootprintEntity.DateInfo> date_info;//足迹时间列表

    public String tell;//帮助中心电话
}
