package com.shunlian.app.view;

import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.newchat.entity.ImageMessage;


/**
 * Created by Administrator on 2018/4/12.
 */

public interface IChatUpLoadImgView extends IView {

    void uploadImg(UploadPicEntity picEntity, String tagId, final ImageMessage imageMessage);

}
