package com.shunlian.app.view;

import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.newchat.entity.ImageMessage;
import com.shunlian.app.newchat.entity.MsgInfo;

import java.util.List;


/**
 * Created by Administrator on 2018/4/12.
 */

public interface IChatView extends IView {

    void uploadImg(UploadPicEntity picEntity, String tagId, final ImageMessage imageMessage);

    void getHistoryMsg(List<MsgInfo> msgInfoList, String lastSendTime, boolean hasMore);
}
