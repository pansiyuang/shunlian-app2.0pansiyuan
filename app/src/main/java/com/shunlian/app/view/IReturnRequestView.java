package com.shunlian.app.view;

import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.bean.UploadPicEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public interface IReturnRequestView extends IView {
    void applyRefundSuccess(String refoundId);

    void getReasonList(List<RefundDetailEntity.RefundDetail.Edit.Reason> reasonList);

    void applyRefundFail(String error);

    void uploadImg(UploadPicEntity entity);
}
