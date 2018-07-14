package com.shunlian.app.view;

import com.shunlian.app.bean.PhoneRecordEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public interface IPhoneRecord extends IView {
    void setApiData(PhoneRecordEntity.Pager data, List<PhoneRecordEntity.MData> mdatas);
}
