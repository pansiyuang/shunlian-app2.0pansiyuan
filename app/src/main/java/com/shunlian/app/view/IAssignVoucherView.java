package com.shunlian.app.view;

import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.VoucherEntity;

/**
 * Created by Administrator on 2018/7/20.
 */

public interface IAssignVoucherView extends IView {

    void getVoucherDetail(VoucherEntity voucherEntity);
    void refreshVoucherState(GoodsDeatilEntity.Voucher voucher);
}
