package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceEntity {
    public String service_num;
    public List<ChatMemberEntity.ChatMember> list;
}
