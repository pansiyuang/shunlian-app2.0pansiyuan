package com.shunlian.app.view;

import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface StoreView extends IView {
    void storeFirst(List<StoreIndexEntity.Body> bodies);
    void storeBaby(List<StoreGoodsListEntity.MData> mDataLis ,int allPage, int page);
    void storeDiscount(StorePromotionGoodsListEntity storePromotionGoodsListEntity,
                       List<StorePromotionGoodsListEntity.Lists.Good.Data> datas,int allPage,int page);
    void storeNew(List<StoreNewGoodsListEntity.Data> storeNewGoodsListEntities);
    void storeHeader(StoreIndexEntity.Head head);
    void storeDiscountMenu(List<StorePromotionGoodsListEntity.Lable> datas);
    void storeVoucher(List<StoreIndexEntity.Voucher> vouchers);
    void storeFocus();
}
