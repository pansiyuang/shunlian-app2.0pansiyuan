package com.shunlian.app.view;

import com.shunlian.app.bean.UploadPicEntity;

/**
 * Created by Administrator on 2017/12/13.
 */

public interface ICommentView extends IView {

    void uploadImg(UploadPicEntity picEntity);

    void uploadProgress(int progress, String tag);

    void appendCommentSuccess();

    void appendCommentFail(String error);

    void changeCommentSuccess();

    void changeCommtFail(String errorstr);
}
