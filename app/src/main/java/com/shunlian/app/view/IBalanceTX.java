package com.shunlian.app.view;

import com.shunlian.app.bean.CommonEntity;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IBalanceTX extends IView {
    void setApiData(CommonEntity data);
    void tiXianCallback(CommonEntity data,int code, String message);
}
