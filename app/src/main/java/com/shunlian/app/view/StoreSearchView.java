package com.shunlian.app.view;

import com.shunlian.app.bean.StoreGoodsListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface StoreSearchView extends IView {
    void storeBaby(List<StoreGoodsListEntity.MData> mDataLis , int allPage, int page);
}
