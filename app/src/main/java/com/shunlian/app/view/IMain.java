package com.shunlian.app.view;

import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.UpdateEntity;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IMain extends IView {
    void setAD(AdEntity data);
    void setUpdateInfo(UpdateEntity data);
}
