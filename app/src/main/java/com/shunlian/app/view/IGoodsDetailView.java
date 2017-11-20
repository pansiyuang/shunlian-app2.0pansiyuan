package com.shunlian.app.view;

import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */

public interface IGoodsDetailView extends IView {

    /**
     * 轮播
     * @param pics
     */
    void banner(ArrayList<String> pics);


    /**
     * 商品的标题和价格
     * 是否包邮
     * 销售量
     * 发货地点
     */
    void goodsInfo(String title, String price, String market_price, String free_shipping, String sales, String address);


    /**
     * 商品详情数据
     */
    void goodsDetailData(GoodsDeatilEntity entity);

    /**
     * 选择商品对话框
     */
    void attributeDialog();


    /**
     *
     * @param is_new 是否新品
     * @param is_explosion 是否爆款
     * @param is_hot 是否热卖
     * @param is_recommend 是否推荐
     */
    void smallLabel(String is_new,String is_explosion, String is_hot,String is_recommend);

    /**
     * 优惠券
     * @param vouchers
     */
    void voucher(ArrayList<GoodsDeatilEntity.Voucher> vouchers);

//    public String decoration_name;//店铺名字
//    public String star; //星级
//    public String quality_logo;  //是否有品质标志1是0否
//    public String goods_count;    //商品数量
//    public String decoration_banner;    //店铺背景图
//    public String attention_count;      //收藏数
//    public String description_consistency;      //描述相符度
//    public String quality_satisfy;     //质量满意度
    /**
     * 店铺信息
     * @param storeInfo
     */
    void shopInfo(GoodsDeatilEntity.StoreInfo storeInfo);

    /**
     * 是否喜爱该商品
     * @param is_fav
     */
    void isFavorite(String is_fav);

    /**
     * 套餐列表
     * @param combos
     */
    void comboDetail(List<GoodsDeatilEntity.Combo> combos);

    /**
     * 商品参数列表
     * @param attrses
     */
    void goodsParameter(List<GoodsDeatilEntity.Attrs> attrses);

    /**
     * 评价列表
     * @param commentses
     */
    void commentList(List<GoodsDeatilEntity.Comments> commentses);

    /**
     *商品详情
     * @param detail
     */
    void detail(GoodsDeatilEntity.Detail detail);
}
