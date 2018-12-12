package com.shunlian.app.eventbus_bean;

/**
 * Created by zhanghe on 2018/8/30.
 */

public class MemberInfoEvent {

    public String weixinNum;//微信号

    public String code;//推荐码
    public String avatar; //头像
    public String nickname;//昵称
    public String weixin;//微信

    public MemberInfoEvent(String weixinNum){
        this.weixinNum = weixinNum;
    }

    public MemberInfoEvent(String code,String avatar,String nickname,String weixin){
        this.code = code;
        this.avatar = avatar;
        this.nickname = nickname;
        this.weixin = weixin;
    }
}
