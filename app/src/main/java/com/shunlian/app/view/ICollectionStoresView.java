package com.shunlian.app.view;

import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public interface ICollectionStoresView extends IView {

    /**
     * 收藏店铺列表
     * @param stores
     */
    void collectionStoresList(int page, int allPage, List<CollectionStoresEntity.Store> stores);

    /**
     * 分类
     * @param cates
     */
    void collectionStoresCategoryList(List<CollectionStoresEntity.Cates> cates);

    /**
     * 删除成功
     */
    void delSuccess();
}
