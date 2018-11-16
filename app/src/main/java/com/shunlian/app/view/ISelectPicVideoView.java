package com.shunlian.app.view;

import com.shunlian.app.bean.BlogDraftEntity;
import com.shunlian.app.bean.UploadPicEntity;

/**
 * Created by zhanghe on 2018/10/22.
 */

public interface ISelectPicVideoView extends IView {


    void uploadImg(UploadPicEntity picEntity);

    /**
     * 上传视频成功
     * @param url 服务端视频地址
     */
    void uploadViodeSuccess(String url,String local_path);

    /**
     * 发布成功
     */
    void publishSuccess(String draft);

    /**
     * 显示草稿
     * @param entity
     */
    void resetDraft(BlogDraftEntity entity);

    /**
     * 视频封面地址
     * @param s
     */
    void videoThumb(String s);
}
