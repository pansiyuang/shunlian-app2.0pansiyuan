package com.shunlian.app.view;

import com.shunlian.app.bean.ConfirmOrderEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public interface IOrderAddressView extends IView {
    void orderList(List<ConfirmOrderEntity.Address> addressList);

    void delAddressSuccess(String addressId);

    void delAddressFail();
}
