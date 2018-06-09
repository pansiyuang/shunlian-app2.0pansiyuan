package com.shunlian.app.view;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.RegisterFinishEntity;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IRegisterTwoView extends IView {
    void resetPsw(String message);
    void registerFinish(BaseEntity<RegisterFinishEntity> entity);
    void smsCode(String smsCode);
}
