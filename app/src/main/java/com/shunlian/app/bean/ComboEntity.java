package com.shunlian.app.bean;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/22.
 */

public class ComboEntity {

    public String combo_id;

    public Map<String,String> goods_sku;

    public ComboEntity(String combo_id, Map<String, String> goods_sku) {
        this.combo_id = combo_id;
        this.goods_sku = goods_sku;
    }
}
