package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonEntity {
    public String fid;//商品收藏id
    public String message;
    public String praise_total;//总赞数
    public List<String> suggest_list; // 搜索关键字提示接口
}
