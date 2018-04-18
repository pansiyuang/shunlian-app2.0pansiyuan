package com.shunlian.app.view;

import com.shunlian.app.bean.AmountDetailEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IAmountDetail extends IView {
    void setApiData(List<AmountDetailEntity.Content> amountDetailEntities);
}
