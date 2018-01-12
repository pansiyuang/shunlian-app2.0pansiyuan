package com.shunlian.app.view;

import com.shunlian.app.bean.UploadPicEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public interface ISubmitLogisticsInfoView extends IView {

    /**
     * 设置物流公司
     * @param name
     */
    void setLogisticsName(String name);

    /**
     * 设置物流单号
     * @param code
     */
    void setLogisticsCode(String code);

    /**
     * 设置说明
     * @param memo
     */
    void setRefundMemo(String memo);

    /**
     * 设置凭证图片
     * @param pics
     */
    void setRefundPics(List<String> pics);


    void uploadImg(UploadPicEntity picEntity);

    void uploadProgress(int progress, String tag);

    /**
     * 提交成功
     */
    void submitSuccess();
}
