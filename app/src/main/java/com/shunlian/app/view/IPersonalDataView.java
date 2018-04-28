package com.shunlian.app.view;

/**
 * Created by Administrator on 2018/4/23.
 */

public interface IPersonalDataView extends IView {


    void setLocation(String district,String district_ids);

    void setAvatar(String avatar);

    void setSex(String sex);

    void setNickname(String nickname);

    /**
     * 设置个性签名
     * @param signature
     */
    void setSignature(String signature);

    void setBirth(String birth);

    /**
     * 设置标签
     * @param tag
     */
    void setTag(String tag);

    /**
     * 设置凭证图片
     * @param url
     */
    void setRefundPics(String url,String domain);


//    void uploadImg(UploadPicEntity picEntity);

//    void uploadProgress(int progress, String tag);
}
