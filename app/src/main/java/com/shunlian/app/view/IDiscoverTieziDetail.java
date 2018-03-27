package com.shunlian.app.view;

import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.DiscoveryCommentListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public interface IDiscoverTieziDetail extends IFindCommentListView {
    void setApiData(DiscoveryCommentListEntity.Mdata data, List<DiscoveryCommentListEntity.Mdata.Commentlist> mdatas);
    void dianZan(CommonEntity data);
    void faBu(DiscoveryCommentListEntity.Mdata.Commentlist data);
}
