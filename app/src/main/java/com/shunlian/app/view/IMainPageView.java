package com.shunlian.app.view;

import com.shunlian.app.bean.MainPageEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/1.
 */

public interface IMainPageView extends IView {
    /**
     * 首页轮播
     * @param banners
     */
    void banner(List<MainPageEntity.Banner> banners);

    /**
     * 天天特惠和新品
     * @param daySpecial
     * @param newGoods
     */
    void daySpecialAndNewGoods(MainPageEntity.DaySpecial daySpecial,MainPageEntity.NewGoods newGoods);

    /**
     * 更多品牌
     * @param brands
     */
    void moreBrands(MainPageEntity.RecommendBrands brands);

    /**
     * 精选商品
     * @param goods
     */
    void moreGoods(MainPageEntity.RecommendBrands goods);
}
