package com.shunlian.app.view;

import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListOneEntity;
import com.shunlian.app.bean.StorePromotionGoodsListTwoEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface StoreView extends IView {
    void storeFirst(List<StoreIndexEntity.Body> bodies);
    void storeBaby(List<StoreGoodsListEntity.MData> mDataLis ,int allPage, int page);
    void storeDiscountOne(List<StorePromotionGoodsListOneEntity.MData> mDatas,int allPage, int page);
    void storeNew(List<StoreNewGoodsListEntity.Data> storeNewGoodsListEntities);
    void storeHeader(StoreIndexEntity.Head head);
    void storeDiscountMenu(List<StorePromotionGoodsListEntity.Lable> datas);
    void storeVoucher(List<GoodsDeatilEntity.Voucher> vouchers);
    void storeFocus();
    void storeDiscountTwo(List<StorePromotionGoodsListTwoEntity.Lists.Good> mDatas);
    void refreshVoucherState(GoodsDeatilEntity.Voucher voucher);
    void getUserId(String userId);
}
