package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginFinishEntity {
    public String id;
    public String unique_sign;
    public String status;
    public String member_id;
    public String mobile;
    public String shareid;
    public String code;
    public String username;
    public String nickname;
    public String avatar;
    public String is_agent;
    public String time;
    public String salt;
    public String refresh_token;
    public String refresh_expires_in;
    public String token;
    public String expires_in;
    public String plus_role;
    public String share_status;//是否有邀请码 1有  2无
    public String is_tag;//是否有标签1是0否
    public List<String> tag;
}
