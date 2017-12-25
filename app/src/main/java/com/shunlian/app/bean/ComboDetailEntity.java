package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComboDetailEntity {

    public GoodsDeatilEntity.Combo current_combo;

    public List<GoodsDeatilEntity.Combo> others_combo;




}
