package com.shunlian.app.view;

import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public interface IAddGoodsView extends IView {

    void getValidGoods(List<GoodsDeatilEntity.Goods> goodsList,int currentPage,int totalPage);
}
