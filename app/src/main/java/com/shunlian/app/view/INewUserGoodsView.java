package com.shunlian.app.view;

import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public interface INewUserGoodsView extends IView {

    /**
     * 用户商品信息
     * @param collectionGoodsLists
     */
    void userGoodsList(int page, int allPage, List<NewUserGoodsEntity.Goods> collectionGoodsLists);

    /**
     * 显示商品属性
     * @param goods
     */
    void showGoodsSku(GoodsDeatilEntity.Goods goods,int postion);

    /**
     * 刷新完成
     */
    void refreshFinish();

    /**
     */
    void addCartSuccess(int cartNum,int postion);

}
