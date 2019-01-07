package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WXLoginEntity {


    /**
     * id : 2
     * unique_sign : oofPYwXXvrrGuQ6JzVogOCcrs91A
     * member_id : 0
     */

    public String id;
    public String unique_sign;
    public String member_id;
    public String status;
    public String mobile;
    public String shareid;
    public String code;
    public String username;
    public String nickname;
    public String avatar;
    public String is_agent;
    public String time;
    public String token_expires_in;
    public String token;
    public String refresh_token;
    public String plus_role;
    public String is_tag;
    public String share_status;//是否有邀请码 1有  2无
    public List<String> tag;

    @Override
    public String toString() {
        return "WXLoginEntity{" +
                "id='" + id + '\'' +
                ", unique_sign='" + unique_sign + '\'' +
                ", member_id='" + member_id + '\'' +
                ", status='" + status + '\'' +
                ", mobile='" + mobile + '\'' +
                ", shareid='" + shareid + '\'' +
                ", code='" + code + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", is_agent='" + is_agent + '\'' +
                ", time='" + time + '\'' +
                ", token_expires_in='" + token_expires_in + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
