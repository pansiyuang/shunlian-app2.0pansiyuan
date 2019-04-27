package com.shunlian.app.yjfk;


import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.view.IView;

import java.util.List;


/**
 * Created by Administrator on 2019/4/17.
 */

public interface IOpinionbackView extends IView {
    void getOpinionfeedback(OpinionfeedbackEntity entity);

    void uploadImg(UploadPicEntity uploadPicEntity);

    void setRefundPics(List<String> relativePath, boolean b);

    void submitSuccess(String message);
    void submitSuccess1(BaseEntity<Opinionfeedback1Entity> data);
    void getcomplaintTypes(ComplaintTypesEntity entity);
    void getcomplaintList(List<ComplanintListEntity.Lists> entity,int allPage, int page);
}
