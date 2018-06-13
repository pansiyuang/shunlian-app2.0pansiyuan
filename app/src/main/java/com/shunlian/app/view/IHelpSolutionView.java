package com.shunlian.app.view;

import com.shunlian.app.bean.HelpcenterSolutionEntity;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface IHelpSolutionView extends IView {
    void setApiData(HelpcenterSolutionEntity solution);
    void initFeedback(boolean isSolved);
    void callFeedback();
    void getUserId(String userId);
}
