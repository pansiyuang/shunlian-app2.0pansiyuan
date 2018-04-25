package com.shunlian.app.view;

import com.shunlian.app.bean.UploadPicEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 */

public interface INewFeedBackView extends IView {

    /**
     * 设置凭证图片
     * @param pics
     */
    void setRefundPics(List<String> pics, boolean isShow);


    void uploadImg(UploadPicEntity picEntity);

    void uploadProgress(int progress, String tag);


    void submitSuccess(String msg);

}
