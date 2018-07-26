package com.shunlian.app.view;

import com.shunlian.app.bean.VouchercenterplEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface IGetCoupon extends IView {
   void setpingData(VouchercenterplEntity vouchercenterplEntity);
   void setdianData(List<VouchercenterplEntity.MData> mData, String page, String total);
   void getCouponCallBack(boolean isCommon,int position,String isGet);
}
