package com.shunlian.app.view;

import com.shunlian.app.bean.UploadPicEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public interface IExperiencePublishView extends IView {

    void setPics(List<String> pics, boolean isShow);

    void uploadImg(UploadPicEntity picEntity);

    void uploadProgress(int progress, String tag);

    void creatExperienctSuccess();
}
