package com.shunlian.app.view;

import com.shunlian.app.bean.GetQrCardEntity;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IQrCode extends IView {
    void setApiData(GetQrCardEntity data);
}
