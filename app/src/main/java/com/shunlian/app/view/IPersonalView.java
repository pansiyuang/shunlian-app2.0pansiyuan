package com.shunlian.app.view;

import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.bean.SignEggEntity;


/**
 * Created by Administrator on 2017/12/8.
 */

public interface IPersonalView extends IView {
    void getApiData(PersonalcenterEntity personalcenterEntity);
    void signEgg(SignEggEntity signEggEntity);
}
