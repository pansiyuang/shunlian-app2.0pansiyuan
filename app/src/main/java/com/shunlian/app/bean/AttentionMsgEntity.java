package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/25.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttentionMsgEntity {
    public int page;
    public int page_size;
    public int total;
    public int total_page;
    public List<HotBlogsEntity.MemberInfo> list;
}
