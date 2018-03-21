package com.shunlian.app.view;

import com.shunlian.app.bean.DiscoveryMaterialEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public interface IDiscoverSucaiku extends IView {
    void setApiData(DiscoveryMaterialEntity data,List<DiscoveryMaterialEntity.Content> datas);
}
