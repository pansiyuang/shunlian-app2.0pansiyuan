package com.shunlian.app.view;

import com.shunlian.app.bean.RankingListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9.
 */

public interface IRankingListView extends IView {

    /**
     * 排行分类列表
     * @param categoryList
     */
    void rankingCategoryList(List<RankingListEntity.Category> categoryList);

    /**
     * 排行商品列表
     * @param goods
     */
    void rankingGoodsList(RankingListEntity.Goods goods);
}
