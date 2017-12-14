package com.shunlian.app.view;

/**
 * Created by Administrator on 2017/12/13.
 */

public interface ICommentView extends IView {

    void appendCommentSuccess();

    void appendCommentFail(String error);
}
