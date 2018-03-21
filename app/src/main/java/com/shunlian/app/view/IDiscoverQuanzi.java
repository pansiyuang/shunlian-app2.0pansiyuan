package com.shunlian.app.view;

import com.shunlian.app.bean.DiscoveryCircleEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public interface IDiscoverQuanzi extends IView {
    void setApiData(DiscoveryCircleEntity.Mdata data, List<DiscoveryCircleEntity.Mdata.Content> mdatas);
}
