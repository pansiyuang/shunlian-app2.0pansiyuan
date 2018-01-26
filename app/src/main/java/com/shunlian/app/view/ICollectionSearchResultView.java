package com.shunlian.app.view;

import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public interface ICollectionSearchResultView extends IView {

    /**
     * 收藏商品列表
     * @param collectionGoodsLists
     */
    void collectionGoodsList(int page,int allPage,
                             List<CollectionGoodsEntity.Goods> collectionGoodsLists);

    /**
     * 收藏店铺列表
     * @param stores
     */
    void collectionStoresList(int page, int allPage, List<CollectionStoresEntity.Store> stores);

    /**
     * 显示商品属性
     * @param goods
     */
    void showGoodsSku(GoodsDeatilEntity.Goods goods);

    /**
     * 删除成功
     */
    void delSuccess();
}
