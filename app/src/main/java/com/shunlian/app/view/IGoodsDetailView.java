package com.shunlian.app.view;

import com.shunlian.app.bean.GoodsDeatilEntity;

/**
 * Created by Administrator on 2017/11/8.
 */

public interface IGoodsDetailView extends IView {

    /**
     * 商品详情数据
     */
    void goodsDetailData(GoodsDeatilEntity entity);

    /**
     * 是否喜爱该商品
     * @param is_fav
     */
    void isFavorite(String is_fav);

    /**
     * 添加购物车
     */
    void addCart(String msg);
}
