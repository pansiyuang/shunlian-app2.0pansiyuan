package com.shunlian.app.view;

import com.shunlian.app.bean.DiscoveryCircleEntity;
import com.shunlian.app.bean.DiscoveryTieziEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public interface IDiscoverTiezi extends IView {
    void setApiData(DiscoveryTieziEntity.Mdata data, List<DiscoveryTieziEntity.Mdata.Hot> mdatas);
}
