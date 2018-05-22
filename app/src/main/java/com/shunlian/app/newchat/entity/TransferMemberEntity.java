package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/5/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferMemberEntity {
    public String sid;
    public String m_user_id;
    public String kid;
    public String user_id;
    public String status;
    public String re_time;
    public String q_time;
    public String headurl;
    public String nickname;
    public String line_status;
    public String member_id;
    public String message_type;
    public String un_read_num;
}
