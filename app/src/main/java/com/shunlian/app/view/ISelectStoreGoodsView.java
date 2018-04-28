package com.shunlian.app.view;

import com.shunlian.app.bean.StoreGoodsListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface ISelectStoreGoodsView extends IView {

    void getBabyList(List<StoreGoodsListEntity.MData> mDataList,int page,int totalPage);

    void refreshFinish();
}
