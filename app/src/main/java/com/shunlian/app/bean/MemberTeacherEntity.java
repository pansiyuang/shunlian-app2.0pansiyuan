package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

public class MemberTeacherEntity implements Serializable{

    public MemberInfoEntity.Followfrom follow_from;//上级的微信

    public String my_weinxin;// 自己的微信

    public MemberTeacherEntity.Followfrom system_weixin;////官网导师的微信

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Followfrom implements Serializable {
        public String weixin_qrcode;//二维码链接
        public String weixin;//微信
    }
}
