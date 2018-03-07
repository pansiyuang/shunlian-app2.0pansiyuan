package com.shunlian.app.view;

import com.shunlian.app.bean.CheckInRespondEntity;
import com.shunlian.app.bean.CheckInStateEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface ISignInView extends IView {
    void signCallBack(CheckInRespondEntity checkInRespondEntity);
    void setApiData(CheckInStateEntity checkInStateEntity, List<CheckInStateEntity.GoodsList.MData> mDataList);
}
