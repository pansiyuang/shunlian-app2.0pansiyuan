package com.shunlian.app.view;

import com.shunlian.app.bean.BalanceDetailEntity;
import com.shunlian.app.bean.WithdrawListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IBalanceDetail extends IView {
    void setApiData(BalanceDetailEntity data, List<BalanceDetailEntity.Balance> balanceList);
    void setApiDatas(WithdrawListEntity.Pager pager, List<WithdrawListEntity.Record> records);
}
