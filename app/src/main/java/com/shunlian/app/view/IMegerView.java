package com.shunlian.app.view;

import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;


/**
 * Created by Administrator on 2017/12/8.
 */

public interface IMegerView extends IView {

    void getCateEntity(CateEntity cateEntity);

    void getGoodsInfo(GoodsDeatilEntity.GoodsInfo goodsInfo);

    void addCart(CateEntity cateEntity);
}
