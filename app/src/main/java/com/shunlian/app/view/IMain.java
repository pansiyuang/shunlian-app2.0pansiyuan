package com.shunlian.app.view;

import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.CommondEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.UpdateEntity;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IMain extends IView {
    void setAD(AdEntity data);
    void setContent(GetDataEntity data);
    void setTab(GetMenuEntity data);
    void setCommond(CommondEntity data);
    void setUpdateInfo(UpdateEntity data);
    void entryInfo(CommonEntity data);
    void setDiscoveryUnreadCount(CommonEntity data);
}
