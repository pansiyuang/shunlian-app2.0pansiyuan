package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/23.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamListEntity {

    public int page;
    public int page_size;
    public int count;
    public int total_page;

    public String  total_egg;  //累计获得金蛋数

    public String  join_num; //累计邀请人数

    public String  caption_time;  //当队长次数

    public List<TeamUserData> list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamUserData {
        public String text;//2019-02-18 13:08:42我参与的活动
        public String text2;//参与活动失败
        public String text3;//队伍未满2人，无法瓜分金蛋，下次努力

        public String head_1;//头部的信息
        public String head_2;//头部的信息
        public String head_3;//头部的信息
        public List<UserData> data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserData {
        public String nickname;  //昵称
        public String avatar;//"http://img.v2.shunliandongli.com/uploads/20180731/20180731182411341r.png_128x128.jpg", //头像
        public String join_num;//邀请人数
        public String end_egg;//分到金蛋数
    }
}
