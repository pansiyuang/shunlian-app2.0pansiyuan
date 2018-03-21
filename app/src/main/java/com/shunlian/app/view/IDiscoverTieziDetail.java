package com.shunlian.app.view;

import com.shunlian.app.bean.DiscoveryCommentListEntity;
import com.shunlian.app.bean.DiscoveryTieziEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public interface IDiscoverTieziDetail extends IView {
    void setApiData(DiscoveryCommentListEntity.Mdata data, List<DiscoveryCommentListEntity.Mdata.Commentlist> mdatas);
}
