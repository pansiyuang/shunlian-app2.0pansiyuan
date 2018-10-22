package com.shunlian.app.view;

import com.shunlian.app.bean.UploadPicEntity;

import java.util.List;

/**
 * Created by zhanghe on 2018/10/22.
 */

public interface ISelectPicVideoView extends IView {
    /**
     * 设置凭证图片
     * @param pics
     */
    void setRefundPics(List<String> pics, boolean isShow);


    void uploadImg(UploadPicEntity picEntity);

    /**
     * 发布成功
     */
    void publishSuccess();
}
