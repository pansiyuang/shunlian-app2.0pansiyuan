package com.shunlian.app.view;

import com.shunlian.app.bean.UploadPicEntity;

/**
 * Created by Administrator on 2017/12/28.
 */

public interface IReturnRequestView extends IView {
    void applyRefundSuccess(String refoundId);

    void applyRefundFail(String error);

    void uploadImg(UploadPicEntity entity);
}
