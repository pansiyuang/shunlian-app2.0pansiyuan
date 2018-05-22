package com.shunlian.app.view;

import com.shunlian.app.bean.HelpClassEntity;
import com.shunlian.app.bean.HelpcenterQuestionEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface IHelpTwoView extends IView {
    void setCateOne(HelpcenterQuestionEntity helpcenterQuestionEntity);
    void setCateTwo(HelpcenterQuestionEntity helpcenterQuestionEntity,List<HelpcenterQuestionEntity.Question> questions);
    void setClass(HelpClassEntity helpClassEntity, List<HelpClassEntity.Article> articles);
    void getUserId(String userId);
}
