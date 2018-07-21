package com.shunlian.app.view;

import com.shunlian.app.bean.HelpSearchEntity;
import com.shunlian.app.bean.HelpcenterQuestionEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface ISearchQView extends IView {
    void setApiData(List<HelpSearchEntity.Content> contents);
    void getUserId(String userId);
    void setPhoneNum(String phoneNum);
}
