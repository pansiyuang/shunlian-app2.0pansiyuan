package com.shunlian.app.view;

import com.shunlian.app.bean.HelpcenterIndexEntity;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface IHelpOneView extends IView {
    void setApiData(HelpcenterIndexEntity helpcenterIndexEntity);
    void setPhoneNum(String phoneNum);
    void getUserId(String userId);
}
