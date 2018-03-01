package com.shunlian.app.view;

import com.shunlian.app.bean.SearchGoodsEntity;

/**
 * Created by Administrator on 2018/3/1.
 */

public interface IGoodsSearchView extends IView {
    void getSearchGoods(SearchGoodsEntity goodsEntity , int page, int allPage);

    void getFairishNums(String count);

    void addStoreFinish(String count);
}
