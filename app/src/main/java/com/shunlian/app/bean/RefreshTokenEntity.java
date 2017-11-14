package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenEntity {
    public String token;
    public String refresh_token;
    public String member_id;
    public String login_status;
    public String token_expires_in;
    public String expires_in;
    public String update_time;
}
