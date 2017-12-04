package com.shunlian.app.view;

import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShoppingCarEntity;

/**
 * Created by Administrator on 2017/11/18.
 */

public interface IShoppingCarView extends IView {

    void OnShoppingCarEntity(ShoppingCarEntity shoppingCarEntity);

    void OnEditEntity(ShoppingCarEntity shoppingCarEntity);

    void OnGetVoucher(GoodsDeatilEntity.Voucher voucher);

}
