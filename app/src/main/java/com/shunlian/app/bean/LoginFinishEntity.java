package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginFinishEntity {
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
    public List<String> tag;
}
