package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/10/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterFinishEntity {

    /**
     * member_id : 90000003
     * mobile : 13007562706
     * shareid : 6
     * code : 9090011211
     * username : sldl_CdPDOlsZPteu
     * nickname : hhhh
     * avatar : 
     * is_agent : 1
     * time : 1508741682
     * token_expires_in : 604800
     * token : 64b9Fsd/ztU42bP4FWYSl98/7Fgu+Ul78SgXXhD3GVCBPXHXEVW8HydPAO2kiEDI//KjvGhbFHi23yjOhtt4kUpaRIQOznvn8ZTuePwYKPEMenS1zUZw1f9uIxlWwVh/qUPz4BCqppuJTBaNv+wG
     * balance : 0.00
     * credit : 0
     */

    public String member_id;
    public String mobile;
    public String shareid;
    public String code;
    public String username;
    public String nickname;
    public String avatar;
    public String is_agent;
    public int time;
    public int token_expires_in;
    public String token;
    public String balance;
    public String credit;
    public String refresh_token;
    public String is_tag;
    public String plus_role;

    @Override
    public String toString() {
        return "RegisterFinishEntity{" +
                "member_id='" + member_id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", shareid='" + shareid + '\'' +
                ", code='" + code + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", is_agent='" + is_agent + '\'' +
                ", time=" + time +
                ", token_expires_in=" + token_expires_in +
                ", token='" + token + '\'' +
                ", balance='" + balance + '\'' +
                ", credit='" + credit + '\'' +
                '}';
    }
}
