package com.shunlian.app.view;

import com.shunlian.app.bean.PhoneOrderDetailEntity;
import com.shunlian.app.newchat.entity.MsgInfo;

import java.util.List;


/**
 * Created by Administrator on 2018/4/12.
 */

public interface IPhoneOrder extends IView {
    void setApiData(PhoneOrderDetailEntity phoneOrderDetailEntity);
}
