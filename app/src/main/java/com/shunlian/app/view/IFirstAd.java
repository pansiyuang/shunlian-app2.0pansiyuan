package com.shunlian.app.view;

import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface IFirstAd extends IView {
   void setGoods(List<GoodsDeatilEntity.Goods> mDatas , int page, int allPage);
}
