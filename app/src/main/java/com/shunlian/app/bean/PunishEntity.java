package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/5/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PunishEntity {

    public String id;
    public String title;
    public String desc;
    public String remark;
    public String complaints;
    public String pic_url;
    public String member_id;
    public String nickname;
}
