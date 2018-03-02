package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonShopEntity {
    public String nickname;
    public String avatarl;
    public String level;
    public List<GoodsDeatilEntity.Goods> goods_list;
    public String qrcode;
}
