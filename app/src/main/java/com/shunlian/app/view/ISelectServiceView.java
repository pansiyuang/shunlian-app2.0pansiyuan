package com.shunlian.app.view;

import com.shunlian.app.bean.RefundDetailEntity;

/**
 * Created by Administrator on 2017/12/27.
 */

public interface ISelectServiceView extends IView {

    void getRefundInfo(RefundDetailEntity.RefundDetail.Edit infoEntity);
}
