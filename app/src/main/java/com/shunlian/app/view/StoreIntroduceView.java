package com.shunlian.app.view;

import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface StoreIntroduceView extends IView {
    void introduceInfo(StoreIntroduceEntity storeIntroduceEntity);
    void storeFocus();
}
