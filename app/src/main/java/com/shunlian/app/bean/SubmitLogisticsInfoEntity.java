package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitLogisticsInfoEntity {

    public String express_com;
    public String express_sn;
    public String memo;
    public List<String> pics;
}
