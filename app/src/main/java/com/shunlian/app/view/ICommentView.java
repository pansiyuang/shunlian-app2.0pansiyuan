package com.shunlian.app.view;

import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.bean.UploadPicEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public interface ICommentView extends IView {

    void uploadImg(UploadPicEntity picEntity);

    void uploadProgress(int progress, String tag);

    void CommentSuccess();

    void CommentFail(String error);

    void getCreatCommentList(List<ReleaseCommentEntity> entityList);
}
