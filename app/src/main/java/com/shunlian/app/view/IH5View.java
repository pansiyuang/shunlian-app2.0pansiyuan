package com.shunlian.app.view;

import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ShareEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.newchat.entity.ImageMessage;
import com.shunlian.app.newchat.entity.MsgInfo;

import java.util.List;


/**
 * Created by Administrator on 2018/4/12.
 */

public interface IH5View extends IView {

    void setShareInfo(ShareEntity commonEntity);

}
