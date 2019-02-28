package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamIndexEntity {
    public String total_egg;  //奖金池金蛋总数
    public List<EggHistory>  egg_history;//金蛋变更记录

    public List<TeamPlayer>   team_player;// //队员   第一个是队长

    public String  text;// "每邀请1位，奖池增加20金蛋；满3人/6人，最高再加888金蛋

    public String  text2;//成为队长最高瓜分1280金蛋",

    public String  strategy_url; //新手策略

    public String  rule_url; //游戏规则

    public String   button_text;// 立即邀请

    public String   password; //口令

    public  PopData1  pop_1; //有则弹   优先pop_2

    public  PopData2  pop_2;//有则弹

    public  PopData3  pop_3;//有则弹

    public String     button_type;//   1我要当队长    2立即邀请   3人员齐全等待开奖   4 倒计时 等下一场

    public String   down_time;//  倒计时用得秒数

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EggHistory{
        public String  nickname;   //昵称
        public String  avatar;  //头像
        public String  egg;//奖金池增加金蛋数
        public String  tm;//1550466522
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamPlayer{
        public String  nickname;   //昵称
        public String  avatar;  //头像
        public String  join_num;//邀请人数
        public String  egg_num;//预计分得金蛋
        public boolean isUser;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PopData1{
        public String  nickname;   //昵称"自己的昵称",
        public String  text;  //您被好友李白邀请了
        public String   content;//金蛋奖池+20金蛋"
        public String  avatar;//
        public String  nickname2;   //"邀请人的昵称",
        public String  content_egg;//加的金蛋数
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
  public static class PopData2{
      public String  qcode_pic;   //
      public String  text;  //您被好友李白邀请了
      public String  text2;  //恭喜！金蛋奖池额外增加888金蛋
      public String   content;//队员人数满6人啦
      public String    text2_egg; //text2里的金蛋数
      public String    content_egg;//content里的金蛋数
  }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PopData3{
        public String  nickname;   //昵称"自己的昵称",
        public String  text;  //您被好友李白邀请了
        public String   content;//金蛋奖池+20金蛋"
        public String  avatar;//
        public String  nickname2;   //"邀请人的昵称",
    }

}
