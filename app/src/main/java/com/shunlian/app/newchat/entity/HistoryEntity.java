package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryEntity {
    public int count;
    public int page;
    public int page_size;
    public String last_send_time;
    public boolean surplus;
    public List<MsgInfo> list;
}
