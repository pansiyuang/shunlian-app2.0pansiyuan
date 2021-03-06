package com.shunlian.app.view;

import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.AdUserEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.bean.ShowVoucherSuspension;
import com.shunlian.app.bean.UserNewDataEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public interface INewUserPageView extends IView {
  void setBubble(BubbleEntity data);
  void bannerList(List<AdUserEntity.AD> adList,boolean isNew);
  void goodCartList(List<NewUserGoodsEntity.Goods> goodsList,boolean isFrist);
  void delCart(String cid);
  void getvoucher(UserNewDataEntity userNewDataEntity);
  void getOldMessage(String message,int code);
  void showVoucherSuspension(ShowVoucherSuspension voucherSuspension);
}
