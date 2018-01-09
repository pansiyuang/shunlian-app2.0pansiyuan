package com.shunlian.app.view;

import com.shunlian.app.bean.UploadPicEntity;

/**
 * Created by Administrator on 2018/1/8.
 */

public interface IPlatformInterventionRequestView extends IView {

    void requestSuccess(String msg);

    void requestFail(String errorMsg);

    void uploadImg(UploadPicEntity entity);
}
