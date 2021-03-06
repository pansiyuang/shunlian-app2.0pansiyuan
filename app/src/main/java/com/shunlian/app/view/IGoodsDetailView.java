package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.CommentAdapter;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;

/**
 * Created by Administrator on 2017/11/8.
 */

public interface IGoodsDetailView extends IView {

    void setBubble(BubbleEntity data);

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

    /**
     * 专题活动
     */
    void specailAct();

    /**
     * 三个点下拉列表
     * @param adapter
     */
    void setAdapter(BaseRecyclerAdapter adapter);

    void getUserId(String userId);

    /**
     * 设置评价adapter
     * @param adapter
     */
    default void setCommentAdapter(CommentAdapter adapter){}

    /**
     * 优品
     */
    default void superiorProduct(){}

    /**
     * 团购
     */
    default void groupBuy(){}

    /**
     * 库存不足
     */
    void stockDeficiency(String stock);

    /**
     * 停留获得金蛋
     */
    default void stayObtainEggs(int time){}

    /**
     * 底部导航
     * @param self_buy 自购
     * @param share_buy 分享赚
     */
    default void selfBuyAndShareBuyBottomBtn(String self_buy,String share_buy){}

    /**
     * 评价列表内容置顶
     */
    default void setCommentRecyViewTop(){}
}
