package com.shunlian.app.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 * 提交商品实体类
 */
public class SubmitGoodsEntity {

    public String store_id;
    public String remark; //备注
    public List<GoodsItems> goods_items;
    public String voucher_id;


    public static class GoodsItems{
        public String prom_id;//促销id
        public String goods_id;
        public String sku_id;
        public String qty;
    }

}
