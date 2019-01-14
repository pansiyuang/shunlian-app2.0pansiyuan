package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayGiveEggEntity {
    public String total_gold_egg;
    public String title;
    public String content;
    public String over_task;
    public String all_task;
}
