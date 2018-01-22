package com.shunlian.app.view;

import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.bean.FootprintEntity;
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

    /**
     *足迹列表
     * @param footprintEntity
     */
    void footprintList(FootprintEntity footprintEntity);

    /**
     * 评价列表数据
     * @param entity
     */
    void commentListData(CommentListEntity entity);

    /**
     * 评价总数量
     * @param praiseTotal
     */
    void praiseTotal(String praiseTotal);

    /**
     * 刷新优惠券状态
     * @param voucher
     */
    void refreshVoucherState(GoodsDeatilEntity.Voucher voucher);

    /**
     * 商品是否下架
     * @param status
     */
    void goodsOffShelf(String status);

    /**
     * 活动状态
     * @param status 活动状态
     * @param remindStatus 提醒状态
     */
    void activityState(String status,String remindStatus);
}
