package com.shunlian.app.view;

import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public interface ICollectionGoodsView extends IView {

    /**
     * 收藏商品列表
     * @param collectionGoodsLists
     */
    void collectionGoodsList(int page,int allPage,List<CollectionGoodsEntity.Goods> collectionGoodsLists);

    /**
     * 分类
     * @param cates
     */
    void collectionGoodsCategoryList(List<CollectionGoodsEntity.Cates> cates);

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
