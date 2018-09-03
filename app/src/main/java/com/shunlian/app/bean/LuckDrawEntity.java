package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/9/3.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class LuckDrawEntity {
    public String id;
    public String tmt_id;
    public String trophy_name;
    public String meg;
    public String thumb;
    public int trophy_type;
    public int give_num;
}
