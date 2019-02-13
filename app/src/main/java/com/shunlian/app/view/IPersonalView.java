package com.shunlian.app.view;

import com.shunlian.app.bean.MemberTeacherEntity;
import com.shunlian.app.bean.PersonalcenterEntity;


/**
 * Created by Administrator on 2017/12/8.
 */

public interface IPersonalView extends IView {
    void getApiData(PersonalcenterEntity personalcenterEntity);
    void getUserId(String userId);
    void teacherCodeInfo(MemberTeacherEntity memberTeacherEntity);
}
