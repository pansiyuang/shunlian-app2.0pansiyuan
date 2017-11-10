package com.shunlian.app.view;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/8.
 */

public interface IGoodsDetailView extends IView {

    /**
     * 轮播
     * @param pics
     */
    void banner(ArrayList<String> pics);


    /**
     * 商品的标题和价格
     * 是否包邮
     * 销售量
     * 发货地点
     */
    void goodsInfo(String title, String price,String market_price,String free_shipping,String sales,String address);

}
